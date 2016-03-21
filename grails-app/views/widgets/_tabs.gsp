<ul id="<%=id%>_tab" class="nav nav-tabs">
    <g:if test="${tabs}">
        <g:each var="tab" in="${tabs}">
            <g:if test="${tab.value.widget}">
                <g:render template="/widgets/${tab.value.widget}" model="${tab.value.attrs}" />
            </g:if>
        </g:each>
    </g:if>
</ul>

<form id="<%=id%>_form" action="/tool/createAnalysis" method="post" class="form-horizontal">
    <div id="<%=id%>_content" class="tab-content">
        <g:render template="/widgets/hidden" model="[id: 'evalObjInstance', value: evalObjInstance]"/>
        <g:each var="panel" in="${tabpanels}">
            <g:if test="${panel.value}">
                <g:render template="/widgets/tabPanel" model="${[tab: tabs[panel.key].attrs, widgets: panel.value]}" />
            </g:if>
        </g:each>
    </div>
</form>