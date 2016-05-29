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