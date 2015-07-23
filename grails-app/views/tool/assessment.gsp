<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Assessment</title>
    <asset:stylesheet href="jquery.bootstrap-touchspin.min.css"/>
    <asset:javascript src="jquery.bootstrap-touchspin.min.js"/>
    <asset:javascript src="http://mbostock.github.com/d3/d3.js"/>
</head>
<body>
<div class="row main">
    <div id="content" class="col-sm-10 col-sm-offset-1 content">
        <p>Unidade produtiva atual: <b>${production_unit_name}</b> </p>

        <ul id="assessment_tab" class="nav nav-tabs">
            <li role="presentation" class="active"> <a href="#indicators" aria-controls="indicators" role="tab" data-toggle="tab">          1. Indicadores</a>      </li>
            <li role="presentation" disabled>                <a href="#recomendations" aria-controls="recomendations" role="tab" data-toggle="tab">  2. Recomendações</a>    </li>
            <li role="presentation" disabled>                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">              3. Relatório</a>         </li>
        </ul>

        <div id="assessment_content" class="tab-content">
            <div role="tabpanel" class="tab-pane ind-content active" id="indicators">
                <g:render template="indicators"></g:render>
            </div>
            <div role="tabpanel" class="tab-pane ind-content" id="recomendations">
                <p>Recomendations</p>
                <g:render template="recomendations"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#indicators">Anterior</a></li>
                            <li><a href="#report">Próximo</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane ind-content" id="report">
                <p>Relatório</p>
                <g:render template="report"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#recomendations">Anterior</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $('.pager a').click(function(){
        $('.nav-tabs a[href="'+$(this).attr('href')+'"]').tab('show');
    });
</script>
</body>
</html>