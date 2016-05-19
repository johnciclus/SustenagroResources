<!DOCTYPE html>
<html class="ide" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Decisioner">
    <meta name="author" content="John Gararavito Suárez">

    <title><g:layoutTitle default="Decisioner"/></title>

    <asset:stylesheet href="normalize.css"/>
    <asset:stylesheet href="bootstrap-paper.min.css"/>
    <asset:stylesheet href="style.css"/>
    <asset:javascript src="jquery-1.11.1.min.js"/>
    <asset:javascript src="bootstrap.min.js"/>

    <g:layoutHead/>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

</head>
<body>
<div class="page">

    <g:layoutBody/>

    <script type="application/javascript">
        $(document).ready(function() {
            $('#logout').click(function (e) {
                $.post('/logout', function (data) {
                    $(location).attr('href', '/');
                });
                return false;
            });
        });
    </script>
</div>
</body>
</html>