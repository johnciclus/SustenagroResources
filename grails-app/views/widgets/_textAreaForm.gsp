<div class="col-sm-4 text-right">
    <label for="<%=id%>" class="control-label"><%=label%></label>
</div>

<div class="col-sm-8">
    <g:render template="/widgets/textArea" model="[id: id, placeholder: placeholder]"/>
</div>