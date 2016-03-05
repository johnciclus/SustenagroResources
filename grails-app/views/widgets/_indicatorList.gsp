<g:if test="${hasWeights}">
    <g:set var="firstColWidth" value="4" />
</g:if>
<g:else>
    <g:set var="firstColWidth" value="6" />
</g:else>

<g:each var="subClass" in="${subClasses}">
    <fieldset>
        <legend><h5>${subClass.value.subClassLabel}</h5></legend>
        <g:each var="indicator" in="${indsInSubClasses}">
            <g:if test="${subClass.key == indicator.subClass}">
                <div class="form-group">
                    <label for="<%= indicator.id %>" class="col-sm-${firstColWidth} control-label">${indicator.label}</label>
                    <g:set var="hasValue" value="${values[indicator.id] != null}" />
                    <div class="col-sm-5">
                        <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://purl.org/biodiv/semanticUI#Categorical'}">
                            <g:each var="category" in="${categories[indicator.category]}">
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="<%= indicator.id %>" value="<%= category.id %>" <g:if test="${ hasValue && values[indicator.id] == category.id}"> checked </g:if>> <%= category.label %>
                                    </label>
                                </div>
                            </g:each>
                        </g:if>
                        <g:elseif test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Real' }">
                            <input type="text" class="form-control" name="${indicator.id}" value="${values[indicator.id]}">
                        </g:elseif>
                    </div>
                    <g:if test="${hasWeights}">
                    <div class="col-sm-2">
                        <g:set var="hasWeight" value="${weights[indicator.id] != null}" />
                        <g:if test="${subClass.key == 'http://semantic.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheField'}">
                            <select id="<%= indicator.id %>-alignment" name="<%= indicator.id %>-alignment" class="form-control clear">
                                <g:if test="${hasWeight == false}">
                                    <option selected disabled hidden value=''></option>
                                </g:if>
                                <g:each in="${tecAlignment}">
                                    <option value="${it.id}" <g:if test="${hasWeight && weights[indicator.id] == it.id}"> selected </g:if>>${it.label}</option>
                                </g:each>
                            </select>
                        </g:if>
                        <g:elseif test="${subClass.key == 'http://semantic.icmc.usp.br/sustenagro#TechnologicalEfficiencyInTheIndustrial'}">
                            <select id="<%= indicator.id %>-optimization" name="<%= indicator.id %>-optimization" class="form-control clear">
                                <g:if test="${hasWeight == false}">
                                    <option selected disabled hidden value=''></option>
                                </g:if>
                                <g:each in="${tecOptimization}">
                                    <option value="${it.id}" <g:if test="${hasWeight && weights[indicator.id] == it.id}"> selected </g:if>>${it.label}</option>
                                </g:each>
                            </select>
                        </g:elseif>
                    </div>
                    </g:if>
                    <div class="col-sm-1">
                        <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear"><span class="glyphicon glyphicon-trash"></span></button>
                    </div>
                </div>
            </g:if>
        </g:each>
    </fieldset>
</g:each>