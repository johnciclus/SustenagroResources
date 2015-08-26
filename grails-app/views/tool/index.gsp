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
					<h5 class="text-primary">${name}</h5>
					<div class="aling-ol">
						${raw(description)}
					</div>
				</div>

				<div class="section">
					<h5 class="text-primary">Selecionar unidade produtiva</h5>
					<form id="select_production_units" action="/tool/selectProductionUnit" method="post" class="form-horizontal" >
						<div class="form-group">
							<label for="production_unit_id" class="col-sm-4 control-label">Unidade produtiva</label>
							<div class="col-sm-8">
								<select id="production_unit_id" name="production_unit_id" class="form-control">
									<option selected disabled hidden value=''></option>
									<g:each in="${production_units}">
										<option value="${it.id}">${it.label}</option>
									</g:each>
								</select>
							</div>
						</div>
                        <div class="form-group col-sm-12 text-center">
                            <input id="new_evaluation" type="submit" class="btn btn-primary" value="Nova avaliação" disabled/>
                            <button id="list_evaluations" class="btn btn-default" type="button" data-toggle="collapse" data-target="#evaluations_form" aria-expanded="false" aria-controls="evaluations_form" disabled>Listar avaliações</button>
                        </div>
                    </form>
				</div>

				<div id="evaluations_form" class="section collapse">

				</div>

                <g:render template="create_production_unit" />
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
                            $('#list_evaluations').prop('disabled', false);
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