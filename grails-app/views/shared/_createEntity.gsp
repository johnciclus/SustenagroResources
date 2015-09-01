<h5 class="text-primary">Cadastrar nova unidade produtiva para realizar avaliação</h5>
<form id="create_form" action="/tool/createProductionUnit" method="post" class="form-horizontal" >
    <div class="form-group">
        <label for="production_unit_name" class="col-sm-4 control-label">Nome da unidade produtiva</label>
        <div class="col-sm-8">
            <input id="production_unit_name" name="production_unit_name" type="text" class="form-control" placeholder="Nome">
        </div>
    </div>
    <div class="form-group">
        <label for="production_unit_microregion" class="col-sm-4 control-label">Microrregião da unidade produtiva</label>
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
    </div>
    <div class="form-group">
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
    </div>
    <div class="form-group">
        <div class="col-sm-4 text-right">
            <label for="production_unit_type" class="control-label">Caracterização dos sistemas produtivos no Centro-Sul</label>
            <p>Temos disponíveis as seguintes caraterizações:</p>
        </div>
        <div class="col-sm-8">
            <table data-toggle="table"
                   data-click-to-select="true"
                   data-select-item-name="production_unit_type">
                <thead>
                <tr>
                    <th></th>
                    <th data-field="characterization">Caracterização</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="i" in="${production_unit_types}" var="it">
                    <tr data-index="${i}">
                        <td><input data-index="${i}" type="radio" name="production_unit_type" value="${it.id}" ></td>
                        <td>${it.label}</td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
    <div class="form-group col-sm-12 text-center">
        <input type="submit" class="btn btn-primary" value="Cadastrar" />
    </div>
</form>