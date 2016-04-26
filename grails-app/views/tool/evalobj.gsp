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
        <asset:javascript src="localization/messages_pt_BR.min.js"/>
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
			function loadAnalyses(){
                $.post('/tool/analyses',
					{ 'evaluation_object_id':  $('#evaluation_object_id').val()},
					function( data ) {
						$('#analyses_form').html(data);
						$('#analyses_form table').bootstrapTable();
						$('#new_analysis').prop('disabled', false);
					}
                );
            }

			if($('#evaluation_object_id').val()!=null){
				loadAnalyses();
			}
			$('#evaluation_object_id').change( function(){
				loadAnalyses(); // render objeval evaluation_object_id
			});

            $("form").each( function(index){
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



            $(".datepicker").datepicker({
                format: 'dd/mm/yyyy',
                language: "pt-BR",
                todayBtn: true,
                autoclose: true
            });
		</script>
	</body>
</html>