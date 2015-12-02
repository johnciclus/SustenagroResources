<ul id="efficiency_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#cost_production_efficiency" aria-controls="cost_production_efficiency" role="tab" data-toggle="tab">1. Eficiência da produção / custo  </a></li>
    <li role="presentation">                <a href="#technologic_efficiency" aria-controls="technologic_efficiency" role="tab" data-toggle="tab">        2. Eficiência tecnológica </a></li>
</ul>

<div id="efficiency_content" class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="cost_production_efficiency">
        <g:each var="subClass" in="${proSubClass}">
            <fieldset>
                <legend><h5>${subClass.value.label}</h5></legend>
                <g:each var="feature" in="${productionFeatures}">
                    <g:if test="${subClass.key == feature.subClass}">
                        <div class="form-group">
                            <label for="<%= feature.id %>" class="col-sm-6 control-label">${feature.label}</label>
                            <g:set var="hasValue" value="${values[feature.id] != null}" />
                            <div class="col-sm-5">
                                <g:if test="${feature.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || feature.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                                    <g:each var="category" in="${proCategories[feature.category]}">
                                        <div class="radio">
                                            <label>
                                                <input type="radio" name="<%= feature.id %>" value="<%= category.id %>" <g:if test="${ hasValue && values[feature.id] == category.id}"> checked </g:if>> <%= category.label %>
                                            </label>
                                        </div>
                                    </g:each>
                                </g:if>
                                <g:elseif test="${feature.valueType =='http://bio.icmc.usp.br/sustenagro#Real' }">
                                    <input type="text" class="form-control" name="${feature.id}" value="${values[feature.id]}">
                                </g:elseif>
                            </div>
                            <div class="col-sm-1">
                                <button id="<%= feature.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                            </div>
                        </div>
                    </g:if>
                </g:each>
            </fieldset>
        </g:each>
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
    <g:each var="subClass" in="${tecSubClass}">
        <fieldset>
            <legend><h5>${subClass.value.label}</h5></legend>
            <g:each var="tech" in="${technologyFeatures}">
                <g:if test="${subClass.key == tech.subClass}">
                    <div class="form-group">
                        <label for="<%= tech.id %>" class="col-sm-4 control-label">${tech.label}</label>
                        <g:set var="hasValue" value="${values[tech.id] != null}" />
                        <div class="col-sm-5">
                            <g:if test="${tech.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || tech.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                                <g:each var="category" in="${tecCategories[tech.category]}">
                                    <div class="radio">
                                        <label>
                                            <input type="radio" name="<%= tech.id %>" value="<%= category.id %>" <g:if test="${ hasValue && values[tech.id] == category.id}"> checked </g:if>> <%= category.label %>
                                        </label>
                                    </div>
                                </g:each>
                            </g:if>
                            <g:elseif test="${tech.valueType =='http://bio.icmc.usp.br/sustenagro#Real' }">
                                <input type="text" class="form-control" name="${tech.id}" value="${values[tech.id]}">
                            </g:elseif>
                        </div>
                        <div class="col-sm-2">
                            <g:set var="hasWeight" value="${weights[tech.id] != null}" />
                            <g:if test="${subClass.key == 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheField'}">
                                <select id="<%= tech.id %>-alignment" name="<%= tech.id %>-alignment" class="form-control clear">
                                    <g:if test="${hasWeight == false}">
                                        <option selected disabled hidden value=''></option>
                                    </g:if>
                                    <g:each in="${tecAlignment}">
                                        <option value="${it.id}" <g:if test="${hasWeight && weights[tech.id] == it.id}"> selected </g:if>>${it.label}</option>
                                    </g:each>
                                </select>
                            </g:if>
                            <g:elseif test="${subClass.key == 'http://bio.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheIndustrial'}">
                                <select id="<%= tech.id %>-optimization" name="<%= tech.id %>-optimization" class="form-control clear">
                                    <g:if test="${hasWeight == false}">
                                        <option selected disabled hidden value=''></option>
                                    </g:if>
                                    <g:each in="${tecOptimization}">
                                        <option value="${it.id}" <g:if test="${hasWeight && weights[tech.id] == it.id}"> selected </g:if>>${it.label}</option>
                                    </g:each>
                                </select>
                            </g:elseif>
                        </div>
                        <div class="col-sm-1">
                            <button id="<%= tech.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                        </div>
                    </div>
                </g:if>
            </g:each>
        </fieldset>
    </g:each>
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
