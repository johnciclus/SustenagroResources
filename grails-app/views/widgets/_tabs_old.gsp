<g:if test="${tabs}">
    <ul id="<%=id%>" class="nav nav-tabs">
        <g:each var="tab" in="${tabs}">
            <g:render template="/widgets/tab" model="${tab.value.attrs}" />
        </g:each>
    </ul>

    <div id="<%=id%>_content" class="tab-content">
        <g:each var="panel" in="${tabpanels}">
            <g:if test="${panel.value}">
                <g:render template="/widgets/tabPanel" model="${[tab: tabs[panel.key].attrs, widgets: panel.value]}" />
            </g:if>
        </g:each>
    </div>
</g:if>