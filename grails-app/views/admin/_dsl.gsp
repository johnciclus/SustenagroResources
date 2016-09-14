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
        <form id="dsl_form" action="/admin/dsl" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Save </button>
        </form>
        <form id="reset_dsl_form" action="/admin/dslReset" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restore </button>
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