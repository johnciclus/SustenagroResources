<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Scenario</title>
    <asset:stylesheet href="bootstrap-table.min.css"/>
    <asset:javascript src="bootstrap-table.min.js"/>
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
    $('#analysis_content table').bootstrapTable()

    $('.pager a').click(function(e){
        $('.nav-tabs a[href="'+$(this).attr('href')+'"]').tab('show');
        //console.log($('.pager a[href="'+$(this).attr('href')+'"]'));
        e.preventDefault();
    });
</script>
</body>
</html>