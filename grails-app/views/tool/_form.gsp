<p>Unidade produtiva atual: <b>${production_unit_name}</b> </p>


<ul id="assessment_tab" class="nav nav-tabs">
    <li role="presentation" class="active"> <a href="#indicators">      1. Indicadores</a>      </li>
    <li role="presentation">                <a href="#recomendations">  2. Recomendações</a>    </li>
    <li role="presentation">                <a href="#graphics">        3. Gráficos</a>         </li>
</ul>

<div id="tabs_content" class="tab-content">
    <div role="tabpanel" class="tab-pane border-content active" id="indicators">
        <g:render template="indicators"></g:render>
    </div>
    <div role="tabpanel" class="tab-pane border-content active" id="recomendations">
        <p>Recomendations</p>
    </div>
    <div role="tabpanel" class="tab-pane border-content active" id="graphics">
        <p>Graphics</p>
    </div>
</div>

<script type="text/javascript">
    $('#assessment_tab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })
</script>

