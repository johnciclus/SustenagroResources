<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Tool</title>
        <!--
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript" src="http://mbostock.github.com/d3/d3.js"></script>
        -->
	</head>
	<body>
		<div class="row main">
			<div id="content" class="col-sm-10 col-sm-offset-1 content">
                <h5 class="text-primary page-header">${name}</h5>
                <div class="aling-ol">
                    ${raw(description)}
                </div>

                <g:render template="production_unit" />
			</div>
		</div>
	</body>
</html>