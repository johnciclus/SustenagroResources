<h5 class="text-primary page-header">Selecionar unidade produtiva</h5>

<form id="list_production_units" action="/tool/assessmentProductionUnit" method="post">
    <div class="form-group">
        <select id="production_unit_id" name="production_unit_id" class="form-control">
            <g:each in="${production_units}">
                <option value="${it.id}">${it.name}</option>
            </g:each>
        </select>
    </div>
    <div class="form-group">
        <input type="submit" class="btn btn-primary" value="Seleccionar" />
    </div>
</form>
