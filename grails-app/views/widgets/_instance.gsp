<div class="col-sm-4 text-right">
    <label for="${id}" class="control-label">${label}</label>
</div>

<div class="col-sm-8">
    <table data-toggle="table"
           data-click-to-select="true"
           <g:if test="${data.size()>5}">
                data-height="240"
           </g:if>
           data-select-item-name="${id}">
        <thead>
        <tr>
            <th></th>
            <th data-field="name">Nome</th>
        </tr>
        </thead>
        <tbody>
            <g:each status="i" var="row" in="${data}">
                <tr data-index="${i}">
                    <td><input data-index="${i}" type="radio" name="${id}" value="${row.id}" ></td>
                    <td>${row.label}</td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>