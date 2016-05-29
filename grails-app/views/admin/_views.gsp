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
    <div class="col-md-3 col-sm-3">
        <p class="title">Views</p>
        <div id="viewsList" class="btn-group-vertical" role="group" aria-label="viewsList">
            <button type="button" class="btn btn-default">1</button>
            <button type="button" class="btn btn-default">2</button>
        </div>
        <ul class="list-group">
            <li class="list-group-item">Cras justo odio</li>
            <li class="list-group-item">Dapibus ac facilisis in</li>
            <li class="list-group-item">Morbi leo risus</li>
            <li class="list-group-item">Porta ac consectetur ac</li>
            <li class="list-group-item">Vestibulum at eros</li>
        </ul>
    </div>

    <div class="col-md-9 col-sm-9">
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