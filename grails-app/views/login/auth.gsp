<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title>SustenAgro - login</title>
    <asset:javascript src="jquery.validate.min.js"/>
    <asset:javascript src="localization/messages_pt_BR.min.js"/>
</head>

<body>
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3 login">
            <div class="page-header">
                <h1>Bem-vindo!</h1>
            </div>

            <g:if test='${flash.message}'>
                <div class="alert alert-info" role="alert">
                <p>${flash.message}</p>
                </div>
            </g:if>

            <div>
                <p>Por favor insira os dados de usuário</p>
            </div>

            <form action='/login/authenticate' method='post' id='loginForm' class='form-horizontal'>
                <div class="form-group required">
                    <label for="username" class="col-sm-4 control-label">Nome de usuário:</label>
                    <div class="col-sm-8">
                        <input type='text' name='username' id='username' class='form-control' required/>
                    </div>
                </div>

                <div class="form-group required">
                    <label for='password' class='col-sm-4 control-label'>Senha:</label>
                    <div class="col-sm-8">
                        <input type="password" name='password' id='password' class="form-control" required/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <label>
                            <input type="checkbox" name='remember_me' id='remember_me' <g:if test='${hasCookie}'>checked="checked"</g:if>/>Permanecer conectado
                        </label>
                    </div>
                </div>

                <p id="remember_me_holder">

                </p>

                <div class="form-group col-sm-12 text-center">
                    <input type="submit" id="submit" value="Entrar" class="btn btn-primary"/>
                </div>
            </form>
        </div>
    </div>

<script type="text/javascript">
    $("form").each( function(index){
        $(this).validate({
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