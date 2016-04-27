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

    $.validator.addMethod("weightRequired", function(value, element, arg){
        console.log('weightRequired');
        return arg != value;
    }, "Value must not equal arg.");

    var rules = {};

    $("input[type='radio']").each(function(){
        var name = $(this).attr('name');
        if($("[name^='"+name+"-']").length){
            rules[name] = {};           //dual dependence
        }
    });

    for(var k in rules){
        //console.log(k);
        //console.log(rules[k]);
    }

    $("form").each( function(index){
        $(this).validate({
            rules: rules,
            ignore: '',
            errorClass: "has-error",
            errorPlacement: function(error, element) {
                var form_group = $(element).parents('.form-group');
                form_group.children(':last-child').append(error);
            },
            highlight: function(element, errorClass, validClass) {
                //console.log('highlight');
                var form_group = $(element).parents('.form-group');
                form_group.addClass(errorClass).removeClass(validClass);
            },
            unhighlight: function(element, errorClass, validClass) {
                //console.log('unhighlight');
                var form_group = $(element).parents('.form-group');
                form_group.removeClass(errorClass).addClass(validClass);
            }
        });
    });

</script>
</body>
</html>