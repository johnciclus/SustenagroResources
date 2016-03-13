<ul id="<%=id%>_tab" class="nav nav-tabs">
    <g:if test="${tabs}">
        <g:each var="tab" in="${tabs}">
            <g:if test="${tab.value.widget}">
                <g:render template="/widgets/${tab.value.widget}" model="${tab.value.args}" />
            </g:if>
        </g:each>
    </g:if>
</ul>

<form id="<%=id%>_form" action="/tool/report" method="post" class="form-horizontal">
    <div id="<%=id%>_content" class="tab-content">
        <g:render template="/widgets/hidden" model="[id: 'evalObjInstance', value: evalObjInstance]"/>
        <g:each var="widget" in="${widgets}">
            <g:if test="${widget.value.args}">
                <g:render template="/widgets/tabPanel" model="${widget.value.args}" />
            </g:if>
        </g:each>
    </div>
</form>