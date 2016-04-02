<g:each var="feature" in="${subClasses}">
    <g:if test="${feature.value.weightIndividuals}"> <g:set var="firstColWidth" value="4" /> </g:if>
    <g:else> <g:set var="firstColWidth" value="6" /> </g:else>
    <div class="form-group">
        <label for="<%= feature.value.id %>" class="col-sm-${firstColWidth} control-label">${feature.value.label}</label>
        <g:set var="hasValue" value="${values[feature.value.id] != null}" />
        <div class="col-sm-5">
            <g:if test="${feature.value.valueTypes.contains('http://purl.org/biodiv/semanticUI#Boolean') || feature.value.valueTypes.contains('http://purl.org/biodiv/semanticUI#Categorical')}">
                <g:each var="option" in="${feature.value.categoryIndividuals}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= feature.value.id %>" value="<%= option.id %>" <g:if test="${hasValue && values[feature.value.id] == option.id}"> checked </g:if>> <%= option.label %>
                        </label>
                    </div>
                </g:each>
            </g:if>
            <g:elseif test="${feature.value.valueType.contains('http://purl.org/biodiv/semanticUI#Real')}">
                <input type="text" class="form-control" name="${feature.value.id}" value="${values[feature.value.id]}">
            </g:elseif>
        </div>
        <g:if test="${feature.value.weightIndividuals}">
            <g:set var="hasWeight" value="${values[feature.value.weightId] != null}" />
            <div class="col-sm-2">
                <select id="<%= feature.value.weightId %>>" name="<%= feature.value.weightId %>" class="form-control clear">
                    <g:if test="${hasWeight == false}">
                        <option selected disabled hidden value=''></option>
                    </g:if>
                    <g:each var="option" in="${feature.value.weightIndividuals}">
                        <option value="${option.id}">${option.label}</option>
                    </g:each>
                </select>
            </div>
        </g:if>
        <div class="col-sm-1">
            <g:render template="/widgets/clearButton" model="[id: feature.value.id]"/>
        </div>
    </div>
</g:each>
