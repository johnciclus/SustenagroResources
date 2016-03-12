<ul id="<%=id%>_tab" class="nav nav-tabs">
    <g:if test="${tabs}">
        <g:each var="tab" in="${tabs}">
            <g:render template="/widgets/${tab.value.widget}" model="${tab.value.args}" />
        </g:each>
    </g:if>
</ul>

<form id="<%=id%>_form" action="/tool/report" method="post" class="form-horizontal">
    <div id="<%=id%>_content" class="tab-content">
        <g:each var="widget" in="${widgets}">
            <g:render template="/widgets/tabPanel" model="${widget.value.args}" />
        </g:each>
    </div>
</form>