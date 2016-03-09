<ul id="<%=id%>_tab" class="nav nav-tabs">
    <li role="presentation"  <g:if test="${report}"> class="active" </g:if>> <a href="#sustainability_assessment" aria-controls="sustainability_assessment" role="tab" data-toggle="tab">Avaliação da sustentabilidade</a></li>
    <li role="presentation"> <a href="#efficiency_assessment" aria-controls="efficiency_assessment" role="tab" data-toggle="tab">Avaliação da eficiência</a></li>
    <g:if test="${!report}">
        <li role="presentation" class="active">                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">        Relatório</a>         </li>
        <li role="presentation"> <a href="#recomendation" aria-controls="recomendation" role="tab" data-toggle="tab">        Recomendação</a>         </li>
    </g:if>
</ul>

<form id="<%=id%>_form" action="/tool/report" method="post" class="form-horizontal">
    <div id="assessment_content" class="tab-content">

    </div>
</form>