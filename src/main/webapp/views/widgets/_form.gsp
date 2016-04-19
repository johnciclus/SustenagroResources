<form <g:if test="${id}"> id="<%=id%>" </g:if> <g:if test="${action}"> action="<%=action%>" </g:if> <g:if test="${method}"> method="<%=method%>" </g:if> <g:if test="${formClass}"> class="<%=formClass%>" </g:if>>
    <g:if test="${widgets}">
        <g:each in="${widgets}">
            <g:render template="/widgets/${it.widget}" model="${it.attrs}"/>
        </g:each>
    </g:if>
</form>