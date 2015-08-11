<g:each in="${report}">
    <g:if test="${it[0] == 'show'}">
        <%= it[1] %>
    </g:if>
    <g:if test="${it[0] == 'recommendation'}">
        <div class='text-primary'>Recomendação</div>

        <div><%= it[1]  %></div>
    </g:if>
</g:each>


<table class="table table-striped table-hover ">
    <thead>
        <tr class="text-primary">
            <th>Recomendação</th>
        </tr>
    </thead>
    <tbody>

    </tbody>
</table>

<div id="graphic">

</div>