<ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">  1. Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">            2. Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">                3. Sociais     </a></li>
</ul>

<div id="indicator_content" class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="environmental_indicators">
        <g:each var="subClass" in="${indSubClass.environmental}">
            <fieldset>
                <legend><h5>${subClass.value[0].label}</h5></legend>
                <g:each var="indicator" in="${indicators.environmental}">
                    <g:if test="${subClass.key == indicator.subClass}">
                        <div class="form-group">
                            <label for="<%= indicator.id %>" class="col-sm-6 control-label">${indicator.label}</label>
                            <g:set var="wasFilled" value="${values[indicator.id] != null}" />
                            <div class="col-sm-5">
                                <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                                    <g:each var="category" in="${indCategories[indicator.category]}">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="<%= indicator.id %>" value="<%= category.id %>" <g:if test="${ wasFilled && values[indicator.id] == category.id}"> checked </g:if>> <%= category.label %>
                                            </label>
                                        </div>
                                    </g:each>
                                </g:if>
                                <g:elseif test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Real' }">
                                    <input type="text" class="form-control" name="${indicator.id}" value="${values[indicator.id]}">
                                </g:elseif>
                            </div>
                            <div class="col-sm-1">
                                <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                            </div>
                        </div>
                    </g:if>
                </g:each>
            </fieldset>
        </g:each>
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#economic_indicators">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>

    <div role="tabpanel" class="tab-pane" id="economic_indicators">
        <g:each var="subClass" in="${indSubClass.economic}">
            <fieldset>
                <legend><h5>${subClass.value[0].label}</h5></legend>
                <g:each var="indicator" in="${indicators.economic}">
                    <g:if test="${subClass.key == indicator.subClass}">
                        <div class="form-group">
                            <label for="<%= indicator.id %>" class="col-sm-6 control-label">${indicator.label}</label>
                            <g:set var="wasFilled" value="${values[indicator.id] != null}" />
                            <div class="col-sm-5">
                                <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                                    <g:each var="category" in="${indCategories[indicator.category]}">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="<%= indicator.id %>" value="<%= category.id %>" <g:if test="${ wasFilled && values[indicator.id] == category.id}"> checked </g:if>> <%= category.label %>
                                            </label>
                                        </div>
                                    </g:each>
                                </g:if>
                                <g:elseif test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Real' }">
                                    <input type="text" class="form-control" name="${indicator.id}" value="${values[indicator.id]}">
                                </g:elseif>
                            </div>
                            <div class="col-sm-1">
                                <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                            </div>
                        </div>
                    </g:if>
                </g:each>
            </fieldset>
        </g:each>
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
        <g:each var="subClass" in="${indSubClass.social}">
            <fieldset>
                <legend><h5>${subClass.value[0].label}</h5></legend>
                <g:each var="indicator" in="${indicators.social}">
                    <g:if test="${subClass.key == indicator.subClass}">
                        <div class="form-group">
                        <label for="<%= indicator.id %>" class="col-sm-6 control-label">${indicator.label}</label>
                        <g:set var="wasFilled" value="${values[indicator.id] != null}" />
                        <div class="col-sm-5">
                            <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                                <g:each var="category" in="${indCategories[indicator.category]}">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="<%= indicator.id %>" value="<%= category.id %>" <g:if test="${ wasFilled && values[indicator.id] == category.id}"> checked </g:if>> <%= category.label %>
                                        </label>
                                    </div>
                                </g:each>
                            </g:if>
                            <g:elseif test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Real' }">
                                <input type="text" class="form-control" name="${indicator.id}" value="${values[indicator.id]}">
                            </g:elseif>
                        </div>
                        <div class="col-sm-1">
                            <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                        </div>
                    </div>
                    </g:if>
                </g:each>
            </fieldset>
        </g:each>
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

