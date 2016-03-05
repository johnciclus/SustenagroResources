<ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">  1. Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">            2. Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">                3. Sociais     </a></li>
</ul>

<div id="indicator_content" class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="environmental_indicators">
        <g:render template="/widgets/indicatorList" model="${['subClasses': indSubClass['http://semantic.icmc.usp.br/sustenagro#EnvironmentalIndicator'],
                                                              'indsInSubClasses': indicators['http://semantic.icmc.usp.br/sustenagro#EnvironmentalIndicator'],
                                                              'values': values,
                                                              'categories': indCategories]}" />
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#economic_indicators">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>

    <div role="tabpanel" class="tab-pane" id="economic_indicators">
        <g:render template="/widgets/indicatorList" model="${['subClasses': indSubClass['http://semantic.icmc.usp.br/sustenagro#EconomicIndicator'],
                                                              'indsInSubClasses': indicators['http://semantic.icmc.usp.br/sustenagro#EconomicIndicator'],
                                                              'values': values,
                                                              'categories': indCategories]}" />
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#environmental_indicators">Anterior</a></li>
                    <li><a href="#social_indicators">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>

    <div role="tabpanel" class="tab-pane" id="social_indicators">
        <g:render template="/widgets/indicatorList" model="${['subClasses': indSubClass['http://semantic.icmc.usp.br/sustenagro#SocialIndicator'],
                                                              'indsInSubClasses': indicators['http://semantic.icmc.usp.br/sustenagro#SocialIndicator'],
                                                              'values': values,
                                                              'categories': indCategories]}" />
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#economic_indicators">Anterior</a></li>
                    <li><a href="#cost_production_efficiency">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>
</div>

