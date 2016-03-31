<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Analysis</title>
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