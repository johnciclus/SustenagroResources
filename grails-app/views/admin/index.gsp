<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="decisioner"/>
		<title>SustenAgro - Admin</title>
		<asset:javascript src="ace-min-noconflict/ace.js"/>
        <asset:javascript src="ace-min-noconflict/ext-language_tools.js"/>
        <asset:javascript src="bootstrap-table-old.min.js"/>
        <asset:javascript src="bootstrap-treeview.min.js"/>
        <asset:stylesheet href="bootstrap-table.min.css"/>
        <asset:stylesheet href="bootstrap-treeview.min.css"/>
	</head>
	<body>
        <div class="row main">
			<div id="content" class="col-md-12 content">
				<ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#ontology">Ontology Editor</a></li>
                    <li><a data-toggle="tab" href="#dsl">DSL Main</a></li>
                    <li><a data-toggle="tab" href="#gui">DSL Graphical User Interface</a></li>
                    <li><a data-toggle="tab" href="#views">Views</a></li>
				</ul>
				<div class="tab-content">
                    <div id="ontology" class="tab-pane fade in active">
                        <div class="row">
                            <div class="col-md-12">
                                <form id="ontology_form" action="/admin/ontology" method="post" class="form-inline-block pull-right" role="form">
                                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
                                </form>
                                <form id="reset_ontology_form" action="/admin/ontologyReset" method="post" class="form-inline-block pull-right" role="form">
                                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
                                </form>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4 col-sm-4">
                                <p class="title">Classes</p>
                                <div id="classesTree">

                                </div>
                                <p class="title">Propiedades</p>
                                <div id="propertiesTree">

                                </div>
                                <p class="title">Individuos</p>
                                <div id="individualsTree">

                                </div>
                            </div>
                            <div class="col-md-8 col-sm-8">
                                <pre id="ontEditor" class="ace_editor editor ace-tm">${ontology}</pre>
                            </div>
                            <script type="application/javascript">
                                var ontology;
                                var classes = [];

                                function getLabel(id, lang){
                                    if(ontology[id]){
                                        var label = ''
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
                                            ontEditor.find(node.id+':');
                                        }
                                        });

                                    classes = [];
                                };

                                var ontEditor = ace.edit("ontEditor");
                                ontEditor.setTheme("ace/theme/chrome");
                                ontEditor.getSession().setMode("ace/mode/yaml");
                                ontEditor.getSession().setTabSize(2);
                                ontEditor.setOption("showPrintMargin", false);
                                document.getElementById('ontEditor').style.fontSize='14px';

                                $( "#ontology_form" ).submit(function( event ) {
                                    $.post(
                                            $(this).attr('action'),
                                            {'ontology':  ontEditor.getValue() },
                                            function( data ) {
                                                if(data=='ok'){
                                                    function resetButton(){
                                                        $('#ontology_form button').removeClass('btn-success').addClass('btn-primary');
                                                    }
                                                    $('#ontology_form button').removeClass('btn-primary').addClass('btn-success');
                                                    setTimeout(resetButton, 1000);
                                                }
                                            }
                                    );
                                    event.preventDefault();
                                });

                                $.get('/admin/ontologyAsJSON', function( data ) {
                                    ontology = data;
                                    defineTree('#classesTree', 'is_a');
                                    defineTree('#propertiesTree', 'subPropertyOf');
                                    defineTree('#individualsTree', 'type');
                                });

                            </script>
                        </div>
                    </div>
                    <div id="dsl" class="tab-pane fade ">
						<div class="row">
							<div class="col-md-12">
								<form id="dsl_form" action="/admin/dsl" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
								</form>
								<form id="reset_dsl_form" action="/admin/dslReset" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
								</form>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<pre id="dslEditor" class="ace_editor editor ace-tm">${dsl_code}</pre>
							</div>

							<script type="application/javascript">
                                var Range = ace.require("ace/range").Range;
                                var langTools = ace.require("ace/ext/language_tools");
                                var dslEditor = ace.edit("dslEditor");
                                var session = dslEditor.getSession();

                                dslEditor.setTheme("ace/theme/chrome");
                                dslEditor.setOption("showPrintMargin", false);
                                dslEditor.setOptions({
                                    enableBasicAutocompletion: true,
                                    enableSnippets: true
                                });
                                session.setMode("ace/mode/groovy");

								document.getElementById('dslEditor').style.fontSize='14px';

                                var dslCompleter = {
                                    getCompletions: function(editor, session, pos, prefix, callback) {
                                        $.get('/admin/autoComplete?word='+prefix, function( respond ) {
                                            callback(null, respond);
                                        });

                                        //callback(null, [
                                        //        {'name': 'title', 'value': 'title', 'score': 400, 'meta': 'commands'},
                                        //        {'name': 'description', 'value': 'Description', 'score': 400, 'meta': 'commands'}]);
                                    }
                                }

                                langTools.addCompleter(dslCompleter);

								$( "#dsl_form" ).submit(function( event ) {
                                    var markers = session.getMarkers(false);
                                    for( var i in markers){
                                        if(markers[i].clazz=='ace_error-line')
                                            session.removeMarker(markers[i].id);
                                    }

									$.post(
                                        $(this).attr('action'),
                                        {'code':  dslEditor.getValue() },
                                        function( data ) {
                                            var res = $(data);
                                            var status = res.find("entry[key='status']");

                                            if(status.text() =='ok'){
                                                $('#dsl_form button').removeClass('btn-primary').addClass('btn-success');
                                                setTimeout(function(){
                                                    $('#dsl_form button').removeClass('btn-success').addClass('btn-primary');
                                                }, 1000);
                                            }
                                            else if(status.text() =='error'){
                                                $('#dsl_form button').removeClass('btn-primary').addClass('btn-danger');
                                                setTimeout(function(){
                                                    $('#dsl_form button').removeClass('btn-danger').addClass('btn-primary');
                                                }, 1000);
                                                var line = Number(res.find("entry[key='line']").text())-1;
                                                session.addMarker(new Range(line, 0, line, 200), "ace_error-line", "fullLine");
                                            }
                                        }
									);
									event.preventDefault();
								});
							</script>
						</div>
					</div>
                    <div id="gui" class="tab-pane fade">
                        <div class="row">
                            <div class="col-md-12">
                                <form id="gui_form" action="/admin/gui" method="post" class="form-inline-block pull-right" role="form">
                                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
                                </form>
                                <form id="reset_gui_form" action="/admin/guiReset" method="post" class="form-inline-block pull-right" role="form">
                                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
                                </form>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <pre id="guiEditor" class="ace_editor editor ace-tm">${gui_code}</pre>
                            </div>

                            <script type="application/javascript">
                                var Range = ace.require("ace/range").Range;
                                var langTools = ace.require("ace/ext/language_tools");
                                var guiEditor = ace.edit("guiEditor");
                                var session = guiEditor.getSession();

                                guiEditor.setTheme("ace/theme/chrome");
                                guiEditor.setOption("showPrintMargin", false);
                                guiEditor.setOptions({
                                    enableBasicAutocompletion: true,
                                    enableSnippets: true
                                });
                                session.setMode("ace/mode/groovy");

                                document.getElementById('guiEditor').style.fontSize='14px';

                                var guiCompleter = {
                                    getCompletions: function(editor, session, pos, prefix, callback) {
                                        $.get('/admin/autoComplete?word='+prefix, function( respond ) {
                                            callback(null, respond);
                                        });

                                        //callback(null, [
                                        //        {'name': 'title', 'value': 'title', 'score': 400, 'meta': 'commands'},
                                        //        {'name': 'description', 'value': 'Description', 'score': 400, 'meta': 'commands'}]);
                                    }
                                }

                                langTools.addCompleter(guiCompleter);

                                $( "#gui_form" ).submit(function( event ) {
                                    var markers = session.getMarkers(false);
                                    for( var i in markers){
                                        if(markers[i].clazz=='ace_error-line')
                                            session.removeMarker(markers[i].id);
                                    }

                                    $.post(
                                            $(this).attr('action'),
                                            {'code':  guiEditor.getValue() },
                                            function( data ) {
                                                var res = $(data);
                                                var status = res.find("entry[key='status']");

                                                if(status.text() =='ok'){
                                                    $('#gui_form button').removeClass('btn-primary').addClass('btn-success');
                                                    setTimeout(function(){
                                                        $('#gui_form button').removeClass('btn-success').addClass('btn-primary');
                                                    }, 1000);
                                                }
                                                else if(status.text() =='error'){
                                                    $('#gui_form button').removeClass('btn-primary').addClass('btn-danger');
                                                    setTimeout(function(){
                                                        $('#gui_form button').removeClass('btn-danger').addClass('btn-primary');
                                                    }, 1000);
                                                    var line = Number(res.find("entry[key='line']").text())-1;
                                                    session.addMarker(new Range(line, 0, line, 200), "ace_error-line", "fullLine");
                                                }
                                            }
                                    );
                                    event.preventDefault();
                                });
                            </script>
                        </div>
                    </div>
                    <div id="views" class="tab-pane fade">
                        <div class="row">
                            <div class="col-md-12">
                                <form id="view_form" action="/admin/views" method="post" class="form-inline-block pull-right" role="form">
                                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
                                </form>
                                <form id="reset_view_form" action="/admin/viewsReset" method="post" class="form-inline-block pull-right" role="form">
                                    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
                                </form>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <pre id="viewsEditor" class="ace_editor editor ace-tm">${views}</pre>
                            </div>

                            <script type="application/javascript">
                                var viewsEditor = ace.edit("viewsEditor");
                                viewsEditor.setTheme("ace/theme/chrome");
                                viewsEditor.getSession().setMode("ace/mode/groovy");
                                viewsEditor.setOption("showPrintMargin", false);
                                document.getElementById('viewsEditor').style.fontSize='14px';

                                $( "#view_form" ).submit(function( event ) {
                                    $.post(
                                            $(this).attr('action'),
                                            {'views':  viewsEditor.getValue() },
                                            function( data ) {
                                                var res = $(data);
                                                var status = res.find("entry[key='status']");
                                                if(status.text() =='ok'){
                                                    $('#view_form button').removeClass('btn-primary').addClass('btn-success');
                                                    setTimeout(function(){
                                                        $('#view_form button').removeClass('btn-success').addClass('btn-primary');
                                                    }, 1000);
                                                }
                                            }
                                    );
                                    event.preventDefault();
                                });
                            </script>
                        </div>
                    </div>
                    <div id="indicators" class="tab-pane fade">
						<div class="row">
                            <div class="col-md-3 col-sm-4 col-xs-6">
                                <g:each var="el" in="${dimensions}">
                                    <div class="panel-group" id="accordion-${el.id}">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                <h4 class="panel-title">
                                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse-${el.id}">${el.label}</a>
                                                </h4>
                                            </div>
                                            <div id="collapse-${el.id}" class="panel-collapse collapse in">
                                                <div class="panel-body">
                                                    <div class="list-group">
                                                    <g:each status="i" var="row" in="${indicators}">
                                                        <g:if test="${ row.dimension == el.id}">
                                                            <button id="${row.id}" type="button" class="list-group-item indicator">${row['label@pt']}</button>
                                                        </g:if>
                                                    </g:each>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                            <div id="indicator_editor" class="col-md-9 col-sm-8 col-xs-6">

                            </div>
                            <script type="application/javascript">

                                $('#indicators .indicator').click(function(){
                                    var id = $(this).attr('id');
                                    $('#indicators .indicator.active').removeClass('active');
                                    $(this).addClass('active');

                                    $.post('/admin/indicatorForm',
                                        {'id':  id},
                                        function(data){
                                            $("#indicator_editor").html(data)
                                            $("#valuetype-table").bootstrapTable()
                                            $('#indicator_form').submit(function(event){
                                                $.post(
                                                    $(this).attr('action'),
                                                    $(this).serializeArray(),
                                                    function( data ) {
                                                        if(data.result == 'ok'){

                                                        }
                                                    }
                                                );
                                                event.preventDefault();
                                            });
                                            $('#indicator_editor .select-dimension').change( function(){
                                                var id = $(this).attr('id');
                                                var dim = $(this).val();
                                                id = id.substring(0, id.indexOf('_'));
                                                var attribute = $('#'+id+'_attribute');
                                                attribute.empty();

                                                $.post('/admin/attributes',
                                                        {'dimension':  dim },
                                                        function(data){
                                                            var res = $(data);
                                                            $.each(res.find("entry[key='attribute']"), function(){
                                                                attribute.append($('<option></option>').attr('value', $(this).text()).text($(this).text()));
                                                            });
                                                        }
                                                );
                                            });
                                        }
                                    );
                                });
                            </script>
						</div>
					</div>
					<div id="widgets" class="tab-pane fade"></div>
				</div>
			</div>
		</div>
	</body>
</html>