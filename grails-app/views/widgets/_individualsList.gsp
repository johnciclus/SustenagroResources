<g:each var="feature" in="${subClasses}">
    <g:if test="${feature.value.weightIndividuals}"> <g:set var="colWidth" value="10" /> </g:if>
    <g:else> <g:set var="colWidth" value="12" /> </g:else>
    <g:set var="hasValue" value="${values[feature.value.id] != null && values[feature.value.id].value}" />
    <g:set var="hasJustification" value="${values[feature.value.id] != null && values[feature.value.id].justification}" />

    <div class="form-group feature">
        <div class="row">
            <label class="col-sm-8 control-label">${feature.value.label}</label>
            <div class="col-sm-4 text-right">
                <button id="<%= feature.value.id + '-justify' %>" type="button" class="btn btn-default justify btn-xs"><span class="glyphicon glyphicon-pencil"><g:message code="justification" /></span></button>
                <g:render template="/widgets/clearButton" model="[id: feature.value.id, label: g.message(code: 'clean'), widgetClass: 'btn-xs']"/>
            </div>
        </div>
        <div class="row">
            <g:if test="${feature.value.valueTypes.contains('http://purl.org/biodiv/semanticUI#Boolean') || feature.value.valueTypes.contains('http://purl.org/biodiv/semanticUI#Categorical')}">
                <g:each var="option" in="${feature.value.categoryIndividuals}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= feature.value.id %>" value="<%= option.id %>" <g:if test="${hasValue && values[feature.value.id].value == option.id}"> checked </g:if>> <%= option.label %>
                        </label>
                    </div>
                </g:each>
            </g:if>
            <g:elseif test="${feature.value.valueTypes.contains('http://purl.org/biodiv/semanticUI#Real')}">
                <input type="text" class="form-control" name="${feature.value.id}" value="${values[feature.value.id]}">
            </g:elseif>
        </div>
        <g:if test="${feature.value.weightIndividuals}">
            <g:set var="hasWeight" value="${values[feature.value.id] != null && values[feature.value.id].weight}" />
            <div class="row">
                <label for="<%= feature.value.id + '-weight' %>" class="col-sm-6 control-label weight-label"><%= feature.value.weightLabel %></label>
                <div class="col-sm-6 text-right">
                    <select id="<%= feature.value.id + '-weight' %>" name="<%= feature.value.id + '-weight' %>" class="form-control">

                        <option selected disabled hidden value=''></option>

                        <g:each var="option" in="${feature.value.weightIndividuals}">
                            <option value="${option.id}" <g:if test="${hasWeight && values[feature.value.id].weight == option.id}"> selected </g:if> >${option.label}</option>
                        </g:each>
                    </select>
                </div>
            </div>
        </g:if>
        <div class='row <g:if test="${!hasJustification}"> hidden </g:if>'>
            <label for="<%= feature.value.id + '-justification' %>" class="col-sm-4 control-label weight-label"><g:message code="justification" /></label>
            <div class="col-sm-8 text-right">
                <g:if test="${hasJustification}">
                    <g:set var="text" value="${values[feature.value.id].justification}" />
                </g:if>
                <g:else>
                    <g:set var="text" value="" />
                </g:else>
                <g:render template="/widgets/textArea" model="[id: feature.value.id + '-justification', text: text, placeholder: g.message(code: 'justification')]"/>
            </div>
        </div>
    </div>
</g:each>
