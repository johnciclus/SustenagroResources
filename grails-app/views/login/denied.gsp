<html>

<head>
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title><g:message code='springSecurity.denied.title' /></title>
</head>

<body>
<div class="body">
    <div class="jumbotron">
        <h2>Acesso negado</h2>
        <p><g:message code='springSecurity.denied.message' /></p>
        <p><a class="btn btn-primary btn-lg" href="/login" role="button">Inicie sessão</a></p>
    </div>
</div>
</body>

</html>