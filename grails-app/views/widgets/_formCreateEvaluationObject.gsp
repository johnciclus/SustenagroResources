<form id="<%=id%>" action="<%=action%>" method="<%=method%>" class="<%=formClass%>" data-toggle="validator" role="form">
    <g:if test="${widgets}">
        <g:each var="widget" in="${widgets}">
            <g:render template="/widgets/formGroup" model="[widgetName: widget.value.widget, model: widget.value.attrs]"/>
        </g:each>
    </g:if>
    <div class="form-group">
        <div class="col-sm-12 text-center">
            <g:render template="/widgets/submit" model="[value: g.message(['code': 'default.form.register'])]" />
        </div>
    </div>
</form>