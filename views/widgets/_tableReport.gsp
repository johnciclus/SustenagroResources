<div class="section">
    <table data-toggle="table" class="table">
        <thead>
        <tr>
            <g:each var="col" in="${header}">
                <th data-field="${col.key}">${col.value}</th>
            </g:each>
        </tr>
        </thead>
        <tbody>
        <g:each status="i" var="row" in="${data}">
            <tr data-index="${i}">
                <g:each var="col" in="${header}">
                    <td>${row[col.key]}</td>
                </g:each>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>