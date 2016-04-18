<table data-toggle="table"
    data-click-to-select="true"
    <g:if test="${data && data.size()>5}">
        data-height="240"
    </g:if>
    <g:if test="${header == null}">
        data-show-header=false
    </g:if>
    <g:if test="${selectType == 'checkbox'}">
        data-single-select=false
    </g:if>>
    <thead>
    <tr>
        <th></th>
        <th data-field="name"><%=header%></th>
    </tr>
    </thead>

    <tbody>
    <g:if test="${data && data.size() == 1}">
        <g:set var="checked" value="checked" />
    </g:if>

    <g:each status="i" var="row" in="${data}">
        <tr data-index="${i}">
            <td><input data-index="${i}" type="<%=selectType%>" name="<%=id%>" value="<%=row.id%>" <%=checked%> <g:if test="${required}">required</g:if>></td>
            <td><%=row.label%></td>
        </tr>
    </g:each>
    </tbody>
</table>