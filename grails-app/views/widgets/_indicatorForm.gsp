<form class="form-horizontal">
    <div class="form-group">
        <h5>${indicator.label}</h5>
        <input type="hidden" name="id" value="${indicator.id}">
        <g:each var="tag" in="${ind_tags}">
            <label for="${i}_${tag}" class="col-sm-4 control-label">${tag.capitalize()}</label>
            <div class="col-sm-8">
                <g:if test="${tag == 'valuetype'}">
                    <select id="${i}_${tag}" class="form-control">
                        <g:each var="el" in="${valuetypes}">
                            <option value="${el.valuetype}" <g:if test="${ indicator.valuetype == el.valuetype}"> selected </g:if>> ${el.valuetype}</option>
                        </g:each>
                    </select>
                </g:if>
                <g:elseif test="${tag == 'dimension'}">
                    <select id="${i}_${tag}" class="form-control select-${tag}">
                        <g:each var="el" in="${dimensions}">
                            <option value="${el.id}" <g:if test="${ indicator.dimension == el.id}"> selected </g:if>> ${el.id}</option>
                        </g:each>
                    </select>
                </g:elseif>
                <g:elseif test="${tag == 'attribute'}">
                    <select id="${i}_${tag}" class="form-control select-${tag}">
                        <g:each var="el" in="${attributes[indicator.dimension]}">
                            <option value="${el.attribute}" <g:if test="${ indicator.attribute == el.attribute}"> selected </g:if>> ${el.attribute}</option>
                        </g:each>
                    </select>
                </g:elseif>
                <g:else>
                    <input id="${i}_${tag}" type="text" class="form-control input input-text-lg" name="${i}_${tag}" value="${indicator[tag]}" >
                </g:else>
            </div>
        </g:each>
    </div>
    <div>
        <table data-toggle="table"
               data-sort-name="id"
               data-sort-order="desc">
            <thead>
            <tr>
                <th data-field="id" data-sortable="true">ID</th>
                <th data-field="name" data-sortable="true">Name</th>
                <th data-field="value" data-sortable="true">Value</th>
            </tr>
            </thead>
            <tbody>
            <g:each status="i" var="el" in="${options[indicator.valuetype]}">
                <tr data-index="${i}">
                    <td>
                        <label for="${i}_id" class="col-sm-4 control-label">${el.id}</label>
                    </td>
                    <td>
                        <input id="${i}_label" type="text" class="form-control input input-text-lg" value="${el.label}" >
                    </td>
                    <td>
                        <input id="${i}_value" type="text" class="form-control input input-text-lg" value="${el.value}" >
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
</form>
<form id="indicators_form" action="/admin/indicators" method="post" class="form-inline-block pull-right" role="form">
    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
</form>
<form id="reset_indicators_form" action="/admin/indicatorsReset" method="post" class="form-inline-block pull-right" role="form">
    <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
</form>