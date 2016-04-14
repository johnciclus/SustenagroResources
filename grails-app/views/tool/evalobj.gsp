<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Tool</title>
		<asset:stylesheet href="bootstrap-table.min.css"/>
		<asset:javascript src="bootstrap-table.min.js"/>
		<asset:javascript src="validator.min.js"/>
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
						$('#analyses_form table').bootstrapTable()
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
		</script>
	</body>
</html>