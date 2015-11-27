<ul id="efficiency_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#cost_production_efficiency" aria-controls="cost_production_efficiency" role="tab" data-toggle="tab">1. Eficiência da produção / custo  </a></li>
    <li role="presentation">                <a href="#technologic_efficiency" aria-controls="technologic_efficiency" role="tab" data-toggle="tab">        2. Eficiência tecnológica </a></li>
</ul>

<form id="efficiency_form" action="/tool/efficiency" method="post" class="form-horizontal">
    <input type="hidden" name="production_unit_id" value="${production_unit.id}">
    <div id="efficiency_content" class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="cost_production_efficiency">

            <g:each var="feature" in="${efficiencyFeatures}">
                <div class="form-group">
                    <label for="<%= feature.id %>" class="col-sm-6 control-label">${feature.label}</label>
                    <g:set var="wasFilled" value="${values[feature.id] != null}" />
                    <div class="col-sm-5">

                    </div>
                    <div class="col-sm-1">
                        <button id="<%= feature.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                    </div>
                </div>
            </g:each>

            <div>
                <nav>
                    <ul class="pager">
                        <li><a href="#technologic_efficiency">Próximo</a></li>
                    </ul>
                </nav>
            </div>
        </div>

        <div role="tabpanel" class="tab-pane" id="technologic_efficiency">

            <div>
                <nav>
                    <ul class="pager">
                        <li><a href="#cost_production_efficiency">Anterior</a></li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</form>