<div class="row">
    <div class="col-md-12">
        <form id="dsl_form" action="/admin/dsls" method="post" class="form-inline-block pull-right" role="form">
            <input name="dsl" type="hidden" value="">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Save </button>
        </form>
        <form id="reset_dsl_form" action="/admin/dslsReset" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restore </button>
        </form>
    </div>
</div>
<div class="row">
    <div id="dslItems" class="col-md-3 col-sm-3">
        <p class="title">DSLs</p>
        <ul class="list-group">
            <g:each in="${dslNames}">
                <a href="#<%=it%>" class="list-group-item itemlink"><%=it.capitalize()%></a>
            </g:each>
        </ul>
    </div>

    <div class="col-md-9 col-sm-9">
        <pre id="dslsEditor" class="ace_editor editor ace-tm"></pre>
    </div>

    <script type="application/javascript">
        $( document ).ready(function() {
            var dslsEditor = ace.edit("dslsEditor");
            dslsEditor.setTheme("ace/theme/chrome");
            dslsEditor.getSession().setMode("ace/mode/groovy");
            dslsEditor.setOption("showPrintMargin", false);
            dslsEditor.$blockScrolling = Infinity;

            document.getElementById('dslsEditor').style.fontSize = '14px';

            $("#dsl_form").submit(function (event) {
                $.post(
                        $(this).attr('action'),
                        {   'id': $("input[name='dsl']").attr('value'),
                            'code': dslsEditor.getValue()},
                        function (data) {
                            var res = $(data);
                            var status = res.find("entry[key='status']");
                            if (status.text() == 'ok') {
                                $('#dsl_form button').removeClass('btn-primary').addClass('btn-success');
                                setTimeout(function () {
                                    $('#dsl_form button').removeClass('btn-success').addClass('btn-primary');
                                }, 1000);
                            }
                        }
                );
                event.preventDefault();
            });

            $('#dslItems .itemlink').click(function(){
                var dslName = $(this).attr('href').substring(1);
                $.post( '/admin/getDsl',
                        {'id': dslName},
                        function (data) {
                            dslsEditor.setValue(data, -1);
                            $("input[name='dsl']").attr('value', dslName);
                        }
                );
                event.preventDefault();
            });
        });
    </script>
</div>