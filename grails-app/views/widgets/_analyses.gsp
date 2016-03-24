<g:render template="/widgets/title" model="[text: title]"/>
<form action="/tool/selectAnalysis" method="post" class="form-horizontal" >
    <input type="hidden" name="evaluation_object_id" value="${evaluation_object_id}">
    <div class="form-group">
        <label for="analysis" class="col-sm-4 control-label">Análises</label>
        <div class="col-sm-8">
            <table data-toggle="table"
                   data-click-to-select="true"
                   data-height="240"
                   data-select-item-name="analysis">
                <thead>
                <tr>
                    <th></th>
                    <th data-field="name">Nome</th>
                </tr>
                </thead>
                <tbody>
                    <g:each status="i" var="it" in="${analyses}">
                        <tr data-index="${i}">
                            <td><input data-index="${i}" type="radio" name="analysis" value="${it.id}"></td>
                            <td>${it.label}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>
    <div class="form-group col-sm-12 text-center">
        <input type="submit" class="btn btn-primary" value="<%=submitLabel%>"/>
    </div>
</form>
