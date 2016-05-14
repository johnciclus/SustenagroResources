<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>SustenAgro - New User</title>
    <asset:javascript src="jquery.validate.min.js"/>
    <asset:javascript src="localization/messages_pt_BR.min.js"/>
</head>

<body>
<div class="row">
    <div class="col-sm-8 col-sm-offset-2 login">
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
    $(document).ready(function() {
        jQuery.validator.addMethod("noSpace", function (value, element) {
            return value.indexOf(" ") < 0 && value != "";
        }, "Space are not allowed");

        $("#signUpForm").validate({
            errorClass: "has-error",
            rules: {
                'http://purl.org/biodiv/semanticUI#hasEmail': {
                    email: true,
                    remote: "emailAvailability"
                },
                'http://purl.org/biodiv/semanticUI#hasUsername': {
                    noSpace: true,
                    remote: "usernameAvailability"
                },
                'http://purl.org/biodiv/semanticUI#hasPassword': {
                    noSpace: true,
                    minlength: 5
                },
                'http://purl.org/biodiv/semanticUI#hasPassword-confirm': {
                    noSpace: true,
                    minlength: 5,
                    equalTo: "input[name='http://purl.org/biodiv/semanticUI#hasPassword']"
                }
            },
            messages: {
                'http://purl.org/biodiv/semanticUI#username': {
                    noSpace: "Espaço em branco não é permitido.",
                    remote: jQuery.validator.format("{0} já está atribuído no sistema.")
                }
            },
            errorPlacement: function (error, element) {
                var form_group = $(element).parents('.form-group');
                form_group.children(':last-child').append(error);
            },
            highlight: function (element, errorClass, validClass) {
                var form_group = $(element).parents('.form-group');
                form_group.addClass(errorClass).removeClass(validClass);
            },
            unhighlight: function (element, errorClass, validClass) {
                var form_group = $(element).parents('.form-group');
                form_group.removeClass(errorClass).addClass(validClass);
            }
        });
    });
</script>
</body>
</html>