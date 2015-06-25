<p>Unidade produtiva atual: <b>${production_unit_name}</b> </p>

<div id="rootwizard">
    <div class="navbar">
        <div class="navbar-inner">
            <div class="container">
                <ul>
                    <li><a href="#tab_inds" data-toggle="tab">1. Indicadores</a></li>
                    <li><a href="#tab_rcms" data-toggle="tab">2. Recomendações</a></li>
                    <li><a href="#tab_grap" data-toggle="tab">3. Gráficos</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="tab-content">
        <div class="tab-pane" id="tab_inds">
            <g:render template="indicators"></g:render>
        </div>

        <div class="tab-pane" id="tab_rcms">
            <p>Recomendations</p>
        </div>
        
        <div class="tab-pane" id="tab_grap">
            <p>Graphics</p>
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