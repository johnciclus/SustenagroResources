<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Tool</title>
		<asset:stylesheet href="bootstrap-table.min.css"/>
		<asset:javascript src="bootstrap-table.min.js"/>
        <!--
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript" src="http://mbostock.github.com/d3/d3.js"></script>
        -->
	</head>
	<body>
		<div class="row main">
			<div id="content" class="col-sm-10 col-sm-offset-1 content">
				<div class="section">
					<g:render template="/widgets/title" model="['text': data['title']]" />
				</div>
				<div class="section">
					<g:render template="/widgets/description" model="['text': data['description']]" />
				</div>
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
            function loadAssessments(){
                $.post('/tool/assessments',
                        { 'production_unit_id':  $('#production_unit_id').val()},
                        function( data ) {
                            $('#assessments_form').html(data);
                            $('#assessments_form table').bootstrapTable()
                            $('#new_assessment').prop('disabled', false);
                        }
                );
            }

			if($('#production_unit_id').val()!=null){
				loadAssessments();
			}
			$('#production_unit_id').change( function(){
				loadAssessments();
			});
		</script>
	</body>
</html>