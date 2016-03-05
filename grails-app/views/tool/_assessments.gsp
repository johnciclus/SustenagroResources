<h5 class="text-primary">Selecionar avaliação</h5>
<form id="select_assessment" action="/tool/selectAssessment" method="post" class="form-horizontal" >
    <input type="hidden" name="production_unit_id" value="http://bio.icmc.usp.br/sustenagro#${production_unit}">
    <div class="form-group">
        <label for="assessment" class="col-sm-4 control-label">Avaliações</label>
        <div class="col-sm-8">
            <table data-toggle="table"
                   data-click-to-select="true"
                   data-height="240"
                   data-select-item-name="assessment">
                <thead>
                <tr>
                    <th></th>
                    <th data-field="name">Nome</th>
                </tr>
                </thead>
                <tbody>
                    <g:each status="i" var="it" in="${assessments}">
                        <tr data-index="${i}">
                            <td><input data-index="${i}" type="radio" name="assessment" value="${it.id}"></td>
                            <td>${it.label}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>
    <div class="form-group col-sm-12 text-center">
        <input id="get_assessment" type="submit" class="btn btn-primary" value="Ver avaliação"/>
    </div>
</form>
