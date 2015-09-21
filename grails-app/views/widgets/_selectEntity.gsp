<h5 class="text-primary">Selecionar unidade produtiva</h5>
<form id="select_production_units" action="/tool/selectProductionUnit" method="post" class="form-horizontal" >
    <div class="form-group">
        <label for="production_unit_id" class="col-sm-4 control-label">Unidade produtiva</label>
        <div class="col-sm-6">
            <select id="production_unit_id" name="production_unit_id" class="form-control">
                <option selected disabled hidden value=''></option>
                <g:each in="${production_units}">
                    <option value="${it.id}">${it.label}</option>
                </g:each>
            </select>
        </div>
        <div class="col-sm-2">
            <input id="new_evaluation" type="submit" class="btn btn-primary" value="Nova avaliação" disabled/>
        </div>
    </div>
</form>

<div id="evaluations_form" class="section">

</div>