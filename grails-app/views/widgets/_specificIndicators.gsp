<div class="col-md-12 section text-center">
    <button class="btn btn-primary center" data-toggle="collapse" type="button" data-target="#New<%=id%>" aria-expanded="false" aria-controls="New<%=id%>">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <%=title%>
    </button>
</div>

<div id="New<%=id%>" class="collapse" class="section">
    <table data-toggle="table" class="table">
        <thead>
        <tr>
            <th data-field="new">

            </th>
            <g:each var="col" in="${header}">
                <th data-field="${col.key}">${col.value}</th>
            </g:each>
        </tr>
        </thead>
        <tbody>
            <g:each var="i" in="${(0..9)}">
                <tr data-index="${i}">
                    <td>
                        <g:render template="/widgets/label" model="[id: id+'['+i+'][name]', label: (i+1)]"></g:render>
                    </td>
                    <td>
                        <g:render template="/widgets/textArea" model="[id: id+'['+i+'][name]']"> </g:render>
                    </td>
                    <td>
                        <g:render template="/widgets/textArea" model="[id: id+'['+i+'][justification]']"> </g:render>
                    </td>
                    <td>
                        <g:render template="/widgets/radioInput" model="[id: id+'['+i+'][value]', value: 'more_sustainable', label: 'Mais sustentável']" ></g:render>
                        <g:render template="/widgets/radioInput" model="[id: id+'['+i+'][value]', value: 'less_sustainable', label: 'Menos sustentável']" ></g:render>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>

