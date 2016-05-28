<!DOCTYPE html>
<html class="full" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="platform to support decision making in sustainable agriculture">
    <meta name="author" content="John Gararavito Suárez">
    <link rel="shortcut icon" href="/assets/favicon.ico" type="image/x-icon" />

    <title><g:layoutTitle default="SustenAgro"/></title>

    <asset:stylesheet href="normalize.css"/>
    <asset:stylesheet href="bootstrap-readable.min.css"/>
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
<div class="container page">
    <!-- Static navbar -->
    <div class="navbar navbar-default" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="/">
                    <asset:image src="logo.png" class="logo"/>
                </a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li <g:if test="${controllerName == null}"> class="active" </g:if> >
                        <a href="/">
                            <span class="glyphicon glyphicon-home" aria-hidden="true"></span> <g:message code="default.navbar.presentation" />
                        </a>
                    </li>
                    <li <g:if test="${controllerName == 'tool'}"> class="active" </g:if> >
                        <a href="/tool/evaluationObject">
                            <span class="glyphicon glyphicon-leaf" aria-hidden="true"></span> <g:message code="default.navbar.assessment" />
                        </a>
                    </li>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <li <g:if test="${controllerName == 'admin'}"> class="active" </g:if>>
                        <a target="_blank" href="/admin">
                            <span class="glyphicon glyphicon-edit" aria-hidden="true"></span> <g:message code="default.navbar.administration" />
                        </a>
                    </li>
                    </sec:ifAnyGranted>
                    <li <g:if test="${controllerName == 'contact'}"> class="active" </g:if> >
                        <a href="/home/contact">
                            <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span> <g:message code="default.navbar.contact" />
                        </a>
                    </li>
                </ul>
                    <sec:ifLoggedIn>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a id='langLabel' href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                    <g:if test="${session.lang=='pt'}">PT</g:if>
                                    <g:elseif test="${session.lang=='en'}">EN</g:elseif>
                                    <g:else>PT</g:else>
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a name="en" class="lang" href="/"><g:message code="default.lang.english" /></a></li>
                                    <li><a name="pt" class="lang" href="/"><g:message code="default.lang.portuguese" /></a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                    <span class="glyphicon glyphicon-user" aria-hidden="true"></span> <sec:username/> <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a href="/"><g:message code="default.greeting.welcome"/> <sec:username/></a></li>
                                    <li role="separator" class="divider"></li>
                                    <li><a id="logout" href="/"><g:message code="default.actions.exit"/></a></li>
                                </ul>
                            </li>
                        </ul>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                    <g:if test="${session.lang=='pt'}">PT</g:if>
                                    <g:elseif test="${session.lang=='en'}">EN</g:elseif>
                                    <g:else>PT</g:else>
                                    <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a name="en" class="lang" href="/"><g:message code="default.lang.english" /></a></li>
                                    <li><a name="pt" class="lang" href="/"><g:message code="default.lang.portuguese" /></a></li>
                                </ul>
                            </li>
                            <li class="active"><a href="/login/auth"><g:message code="default.actions.login"/></a></li>
                            <li><a href="/user/signup"><g:message code="default.actions.signup"/></a>

                        </ul>
                    </sec:ifNotLoggedIn>
            </div>
        </div>
    </div>

    <g:layoutBody/>

    <div class="row footer">
        <div class="col-sm-4">
            <asset:image src='embrapa-ma.jpg' class="img-centered" height="75" />
        </div>
        <div class="col-sm-4">
            <asset:image src='logo-icmc.jpg' class="img-centered" height="75" />
        </div>
        <div class="col-sm-4">
            <asset:image src='intermidia.png' class="img-centered" height="75" />
        </div>
    </div>

    <script type="application/javascript">
        $(document).ready(function() {
            $('#logout').click(function(e){
                $.post('/logout', function( data ) {
                    $(location).attr('href', '/');
                });
                return false;
            });

            $('.lang').click(function(e){
                $.post('/user/setLang', {lang: $(this).attr('name')}, function( data ) {
                    location.reload();
                });
                return false;
            });
        });
    </script>
</div>
</body>
</html>