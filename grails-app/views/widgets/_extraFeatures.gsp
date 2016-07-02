<div class="col-md-12 section text-center">
    <button class="btn btn-primary center" data-toggle="collapse" type="button" data-target="#New<%=name%>" aria-expanded="false" aria-controls="New<%=name%>">
        <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <%=title%>
    </button>
</div>

<div id="New<%=name%>" class="collapse" class="section">
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
                        <g:render template="/widgets/label" model="[id: id+'['+i+'][ui:hasName]', label: (i+1)]"></g:render>
                    </td>
                    <td>
                        <g:render template="/widgets/textArea" model="[id: id+'['+i+'][ui:hasName]', text: values[i]? values[i].name : null]"> </g:render>
                    </td>
                    <td>
                        <g:render template="/widgets/textArea" model="[id: id+'['+i+'][:hasJustification]', text: values[i]? values[i].justification : null]"> </g:render>
                    </td>
                    <td>
                        <g:set var="extraFeatureValue" value="${values[i]? values[i].value : null}"></g:set>
                        <g:each var="option" in="${options}">
                            <g:render template="/widgets/radioInput" model="[id: id+'['+i+'][ui:value]', value: option.id, label: option.label, checked: extraFeatureValue == option.value ]" ></g:render>
                        </g:each>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
</div>

