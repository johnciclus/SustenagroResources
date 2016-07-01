<form id='selectEvaluationObject' action="/tool/selectEvaluationObject" method="post" class="form-horizontal" >
    <g:render template="/widgets/requireLabel"/>
    <div class="form-group required">
        <label for="evaluation_object_id" class="col-sm-4 control-label"><g:message code="productionUnit" /></label>
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
    <div id="evaluation_object_container">

    </div>
    <div class="form-group">
        <div class="col-sm-12 text-center">
            <g:render template="/widgets/submit" model="[value: g.message(['code': 'default.form.newAnalisys'])]" />
        </div>
    </div>
</form>

<div id="analyses_form_container" class="section">

</div>