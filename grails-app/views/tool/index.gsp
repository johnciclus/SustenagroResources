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
			<div  id="content" class="col-sm-10 col-sm-offset-1 content">
                <g:each in="${inputs}">
                    <div class="section">
                        <g:render template="/widgets/${it.widget}" model="${it.args}" />
                    </div>
                </g:each>
			</div>
		</div>
		<script type="text/javascript">
            function loadEvaluations(){
                $.post('/tool/evaluations',
                        {'production_unit_id':  $('#production_unit_id').val()},
                        function( data ) {
                            $('#evaluations_form').html(data);
                            $('#evaluations_form table').bootstrapTable()
                            $('#new_evaluation').prop('disabled', false);
                        }
                );
            }

			if($('#production_unit_id').val()!=null){
                loadEvaluations();
			}
			$('#production_unit_id').change( function(){
                loadEvaluations();
			});
		</script>
	</body>
</html>