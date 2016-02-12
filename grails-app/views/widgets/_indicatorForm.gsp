<form id="indicator_form" action="/admin/indicators" method="post" class="form-horizontal">
    <div class="form-group">
        <input type="hidden" id="${_csrf.parameterName}" name="${_csrf.parameterName}" value="${_csrf.token}"}/>
        <input type="hidden" id="id_base" name="id_base" value="${indicator.id}"/>
        <g:each var="tag" in="${ind_tags}">
            <label for="${tag}" class="col-sm-4 control-label">${tag.capitalize()}</label>
            <div class="col-sm-8">
                <g:if test="${tag == 'valuetype'}">
                    <select id="${tag}" name="${tag}" class="form-control">
                        <g:each var="el" in="${valuetypes}">
                            <option value="${el.valuetype}" <g:if test="${ indicator.valuetype == el.valuetype}"> selected </g:if>> ${el.valuetype}</option>
                        </g:each>
                    </select>
                </g:if>
                <g:elseif test="${tag == 'dimension'}">
                    <select id="${tag}" name="${tag}" class="form-control select-${tag}">
                        <g:each var="el" in="${dimensions}">
                            <option value="${el.id}" <g:if test="${ indicator.dimension == el.id}"> selected </g:if>> ${el.id}</option>
                        </g:each>
                    </select>
                </g:elseif>
                <g:elseif test="${tag == 'attribute'}">
                    <select id="${tag}" name="${tag}" class="form-control select-${tag}">
                        <g:each var="el" in="${attributes[indicator.dimension]}">
                            <option value="${el.attribute}" <g:if test="${ indicator.attribute == el.attribute}"> selected </g:if>> ${el.attribute}</option>
                        </g:each>
                    </select>
                </g:elseif>
                <g:else>
                    <input id="${tag}" type="text" class="form-control input input-text-lg" name="${tag}" value="${indicator[tag]}" >
                </g:else>
            </div>
        </g:each>
    </div>
    <div class="form-group">
        <table id = "valuetype-table"
               data-toggle="table"
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
            <g:each var="el" in="${options[indicator.valuetype]}">
                <tr>
                    <td>
                        <input id="id" type="text" class="form-control input input-text-lg" value="${el.id}" >
                    </td>
                    <td>
                        <input id="label" type="text" class="form-control input input-text-lg" value="${el.label}" >
                    </td>
                    <td>
                        <input id="value" type="text" class="form-control input input-text-lg" value="${el.value}" >
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div class="form-group text-center">
        <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
    </div>
</form>