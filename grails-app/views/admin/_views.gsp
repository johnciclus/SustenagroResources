<div class="row">
    <div class="col-md-12">
        <form id="view_form" action="/admin/views" method="post" class="form-inline-block pull-right" role="form">
            <input name="view" type="hidden" value="">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Save </button>
        </form>
        <form id="reset_view_form" action="/admin/viewsReset" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restore </button>
        </form>
    </div>
</div>
<div class="row">
    <div id="viewItems" class="col-md-3 col-sm-3">
        <p class="title">Views</p>
        <ul class="list-group">
            <g:each in="${viewNames}">
                <a href="#<%=it%>" class="list-group-item itemlink"><%=it.capitalize()%></a>
            </g:each>
        </ul>
    </div>

    <div class="col-md-9 col-sm-9">
        <pre id="viewsEditor" class="ace_editor editor ace-tm"></pre>
    </div>

    <script type="application/javascript">
        $( document ).ready(function() {
            var viewsEditor = ace.edit("viewsEditor");
            viewsEditor.setTheme("ace/theme/chrome");
            viewsEditor.getSession().setMode("ace/mode/groovy");
            viewsEditor.setOption("showPrintMargin", false);
            viewsEditor.$blockScrolling = Infinity;

            document.getElementById('viewsEditor').style.fontSize = '14px';

            $("#view_form").submit(function (event) {
                $.post(
                        $(this).attr('action'),
                        {   'id': $("input[name='view']").attr('value'),
                            'code': viewsEditor.getValue()},
                        function (data) {
                            var res = $(data);
                            var status = res.find("entry[key='status']");
                            if (status.text() == 'ok') {
                                $('#view_form button').removeClass('btn-primary').addClass('btn-success');
                                setTimeout(function () {
                                    $('#view_form button').removeClass('btn-success').addClass('btn-primary');
                                }, 1000);
                            }
                        }
                );
                event.preventDefault();
            });

            $('#viewItems .itemlink').click(function(){
                var viewName = $(this).attr('href').substring(1);
                $.post( '/admin/getView',
                    {'id': viewName},
                    function (data) {
                        viewsEditor.setValue(data, -1);
                        $("input[name='view']").attr('value', viewName);
                    }
                );
                event.preventDefault();
            });
        });
    </script>
</div>