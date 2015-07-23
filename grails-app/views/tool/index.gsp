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

				<g:render template="list_production_unit" />

                <g:render template="create_production_unit" />
			</div>
		</div>
	</body>
</html>