<!--
  Copyright (c) 2015-$today.year Dilvan Moreira.
  Copyright (c) 2015-$today.year John Garavito.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  -->

<div class="row">
    <div class="col-md-12">
        <form id="ontology_form" action="/admin/ontology" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary" data-loading-text="<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span> Saving..."> Save </button>
        </form>
        <form id="reset_ontology_form" action="/admin/ontologyReset" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restore </button>
        </form>
    </div>
</div>
<div class="row">
    <div class="col-md-3 col-sm-3 treesViews">
        <p class="title">Classes</p>
        <div id="classesTree">

        </div>
        <p class="title">Properties</p>
        <div id="propertiesTree">

        </div>
        <p class="title">Individuals</p>
        <div id="individualsTree">

        </div>
    </div>
    <div class="col-md-9 col-sm-9">
        <pre id="ontEditor" class="ace_editor editor ace-tm">${ontology}</pre>
    </div>
    <script type="application/javascript" charset="utf-8">
        $(document).ready(function() {
            var ontology;
            var classes = [];

            function getLabel(id, lang){
                if(ontology[id]){
                    var label = '';
                    for(var lg in ontology[id]['label']){
                        if(ontology[id]['label'][lg].search(lang) != -1){
                            label = ontology[id]['label'][lg].replace('@'+lang, '');
                        }
                    }
                    return label
                }
                else{
                    return id
                }
            }

            function setRootNodes(id, property){
                if(ontology[id] && ontology[id][property]){
                    setRootNodes(ontology[id][property], property);
                }
                else{
                    var exist = false;
                    for(var elId in classes){
                        if(classes[elId].id == id){
                            exist = true;
                        }
                    }
                    if(!exist){
                        classes.push({id: id, text: getLabel(id, 'pt'), nodes: []});
                    }
                }
            }

            function setChildNodes(node, property){
                var nodes = {};
                var childNode;
                for (var id in ontology) {
                    if (ontology[id][property] == node.id) {
                        childNode = {id: id, text: getLabel(id, 'pt')};
                        nodes[id] = childNode;
                        if (!node['nodes']) {   node['nodes'] = []; }
                        node.nodes.push(childNode);
                    }
                }
                return nodes;
            }

            function defineTree(div, property){
                var nodes = {};
                var nodesbyLevel = {};
                var nodesTmp;
                var size = 0;

                for(var id in ontology){
                    if(ontology[id][property]){
                        setRootNodes(id, property);
                    }
                }

                for(var id in classes){
                    nodes[classes[id].id] = classes[id];
                    size++;
                }

                while(size>0){
                    for(var id in nodes){
                        nodesTmp = setChildNodes(nodes[id], property);
                        for (var nId in nodesTmp) { nodesbyLevel[nId] = nodesTmp[nId]; }
                    }

                    nodes = nodesbyLevel;
                    nodesbyLevel = {};
                    size = 0;

                    for(var id in nodes){ size++; }
                }

                $(div).treeview({
                    data: classes,
                    levels: 1,
                    onNodeSelected: function(event, node) {
                        ontEditor.find('\n'+node.id+':');
                    }
                });

                classes = [];
            };

            function loadTrees(){
                console.log('Load trees');
                $.get('/admin/ontologyAsJSON', function( data ) {
                    ontology = data;
                    defineTree('#classesTree', 'is_a');
                    defineTree('#propertiesTree', 'subPropertyOf');
                    defineTree('#individualsTree', 'type');
                });
            }

            var ontEditor = ace.edit("ontEditor");
            ontEditor.setTheme("ace/theme/chrome");
            ontEditor.getSession().setMode("ace/mode/yaml");
            ontEditor.getSession().setTabSize(2);
            ontEditor.setOption("showPrintMargin", false);
            document.getElementById('ontEditor').style.fontSize='14px';

            $( "#ontology_form" ).submit(function( event ) {
                //$('#ontology_form button').removeClass('btn-primary').addClass('btn-warning');
                $('#ontology_form button').button('loading');
                $.post(
                        $(this).attr('action'),
                        {'ontology':  ontEditor.getValue() },
                        function( data ) {
                            if(data){
                                $('#ontology_form button').button('reset');
                                $('#ontology_form button').removeClass('btn-warning').removeClass('btn-primary').addClass('btn-success');

                                setTimeout(function () {
                                    $('#ontology_form button').removeClass('btn-success').addClass('btn-primary');
                                }, 1000);

                                loadTrees();
                            }
                        }
                );
                event.preventDefault();
            });

            loadTrees();
        });
    </script>
</div>