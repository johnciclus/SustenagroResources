<div class="col-sm-4 text-right">
    <label for="${id}" class="control-label">${label}</label>
</div>

<div class="col-sm-8">
    <table data-toggle="table"
           data-click-to-select="true"
           data-height="240"
           data-select-item-name="${id}">
        <thead>
        <tr>
            <th></th>
            <th data-field="name">Nome</th>
        </tr>
        </thead>
        <tbody>

            <tr data-index="0">
                <td><input data-index="0" type="radio" name="${id}" value="id" ></td>
                <td>label</td>
            </tr>
        </tbody>
    </table>
</div>