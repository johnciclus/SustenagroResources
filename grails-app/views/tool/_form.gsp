<p>Unidade produtiva atual: <b>production_unit.name</b> </p>

<div id="rootwizard">
    <div class="navbar">
        <div class="navbar-inner">
            <div class="container">
                <ul class="nav nav-pills">
                    <li><a href="#characterization" data-toggle="tab">1. Caracterização</a></li>
                    <li><a href="#indicators" data-toggle="tab">2. Indicadores</a></li>
                    <li><a href="#recomendations" data-toggle="tab">3. Recomendações</a></li>
                    <li><a href="#graphics" data-toggle="tab">4. Gráficos</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="tab-content">
        <div class="tab-pane" id="characterization">
            <g:render template="characterization" />
        </div>
        <div class="tab-pane" id="indicators">
            <g:render template="indicators" />
        </div>
        <div class="tab-pane" id="recomendations">
            <g:render template="recomendations" />
        </div>
        <div class="tab-pane" id="graphics">
            <g:render template="graphics" />
        </div>

        <ul class="pager wizard">
            <li class="previous first" style="display:none;"><a href="#">Primeiro</a></li>
            <li class="previous"><a href="#">Anterior</a></li>
            <li class="next last" style="display:none;"><a href="#">Último</a></li>
            <li class="next"><a href="#">Próximo</a></li>
        </ul>
    </div>
</div>
<script type="application/javascript">
    $(document).ready(function() {
        $('#rootwizard').bootstrapWizard();
    });
</script>