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
            <li role="presentation" <g:if test="${report == null}"> class="active" </g:if>> <a href="#sustainability_assessment" aria-controls="sustainability_assessment" role="tab" data-toggle="tab">Avaliação da sustentabilidade</a></li>
            <li role="presentation"> <a href="#efficiency_assessment" aria-controls="efficiency_assessment" role="tab" data-toggle="tab">Avaliação da eficiência</a></li>
            <g:if test="${report != null}">
            <li role="presentation" class="active">                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">        Relatório</a>         </li>
            </g:if>
        </ul>

        <div id="assessment_content" class="tab-content">
            <div role="tabpanel" class="tab-pane ind-content <g:if test='${report == null}'>active</g:if>"  id="sustainability_assessment">
                <g:render template="sustainability_assessment"></g:render>
            </div>
            <div role="tabpanel" class="tab-pane ind-content"  id="efficiency_assessment">
                <g:render template="efficiency_assessment"></g:render>
            </div>
            <g:if test="${report != null}">
            <div role="tabpanel" class="tab-pane ind-content active" id="report">
                <g:render template="report"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#sustainability_assessment">Anterior</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            </g:if>
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