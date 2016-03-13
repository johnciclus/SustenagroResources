<g:if test="${userWeight}"> <g:set var="firstColWidth" value="4" /> </g:if>
<g:else> <g:set var="firstColWidth" value="6" /> </g:else>

<g:each var="subClass" in="${subClasses}">
    <fieldset>
    <legend><h5>${subClass.value.label}</h5></legend>
        <g:each var="feature" in="${subClass.value.subClass}">
        <div class="form-group">
            <label for="<%= feature.value.id %>" class="col-sm-${firstColWidth} control-label">${feature.value.label}</label>
            <g:set var="hasValue" value="${values[feature.value.id] != null}" />
            <div class="col-sm-5">
                <g:if test="${feature.value.valueType =='http://purl.org/biodiv/semanticUI#Boolean' || feature.value.valueType =='http://purl.org/biodiv/semanticUI#Categorical'}">
                    <g:each var="option" in="${categories[feature.value.category]}">
                        <div class="radio">
                            <label>
                                <input type="radio" name="<%= feature.value.id %>" value="<%= option.id %>" <g:if test="${hasValue && values[feature.value.id] == option.id}"> checked </g:if>> <%= option.label %>
                            </label>
                        </div>
                    </g:each>
                </g:if>
                <g:elseif test="${feature.value.valueType =='http://purl.org/biodiv/semanticUI#Real' }">
                    <input type="text" class="form-control" name="${feature.value.id}" value="${values[feature.value.id]}">
                </g:elseif>
            </div>
            <g:if test="${userWeight}">
            <div class="col-sm-2">
                <g:set var="hasWeight" value="${weights[feature.value.id] != null}" />
                <select id="<%= feature.value.id %>-<%=userWeightLabel%>>" name="<%= feature.value.id %>-<%=userWeightLabel%>" class="form-control clear">
                    <g:if test="${hasWeight == false}">
                        <option selected disabled hidden value=''></option>
                    </g:if>
                    <g:each in="${userWeight}">
                        <option value="${it.id}" <g:if test="${hasWeight && weights[feature.value.id] == it.id}"> selected </g:if>>${it.label}</option>
                    </g:each>
                </select>
            </div>
            </g:if>
            <div class="col-sm-1">
                <g:render template="/widgets/clearButton" model="[id: feature.value.id]"/>
            </div>
        </div>
        </g:each>
    </fieldset>
</g:each>
