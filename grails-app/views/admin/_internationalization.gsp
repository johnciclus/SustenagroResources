<div class="row">
    <div class="col-md-12">
        <form id="lang_form" action="/admin/langs" method="post" class="form-inline-block pull-right" role="form">
            <input name="lang" type="hidden" value="">
            <button type="submit" class="btn btn-primary" data-loading-text="<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span> Saving..."> Save </button>
       </form>
        <form id="reset_lang_form" action="/admin/langsReset" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restore </button>
        </form>
    </div>
</div>
<div class="row">
    <div id="langItems" class="col-md-3 col-sm-3">
        <p class="title">Languages</p>
        <ul class="list-group">
            <g:each in="${langNames}">
                <a href="#<%=it.key%>" class="list-group-item itemlink"><%=it.value.capitalize()%></a>
            </g:each>
        </ul>
    </div>

    <div class="col-md-9 col-sm-9">
        <pre id="langEditor" class="ace_editor editor ace-tm"></pre>
    </div>

    <script type="application/javascript">
        $( document ).ready(function() {
            var langEditor = ace.edit("langEditor");
            langEditor.setTheme("ace/theme/chrome");
            langEditor.getSession().setMode("ace/mode/properties");
            langEditor.setOption("showPrintMargin", false);
            langEditor.$blockScrolling = Infinity;

            document.getElementById('langEditor').style.fontSize = '14px';

            $("#lang_form").submit(function (event) {
                $.post(
                    $(this).attr('action'),
                    {   'id': $("input[name='lang']").attr('value'),
                        'code': langEditor.getValue()},
                    function (data) {
                        var res = $(data);
                        var status = res.find("entry[key='status']");
                        if (status.text() == 'ok') {
                            $('#lang_form button').removeClass('btn-primary').addClass('btn-success');
                            setTimeout(function () {
                                $('#lang_form button').removeClass('btn-success').addClass('btn-primary');
                            }, 1000);
                        }
                    }
                );
                event.preventDefault();
            });

            $('#langItems .itemlink').click(function(){
                var langName = $(this).attr('href').substring(1);
                console.log(langName)
                $.post( '/admin/getLang',
                        {'id': langName},
                        function (data) {
                            langEditor.setValue(data, -1);
                            $("input[name='lang']").attr('value', langName);
                        }
                );
                event.preventDefault();
            });
        });
    </script>
</div>