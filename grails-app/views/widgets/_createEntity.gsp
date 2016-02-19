<h5 class="text-primary">Cadastrar nova unidade produtiva para realizar avaliação</h5>
<form id="create_form" action="/tool/createProductionUnit" method="post" class="form-horizontal">
    <g:if test="${widgets}">
        <g:each var="widget" in="${widgets}">
            <div class="form-group">
                <g:render template="/widgets/${widget.value.widget}" model="${widget.value.args}" />
            </div>
        </g:each>
    </g:if>

    <div class="form-group col-sm-12 text-center">
        <input type="submit" class="btn btn-primary" value="Cadastrar" />
    </div>
</form>