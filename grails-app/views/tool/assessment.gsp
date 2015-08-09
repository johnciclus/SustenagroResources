<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>SustenAgro - Tool - Assessment</title>
    <asset:stylesheet href="jquery.bootstrap-touchspin.min.css"/>
    <asset:javascript src="jquery.bootstrap-touchspin.min.js"/>
</head>
<body>
<div class="row main">
    <div id="content" class="col-sm-10 col-sm-offset-1 content">
        <p>Unidade produtiva atual: <b>${production_unit_name}</b> </p>

        <ul id="assessment_tab" class="nav nav-tabs">
            <li role="presentation" class="active"> <a href="#indicators" aria-controls="indicators" role="tab" data-toggle="tab">  Indicadores</a>      </li>
            <li role="presentation">                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">          Relatório</a>         </li>
        </ul>

        <div id="assessment_content" class="tab-content">
            <div role="tabpanel" class="tab-pane ind-content active" id="indicators">
                <g:render template="indicators"></g:render>
            </div>
            <div role="tabpanel" class="tab-pane ind-content" id="report">

                <g:render template="report"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#indicators">Anterior</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js" charset="utf-8"></script>
<script>
    $('.pager a').click(function(){
        $('.nav-tabs a[href="'+$(this).attr('href')+'"]').tab('show');
    });

    var w = 960, h = 960;

    var svg = d3.select("#graphic").append("svg:svg")
            .attr("width", w)
            .attr("height", h);

    svg.append("svg:circle")
            .attr("cx", w/3)
            .attr("cy", h/3)
            .attr("r", 250)
            .style("fill", "green")
            .style("fill-opacity", ".5");

    svg.append("svg:circle")
            .attr("cx", w*2/3)
            .attr("cy", h/3)
            .attr("r", 250)
            .style("fill", "steelblue")
            .style("fill-opacity", ".5");

    svg.append("svg:circle")
            .attr("cx", w/2)
            .attr("cy", h*2/3)
            .attr("r", 250)
            .style("fill", "darkred")
            .style("fill-opacity", ".5");
</script>
</body>
</html>