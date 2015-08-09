
<table class="table table-striped table-hover ">
    <thead>
        <tr class="text-primary">
            <th>Recomendação</th>
        </tr>
    </thead>
    <tbody>
        <g:if test="${report.class == String}">
            <tr>
                <td> ${raw(report)} </td>
            </tr>
        </g:if>
        <g:else>
        <g:each in="${report}">
            <tr>
                <td> ${raw(it)} </td>
            </tr>
        </g:each>
        </g:else>
    </tbody>
</table>

<div id="graphic">

</div>