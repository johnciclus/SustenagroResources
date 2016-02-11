<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Home</title>
</head>
<body>
<div class="row main">
    <div>
        <form name="f" action="/login" method="POST">
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
                    <label for="username" class="col-sm-offset-2 col-sm-4 control-label">Username</label>
                    <div class="col-sm-4">
                        <input type="text" id="username" name="username" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-sm-offset-2 col-sm-4 control-label">Password</label>
                    <div class="col-sm-4">
                        <input type="password" id="password" name="password" class="form-control"/>
                    </div>
                </div>
                    <button type="submit" class="btn btn-default">Log in</button>
            </fieldset>
        </form>
        <form name="logout" action="/logout" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="submit" class="btn btn-default" value="logout">
        </form>
    </div>
</div>
</body>
</html>