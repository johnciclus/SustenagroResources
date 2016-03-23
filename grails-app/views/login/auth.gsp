<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title><g:message code='springSecurity.login.title'/></title>
</head>

<body>
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3 login">
            <div class="page-header">
                <h1>Bem-vindo!</h1>
            </div>
            <div>
                <p>Por favor insira os dados de usuário</p>
            </div>

            <g:if test='${flash.message}'>
                <div>${flash.message}</div>
            </g:if>

            <form action='/login/authenticate' method='post' id='loginForm' class='form-horizontal'>
                <div class="form-group">
                    <label for="username" class="col-sm-4 control-label">Nome de usuário:</label>
                    <div class="col-sm-8">
                        <input type='text' name='username' id='username' class='form-control'/>
                    </div>
                </div>

                <div class="form-group">
                    <label for='password' class='col-sm-4 control-label'>Senha:</label>
                    <div class="col-sm-8">
                        <input type="password" name='password' id='password' class="form-control"/>
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
                    <input type="submit" id="submit" value="${message(code: 'springSecurity.login.button')}" class="btn btn-primary"/>
                </div>
            </form>
        </div>
    </div>
</body>
</html>