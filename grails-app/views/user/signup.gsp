<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>SustenAgro - New User</title>
    <asset:javascript src="jquery.validate.min.js"/>
    <asset:javascript src="localization/messages_pt_BR.min.js"/>
</head>

<body>
<div class="row">
    <div class="col-sm-6 col-sm-offset-3 login">
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
    jQuery.validator.addMethod("noSpace", function(value, element) {
        return value.indexOf(" ") < 0 && value != "";
    }, "Space are not allowed");

    $("#signUpForm").validate({
        errorClass: "has-error",
        rules: {
            'http://purl.org/biodiv/semanticUI#hasEmail': {
                email: true
            },
            'http://purl.org/biodiv/semanticUI#hasUserName': {
                noSpace: true,
                remote: "username_availability"
            },
            'http://purl.org/biodiv/semanticUI#hasPassword': {
                noSpace: true,
                minLength: 5
            },
            'http://purl.org/biodiv/semanticUI#hasPassword-confirm': {
                noSpace: true,
                minLength: 5,
                equalTo: "input[name='http://purl.org/biodiv/semanticUI#hasPassword']"
            }
        },
        errorPlacement: function(error, element) {
            var form_group = $(element).parents('.form-group');
            form_group.children(':last-child').append(error);
        },
        highlight: function(element, errorClass, validClass) {
            var form_group = $(element).parents('.form-group');
            form_group.addClass(errorClass).removeClass(validClass);
        },
        unhighlight: function(element, errorClass, validClass) {
            var form_group = $(element).parents('.form-group');
            form_group.removeClass(errorClass).addClass(validClass);
        }
    })
</script>
</body>
</html>