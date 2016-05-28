<g:render template="/widgets/title" model="[text: g.message(code: 'analyses.title')]"/>
<form id="analyses_form" action="/tool/selectAnalysis" method="post" class="form-horizontal" >
    <input type="hidden" name="evaluation_object_id" value="${evaluation_object_id}">
    <div class="form-group required">
        <label for="analysis" class="col-sm-4 control-label"><g:message code="analyses.label" /></label>
        <div class="col-sm-8">
            <table data-toggle="table"
                   data-click-to-select="true"
                   data-height="240"
                   data-select-item-name="analysis">
                <thead>
                <tr>
                    <th></th>
                    <th data-field="name"><g:message code="label.name" /></th>
                </tr>
                </thead>
                <tbody>
                    <g:each status="i" var="it" in="${analyses}">
                        <tr data-index="${i}">
                            <td><input data-index="${i}" type="radio" name="analysis" value="${it.id}" required></td>
                            <td>${it.label}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-12 text-center">
            <g:render template="/widgets/submit" model="[value: g.message(['code': 'analyses.submit'])]" />
        </div>
    </div>
</form>

<script type="text/javascript">
    $("#analyses_form").each( function(index){
        console.log($(this));
        $(this).validate({
            errorClass: "has-error",
            errorPlacement: function(error, element) {
                var form_group = $(element).parents('.form-group');
                form_group.children(':last-child').append(error);
            },
            highlight: function(element, errorClass, validClass) {
                //console.log('highlight');
                var form_group = $(element).parents('.form-group');
                form_group.addClass(errorClass).removeClass(validClass);
            },
            unhighlight: function(element, errorClass, validClass) {
                //console.log('unhighlight');
                var form_group = $(element).parents('.form-group');
                form_group.removeClass(errorClass).addClass(validClass);
            }
        });
    });
</script>
