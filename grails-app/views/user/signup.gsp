<html>
<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title><g:message code='springSecurity.login.title'/></title>
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

        <form action='/user/createUser' method='post' id='signUpForm' class='form-horizontal'>

            <div class="form-group">
                <label for="surname" class="col-sm-4 control-label">Sobrenome:</label>
                <div class="col-sm-8">
                    <input type='text' name='surname' id='surname' class='form-control'/>
                </div>
            </div>

            <div class="form-group">
                <label for="email" class="col-sm-4 control-label">Email:</label>
                <div class="col-sm-8">
                    <input type='email' name='email' id='email' class='form-control'/>
                </div>
            </div>

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
                <label for='passwordrepeat' class='col-sm-4 control-label'>Repita a senha:</label>
                <div class="col-sm-8">
                    <input type="password" name='passwordrepeat' id='passwordrepeat' class="form-control"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-8">
                    <label>
                        <input type="checkbox" name='termsofuse' id='termsofuse' />Você concorda com os nossos termos de uso
                    </label>
                </div>
            </div>

            <div class="form-group col-sm-12 text-center">
                <input type="submit" id="submit" value="Cadastrar" class="btn btn-primary"/>
            </div>
        </form>
    </div>
</div>
</body>
</html>