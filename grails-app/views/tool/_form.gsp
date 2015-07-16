<p>Unidade produtiva atual: <b>${production_unit_name}</b> </p>

<ul id="assessment_tab" class="nav nav-tabs">
    <li role="presentation" class="active"> <a href="#indicators" aria-controls="indicators" role="tab" data-toggle="tab">          1. Indicadores</a>      </li>
    <li role="presentation">                <a href="#recomendations" aria-controls="recomendations" role="tab" data-toggle="tab">  2. Recomendações</a>    </li>
    <li role="presentation">                <a href="#graphics" aria-controls="graphics" role="tab" data-toggle="tab">              3. Gráficos</a>         </li>
</ul>

<div id="assessment_content" class="tab-content">
    <div role="tabpanel" class="tab-pane ind-content active" id="indicators">
        <g:render template="indicators"></g:render>
    </div>
    <div role="tabpanel" class="tab-pane ind-content" id="recomendations">
        <p>Recomendations</p>
    </div>
    <div role="tabpanel" class="tab-pane ind-content" id="graphics">
        <p>Graphics</p>
    </div>
</div>



