<form action="/tool/selectEvaluationObject" method="post" class="form-horizontal" >
    <div class="form-group">
        <label for="evaluation_object_id" class="col-sm-3 control-label">${label}</label>
        <div class="col-sm-6">
            <select id="evaluation_object_id" name="evaluation_object_id" class="form-control">
                <option selected disabled hidden value=''></option>
                <g:each in="${evaluationObjects}">
                    <option value="${it.id}">${it.label}</option>
                </g:each>
            </select>
        </div>
        <div class="col-sm-2">
            <input id="new_analysis" type="submit" class="btn btn-primary" value="${submitLabel}" disabled/>
        </div>
    </div>
</form>

<div id="analyses_form" class="section">

</div>