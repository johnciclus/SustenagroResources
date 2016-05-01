<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Analysis</title>
    <asset:javascript src="jquery.validate.min.js"/>
    <asset:javascript src="localization/messages_pt_BR.min.js"/>
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

    var rules = {};

    $("input[type='radio']").each(function(){
        var e1Name = $(this).attr('name');
        var e2 = $("[name^='"+e1Name+"-']");
        if(e2.length){
            var e2Name = $(e2).attr('name');
            rules[e1Name] = {required: function(element) {
                var name = $(element).attr('name');
                return (($("[name^='"+name+"-']").val() != null) != $(element).is(':checked'));
            }};
            rules[e2Name] = {required: function(element) {
                var name = $(element).attr('name');
                var anotherName = name.substring(0, name.lastIndexOf('-'));
                return (($(element).val() != null) != $("[name='"+anotherName+"']").is(':checked'));
            }};
        }
    });

    $("form").each( function(index){
        $(this).validate({
            rules: rules,
            ignore: '',
            errorClass: "has-error",
            errorPlacement: function(error, element) {
                var form_group = $(element).parents('.form-group');
                form_group.append(error);
            },
            highlight: function(element, errorClass, validClass) {
                //console.log('highlight');
                var form_group = $(element).parents('.form-group');
                $(element).addClass(errorClass).removeClass(validClass);
                form_group.addClass(errorClass).removeClass(validClass);
            },
            unhighlight: function(element, errorClass, validClass) {
                //console.log('unhighlight');
                var form_group = $(element).parents('.form-group');
                $(element).removeClass(errorClass).addClass(validClass);
                form_group.removeClass(errorClass).addClass(validClass);
            }
        });
    });

</script>
</body>
</html>