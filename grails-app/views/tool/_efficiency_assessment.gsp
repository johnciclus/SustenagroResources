<ul id="efficiency_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#cost_production_efficiency" aria-controls="cost_production_efficiency" role="tab" data-toggle="tab">1. Eficiência da produção / custo  </a></li>
    <li role="presentation">                <a href="#technologic_efficiency" aria-controls="technologic_efficiency" role="tab" data-toggle="tab">        2. Eficiência tecnológica </a></li>
</ul>

<div id="efficiency_content" class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="cost_production_efficiency">
        <g:render template="/widgets/indicatorList" model="${['subClasses': proSubClass,
                                                              'indsInSubClasses': productionFeatures,
                                                              'values': values,
                                                              'categories': proCategories]}" />
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#social_indicators">Anterior</a></li>
                    <li><a href="#technologic_efficiency">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>

    <div role="tabpanel" class="tab-pane" id="technologic_efficiency">

    <g:render template="/widgets/indicatorList" model="${[subClasses: tecSubClass,
                                                          indsInSubClasses: technologyFeatures,
                                                          categories: tecCategories,
                                                          values: values,
                                                          hasWeights: true,
                                                          weights: weights,
                                                          tecAlignment: tecAlignment,
                                                          tecOptimization: tecOptimization]}" />
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#cost_production_efficiency">Anterior</a></li>
                    <li>
                        <input type="submit" class="btn btn-primary" value="Avaliar" />
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</div>