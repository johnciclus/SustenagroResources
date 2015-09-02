<div class="col-sm-4 text-right">
    <label for="production_unit_microregion" class="control-label">Microrregião da unidade produtiva</label>
</div>

<div class="col-sm-8">
    <table data-toggle="table"
           data-click-to-select="true"
           data-height="240"
           data-select-item-name="production_unit_microregion">
        <thead>
        <tr>
            <th></th>
            <th data-field="name">Nome</th>
        </tr>
        </thead>
        <tbody>
        <g:each status="i" in="${microregions}" var="it">
            <tr data-index="${i}">
                <td><input data-index="${i}" type="radio" name="production_unit_microregion" value="${it.id}" ></td>
                <td>${it.label}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>