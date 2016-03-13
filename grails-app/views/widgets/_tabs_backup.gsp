<ul id="assessment_tab" class="nav nav-tabs">
    <li role="presentation" <g:if test="${report == null}"> class="active" </g:if>> <a href="#sustainability_assessment" aria-controls="sustainability_assessment" role="tab" data-toggle="tab">Avaliação da sustentabilidade</a></li>
    <li role="presentation"> <a href="#efficiency_assessment" aria-controls="efficiency_assessment" role="tab" data-toggle="tab">Avaliação da eficiência</a></li>
    <g:if test="${report != null}">
        <li role="presentation" class="active">                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">        Relatório</a>         </li>
        <li role="presentation"> <a href="#recomendation" aria-controls="recomendation" role="tab" data-toggle="tab">        Recomendação</a>         </li>
    </g:if>
</ul>

<form id="assessment_form" action="/tool/report" method="post" class="form-horizontal">
    <div id="assessment_content" class="tab-content">
        <div role="tabpanel" class="tab-pane ind-content <g:if test='${report == null}'>active</g:if>"  id="sustainability_assessment">
            <g:render template="/widgets/sustainability_assessment"></g:render>
        </div>
        <div role="tabpanel" class="tab-pane ind-content"  id="efficiency_assessment">
            <g:render template="/widgets/efficiency_assessment"></g:render>
        </div>
        <g:if test="${report != null}">
            <div role="tabpanel" class="tab-pane ind-content active" id="report">
                <g:render template="/widgets/report"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#sustainability_assessment">Anterior</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane ind-content" id="recomendation">
                <g:render template="/widgets/recomendation"></g:render>
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
</form>