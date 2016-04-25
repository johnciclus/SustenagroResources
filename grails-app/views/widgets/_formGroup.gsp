<div class="form-group <%=widgetClass%> <g:if test='${model.required}'>required</g:if>">
    <g:render template="/widgets/${widgetName}" model="${model}" />
</div>