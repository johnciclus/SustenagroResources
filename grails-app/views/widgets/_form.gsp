<form id="<%=id%>" action="<%=action%>" method="<%=method%>" class="<%=formClass%>">
    <div class="form-group col-sm-12 text-center">
        <g:render template="/widgets/submit" model="[value: submitLabel]"/>
    </div>
</form>