<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Assessment</title>
    <asset:stylesheet href="jquery.bootstrap-touchspin.min.css"/>
    <asset:javascript src="jquery.bootstrap-touchspin.min.js"/>
</head>
<body>
<div class="row main">
    <div id="content" class="col-sm-10 col-sm-offset-1 content">
        <p>Unidade produtiva atual: <b>${production_unit.name}</b> </p>

        <ul id="assessment_tab" class="nav nav-tabs">
            <li role="presentation" <g:if test="${report == null}"> class="active" </g:if>> <a href="#indicators" aria-controls="indicators" role="tab" data-toggle="tab"> Indicadores</a>      </li>
            <g:if test="${report != null}">
            <li role="presentation" class="active">                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">        Relatório</a>         </li>
            </g:if>
        </ul>

        <div id="assessment_content" class="tab-content">
            <div role="tabpanel" class="tab-pane ind-content <g:if test='${report == null}'>active</g:if>"  id="indicators">
                <g:render template="indicators"></g:render>
            </div>
            <g:if test="${report != null}">
            <div role="tabpanel" class="tab-pane ind-content active" id="report">
                <g:render template="report"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#indicators">Anterior</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            </g:if>
        </div>
    </div>
</div>
</body>
</html>