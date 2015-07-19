<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Assessment</title>
    <asset:stylesheet href="jquery.bootstrap-touchspin.min.css"/>
    <asset:javascript src="jquery.bootstrap-touchspin.min.js"/>
    <!--
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
		<script type="text/javascript" src="http://mbostock.github.com/d3/d3.js"></script>
	-->
</head>
<body>
<div class="row main">
    <div id="content" class="col-sm-10 col-sm-offset-1 content">
        <p>Unidade produtiva atual: <b>${production_unit_name}</b> </p>

        <ul id="assessment_tab" class="nav nav-tabs">
            <li role="presentation" class="active"> <a href="#indicators" aria-controls="indicators" role="tab" data-toggle="tab">          1. Indicadores</a>      </li>
            <li role="presentation">                <a href="#recomendations" aria-controls="recomendations" role="tab" data-toggle="tab">  2. Recomendações</a>    </li>
            <li role="presentation">                <a href="#graphics" aria-controls="graphics" role="tab" data-toggle="tab">              3. Gráficos</a>         </li>
        </ul>

        <div id="assessment_content" class="tab-content">
            <div role="tabpanel" class="tab-pane ind-content active" id="indicators">
                <g:render template="indicators"></g:render>
            </div>
            <div role="tabpanel" class="tab-pane ind-content" id="recomendations">
                <p>Recomendations</p>
            </div>
            <div role="tabpanel" class="tab-pane ind-content" id="graphics">
                <p>Graphics</p>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $('.pager a').click(function(){
        console.log($('.nav-tabs a[href="'+$(this).attr('href')+'"]'));
    });
</script>
</body>
</html>