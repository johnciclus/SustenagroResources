<ul id="<%=id%>" class="nav nav-tabs">
    <g:if test="${tabs}">
        <g:each var="tab" in="${tabs}">
            <g:if test="${tab.value.widget}">
                <g:render template="/widgets/${tab.value.widget}" model="${tab.value.attrs}" />
            </g:if>
        </g:each>
    </g:if>
</ul>

<div id="<%=id%>_content" class="tab-content">
    <g:each var="panel" in="${tabpanels}">
        <g:if test="${panel.value}">
            <g:render template="/widgets/tabPanel" model="${[tab: tabs[panel.key].attrs, widgets: panel.value]}" />
        </g:if>
    </g:each>
</div>