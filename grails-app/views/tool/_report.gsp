<div class="section">
    <g:each in="${report}">
        <g:if test="${it[0] == 'show'}">
            <%= it[1] %>
        </g:if>
        <g:elseif test="${it[0] == 'recommendation'}">
            <div class='text-primary'>Recomendação</div>

            <div><%= it[1]  %></div>
        </g:elseif>
    </g:each>
</div>

<div class="section">
    <div id="graphic">

    </div>
</div>
