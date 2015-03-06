<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Admin</title>
	</head>
	<body>
		<div class="row main">
			<div class="col-sm-10 col-sm-offset-1 content">
				<h5 class="text-primary page-header">DSL Code</h5>				
				<form id="dsl_form" action="/sustenagro/admin/dslCreate" method="post">
					<div class="form-group">
						<label for="code" class="control-label">Code:</label>
						<textarea id="code" name="code" class="form-control"></textarea>
					</div>
					<input type="submit" class="btn btn-primary" value="generate" />
				</form>
				
				<script type="application/javascript">
					$( "#dsl_form" ).submit(function( event ) {
					  $.post(
					  	$(this).attr('action'),
					  	{code: $("#code").val()}
					  );
					  event.preventDefault();
					});
				</script>
				
			</div>
		</div>
	</body>
</html>