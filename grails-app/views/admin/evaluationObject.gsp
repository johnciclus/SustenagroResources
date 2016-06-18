<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool</title>
    <asset:stylesheet href="bootstrap-table.min.css"/>
    <asset:stylesheet href="bootstrap-datepicker3.min.css"/>
    <asset:javascript src="bootstrap-table.min.js"/>
    <asset:javascript src="bootstrap-datepicker.min.js"/>
    <asset:javascript src="locales/bootstrap-datepicker.pt-BR.min.js"/>
    <asset:javascript src="jquery.validate.min.js"/>

    <g:if test="${!session.lang || session.lang=='pt'}">
        <asset:javascript src="locale/bootstrap-table-pt-BR.min.js"/>
        <asset:javascript src="localization/messages_pt_BR.min.js"/>
    </g:if>
    <g:else>
        <asset:javascript src="locale/bootstrap-table-en-US.min.js"/>
    </g:else>


    <!--
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript" src="http://mbostock.github.com/d3/d3.js"></script>
        -->
</head>
<body>
<div class="row main">
    <div id="content" class="col-sm-10 col-sm-offset-1 content">
        <g:if test="${inputs}">
            <g:each in="${inputs}">
                <div class="section">
                    <g:render template="/widgets/${it.widget}" model="${it.attrs}" />
                </div>
            </g:each>
        </g:if>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        function loadAnalyses() {
            $.post('/tool/analysesView',
                    {'id': $('#evaluation_object_id').val()},
                    function (data) {
                        $('#analyses_form_container').html(data);
                        $('#analyses_form_container table').bootstrapTable();
                        $('#new_analysis').prop('disabled', false);
                    }
            );
        }

        function loadEvaluationObject() {
            $.post('/tool/evaluationObjectView',
                    {'id': $('#evaluation_object_id').val()},
                    function (data) {
                        $('#evaluation_object_container').html(data);
                        $('#evaluation_object_container table').bootstrapTable();
                    }
            );
        }

        if ($('#evaluation_object_id').val() != null) {
            loadEvaluationObject();
            loadAnalyses();
        }

        $('#evaluation_object_id').change(function () {
            loadEvaluationObject();
            loadAnalyses();
        });

        $("input[name='http://dbpedia.org/ontology/state']").change(function () {
            $.post('/tool/microregionsView',
                    {'http://dbpedia.org/ontology/state': $(this).val()},
                    function (data) {
                        var form_group = $("label[for='http://purl.org/biodiv/semanticUI#hasMicroregion']").parents('.form-group');
                        form_group.children(':last-child').html(data);
                        form_group.find('table').bootstrapTable()
                    }
            );
        });

        $("form").each(function (index) {
            $(this).validate({
                errorClass: "has-error",
                rules: {
                    'http://purl.org/biodiv/semanticUI#hasName': {
                        remote: "/tool/evaluationObjectNameAvailability"
                    }
                },
                messages: {
                    'http://purl.org/biodiv/semanticUI#hasName': {
                        remote: jQuery.validator.format("{0} já está atribuído no sistema.")
                    }
                },
                errorPlacement: function (error, element) {
                    var form_group = $(element).parents('.form-group');
                    form_group.children(':last-child').append(error);
                },
                highlight: function (element, errorClass, validClass) {
                    //console.log('highlight');
                    var form_group = $(element).parents('.form-group');
                    form_group.addClass(errorClass).removeClass(validClass);
                },
                unhighlight: function (element, errorClass, validClass) {
                    //console.log('unhighlight');
                    var form_group = $(element).parents('.form-group');
                    form_group.removeClass(errorClass).addClass(validClass);
                }
            });
        });

        $(".datepicker").datepicker({
            format: 'dd/mm/yyyy',
            language: "pt-BR",
            todayBtn: true,
            autoclose: true
        });
    });
</script>
</body>
</html>