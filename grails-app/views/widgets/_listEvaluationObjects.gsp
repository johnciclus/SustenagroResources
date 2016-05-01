<form id='selectEvaluationObject' action="/tool/selectEvaluationObject" method="post" class="form-horizontal" >
    <div class="form-group required">
        <label for="evaluation_object_id" class="col-sm-4 control-label">${label}</label>
        <div class="col-sm-6">
            <select id="evaluation_object_id" name="evaluation_object_id" class="form-control" required>
                <g:if test="${!id}">
                    <option selected disabled hidden value=""></option>
                </g:if>
                <g:each in="${evaluationObjects}">
                    <option value="${it.id}" <g:if test="${id == it.id}"> selected </g:if> >${it.label}</option>
                </g:each>
            </select>
        </div>
    </div>
    <g:if test="${id}">
        <g:render template="/widgets/tableReport" model="[header: [label: 'Propiedade', value: 'Valor'], data: data]" />
    </g:if>
    <g:render template="/widgets/formGroup" model="[widgetName: 'submit', widgetClass: 'col-sm-12 text-center', model: [value: submitLabel]]"/>
</form>

<div id="analyses_form_container" class="section">

</div>