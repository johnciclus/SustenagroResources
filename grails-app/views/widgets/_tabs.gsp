<ul id="<%=id%>_tab" class="nav nav-tabs">
    <g:if test="${widgets}">
        <g:each var="widget" in="${widgets}">
            <g:render template="/widgets/${widget.value.widget}" model="${widget.value.args}" />
        </g:each>
    </g:if>
</ul>

<form id="<%=id%>_form" action="/tool/report" method="post" class="form-horizontal">
    <div id="<%=id%>_content" class="tab-content">
        <g:if test="${widgets}">
            <g:each status="i" var="widget" in="${widgets}">
                <g:if test="${widget.value.widget == 'tab'}">
                    <g:render template="/widgets/tabPanel" model="${widget.value.args}" />
                </g:if>
            </g:each>
        </g:if>
    </div>
</form>