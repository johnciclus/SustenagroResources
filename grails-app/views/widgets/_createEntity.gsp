<h5 class="text-primary">Cadastrar nova unidade produtiva para realizar avaliação</h5>
<form id="create_form" action="/tool/createProductionUnit" method="post" class="form-horizontal">
    <div class="form-group">
        <label for="production_unit_name" class="col-sm-4 control-label">Nome da unidade produtiva</label>
        <div class="col-sm-8">
            <input id="production_unit_name" name="production_unit_name" type="text" class="form-control" placeholder="Nome">
        </div>
    </div>

    <g:if test="${widgets}">
        <g:each var="widget" in="${widgets}">
            <div class="form-group">
                <g:render template="/widgets/${widget.value.widget}" model="${widget.value.args}" />
            </div>
        </g:each>
    </g:if>

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
                <g:each status="i" in="${productionunit_types}" var="it">
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