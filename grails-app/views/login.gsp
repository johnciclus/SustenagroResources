<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Home</title>
</head>
<body>
<div class="row main">
    <div class="col-sm-8 col-sm-offset-2">
        <form name="f" action="/login" method="POST" class="form-horizontal">
            <fieldset>
                <legend>Please Login</legend>
                <g:if test="${params.error}">
                    <div>
                        Invalid username and password.
                    </div>
                </g:if>
                <g:if test="${params.logout}">
                    <div>
                        You have been logged out.
                    </div>
                </g:if>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="form-group">
                    <label for="username" class="col-sm-4 control-label">Username</label>
                    <div class="col-sm-6">
                        <input type="text" id="username" name="username" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-sm-4 control-label">Password</label>
                    <div class="col-sm-6">
                        <input type="password" id="password" name="password" class="form-control"/>
                    </div>
                </div>
                <div class="form-group col-sm-12 text-center">
                    <button type="submit" class="btn btn-primary">Log in</button>
                </div>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>