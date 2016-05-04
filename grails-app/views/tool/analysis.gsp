<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Scenario</title>
    <asset:stylesheet href="bootstrap-table.min.css"/>
    <asset:javascript src="bootstrap-table.min.js"/>
    <asset:javascript src="d3.min.js"/>
    <asset:javascript src="md5.min.js"/>
    <script src='https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'></script>

</head>
<body>
<div class="row main">
    <div id="content" class="col-sm-10 col-sm-offset-1 content">
        <g:if test="${inputs}">
            <g:each in="${inputs}">
                <div class="section">
                    <g:render template="/widgets/${it.widget}" model="${it.attrs}" />
                </div>
            </g:each>
        </g:if>
    </div>
</div>
<script type="text/javascript">
    $('.pager a').click(function(e){
        var id = $(this).attr('href');
        id = id.substring(0, id.lastIndexOf('_tab_'));
        var main_id = $('#main_tabs li.active a').attr('href')
        main_id = main_id.substring(0, main_id.lastIndexOf('_tab_'));
        if(id != main_id){
            var parent_id = $('.nav-tabs a[href="'+$(this).attr('href')+'"]').parents('.tab-pane').attr('id')
            $('.nav-tabs a[href="'+'#'+parent_id+'"]').tab('show');
        }
        $('.nav-tabs a[href="'+$(this).attr('href')+'"]').tab('show');
        e.preventDefault();
    });

    $(".clear").click(function(){
        var name = $(this).attr('id').replace('-clear', '');
        $("input:radio").filter(function(index) {return $(this).attr('name')===name;})
                .removeAttr('checked');
    });
</script>
</body>
</html>