<div class="btn-group btn-group-justified">
    <a href="#" class="btn btn-primary">Ambientais</a>
    <a href="#" class="btn btn-default">Econômicos</a>
    <a href="#" class="btn btn-default">Sociais</a>
</div>


<table class="table table-striped table-hover ">
    <thead>

    <tr class="text-primary">
        <th>#</th>
        <th>Descrição</th>
        <th>Importância</th>
    </tr>
    </thead>
    <tbody>
    <g:if test="${recommendations.class == String}">
        <tr>
            <td>1</td>
            <td> ${raw(recommendations)} </td>
            <td>Meia</td>
        </tr>
    </g:if>
    <g:else>
        <g:each in="${recommendations}">
            <tr>
                <td>1</td>
                <td> ${raw(it)} </td>
                <td>Meia</td>
            </tr>
        </g:each>
    </g:else>
    <tr>
        <td>2</td>
        <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
        <td>Baixa</td>
    </tr>
    </tbody>
</table>

<div id="graphic">

</div>