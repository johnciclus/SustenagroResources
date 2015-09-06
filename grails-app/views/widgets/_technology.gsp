<div class="col-sm-4 text-right">
    <label for="production_unit_technology" class="control-label">Tecnologias disponíveis</label>
    <p>Temos disponíveis as seguintes tecnologias para caracterizar os sistemas de produção de cana-de açúcar no Centro-Sul do Brasil:</p>
</div>

<div class="col-sm-8">
    <table data-toggle="table"
           data-click-to-select="true"
           data-select-item-name="production_unit_technology">
        <thead>
        <tr>
            <th></th>
            <th data-field="techonology">Tecnologia</th>
            <th data-field="description">Descrição</th>
        </tr>
        </thead>
        <tbody>
        <g:each status="i" in="${technologies}" var="it">
            <tr data-index="${i}">
                <td><input data-index="${i}" type="radio" name="production_unit_technology" value="${it.id}" ></td>
                <td>${it.label}</td>
                <td>${it.description}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>