<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Assessment</title>
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

    $('.pager a').click(function(e){
        if($(this).attr('href')=='#cost_production_efficiency'){
            $(".nav-tabs a[href='#efficiency_assessment']").tab('show')
        }
        else if($(this).attr('href')=='#social_indicators'){
            $(".nav-tabs a[href='#sustainability_assessment']").tab('show')
        }
        $('.nav-tabs a[href="'+$(this).attr('href')+'"]').tab('show');
        //console.log($('.pager a[href="'+$(this).attr('href')+'"]'));
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