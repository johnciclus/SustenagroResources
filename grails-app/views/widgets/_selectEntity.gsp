<h5 class="text-primary">Selecionar unidade produtiva</h5>
<form id="select_production_units" action="/tool/selectProductionUnit" method="post" class="form-horizontal" >
    <div class="form-group">
        <label for="production_unit_id" class="col-sm-4 control-label">Unidade produtiva</label>
        <div class="col-sm-8">
            <select id="production_unit_id" name="production_unit_id" class="form-control">
                <option selected disabled hidden value=''></option>
                <g:each in="${production_units}">
                    <option value="${it.id}">${it.label}</option>
                </g:each>
            </select>
        </div>
    </div>
    <div class="form-group col-sm-12 text-center">
        <input id="new_evaluation" type="submit" class="btn btn-primary" value="Nova avaliação" disabled/>
        <button id="list_evaluations" class="btn btn-default" type="button" data-toggle="collapse" data-target="#evaluations_form" aria-expanded="false" aria-controls="evaluations_form" disabled>Listar avaliações</button>
    </div>
</form>

<div id="evaluations_form" class="section collapse">

</div>