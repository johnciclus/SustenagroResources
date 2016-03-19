<form id="<%=id%>" action="<%=action%>" method="<%=method%>" class="<%=formClass%>">
    <g:render template="/widgets/hidden" model="[id: 'evalObjType', value: evalObjType]"/>
    <g:if test="${widgets}">
        <g:each var="widget" in="${widgets}">
            <g:render template="/widgets/formGroup" model="[widgetName: widget.value.widget, model: widget.value.attrs]"/>
        </g:each>
    </g:if>
    <g:render template="/widgets/formGroup" model="[widgetName: 'submit', widgetClass: 'col-sm-12 text-center', model: [value: submitLabel]]"/>
</form>