<!DOCTYPE html>
<html class="full" lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Platform to support decision making on sustainability in agriculture">
        <meta name="author" content="John Gararavito Suárez">
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        
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
                        <g:img dir='images' file='logo.png' class="logo" />
                    </a>
                  </div>
                  <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                      <li <g:if test="${controllerName == null}"> class="active" </g:if> ><a href="/">Inicio</a></li>
                      <li <g:if test="${controllerName == 'tool'}"> class="active" </g:if> ><a href="/tool">Ferramenta</a></li>
                      
                      <!--<li <g:if test="${controllerName == 'tool'}"> class="active" </g:if> class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Ferramenta <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                          <li <g:if test="${controllerName == 'tool' && actionName == 'index'}"> class="active" </g:if> ><a href="/sustenagro/tool">Apresentação</a></li>
                          <li class="divider"></li>
                          <li <g:if test="${controllerName == 'tool' && actionName == 'location'}">                 class="active" </g:if> ><a href="/sustenagro/tool/location">                1. Localização</a></li>
                          <li <g:if test="${controllerName == 'tool' && actionName == 'crop'}">                         class="active" </g:if> ><a href="/sustenagro/tool/crop">                        2. Culturas</a></li>
                          <li <g:if test="${controllerName == 'tool' && actionName == 'technology'}">           class="active" </g:if> ><a href="/sustenagro/tool/technology">          3. Tecnologias</a></li>
                          <li <g:if test="${controllerName == 'tool' && actionName == 'characterization'}"> class="active" </g:if> ><a href="/sustenagro/tool/characterization">4. Caracterização</a></li>
                          <li <g:if test="${controllerName == 'tool' && actionName == 'indicators'}">           class="active" </g:if> ><a href="/sustenagro/tool/indicators">          5. Indicadores</a></li>
                          <li <g:if test="${controllerName == 'tool' && actionName == 'recomendations'}">   class="active" </g:if> ><a href="/sustenagro/tool/recomendations">  6. Recomendações</a></li>
                        </ul>
                      </li>-->
                      
                      <li <g:if test="${controllerName == 'contact'}"> class="active" </g:if> ><a href="/contact">Contato</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                      <li class="active"><a href="./">Inicie sessão</a></li>
                      <li><a href="../navbar-static-top/">Cadastre-se</a></li>
                    </ul>
                  </div><!--/.nav-collapse -->
                </div><!--/.container-fluid -->
            </div>
            
            <g:layoutBody/>
            
            <div class="row footer">
                <div class="col-sm-4">
                    <g:img dir='images' file='embrapa-ma.jpg' class="img-centered" height="75" />
                </div>
                <div class="col-sm-4">
                    <g:img dir='images' file='logo-icmc.jpg' class="img-centered" height="75" />
                </div>
                <div class="col-sm-4">
                    <g:img dir='images' file='intermidia.png' class="img-centered" height="75" />
                </div>
            </div>
        </div> <!-- /container -->
    </body>
</html>