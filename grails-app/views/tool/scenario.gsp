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
                <g:render template="/widgets/${it.widget}" model="${it.args}" />
            </g:each>
        </g:if>
    </div>
</div>
<script type="text/javascript">
    $('#report table').bootstrapTable()

</script>
</body>
</html>