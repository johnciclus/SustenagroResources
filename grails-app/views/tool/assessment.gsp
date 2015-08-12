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
        <p>Unidade produtiva atual: <b>${production_unit.name}</b> </p>

        <ul id="assessment_tab" class="nav nav-tabs">
            <li role="presentation" <g:if test="${report == null}"> class="active" </g:if>> <a href="#indicators" aria-controls="indicators" role="tab" data-toggle="tab"> Indicadores</a>      </li>
            <g:if test="${report != null}">
            <li role="presentation" class="active">                <a href="#report" aria-controls="report" role="tab" data-toggle="tab">        Relatório</a>         </li>
            </g:if>
        </ul>

        <div id="assessment_content" class="tab-content">
            <div role="tabpanel" class="tab-pane ind-content <g:if test='${report == null}'>active</g:if>"  id="indicators">
                <g:render template="indicators"></g:render>
            </div>
            <g:if test="${report != null}">
            <div role="tabpanel" class="tab-pane ind-content active" id="report">
                <g:render template="report"></g:render>
                <div>
                    <nav>
                        <ul class="pager">
                            <li><a href="#indicators">Anterior</a></li>
                        </ul>
                    </nav>
                </div>
            </div>
            </g:if>
        </div>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js" charset="utf-8"></script>
<script>
    $('.pager a').click(function(){
        $('.nav-tabs a[href="'+$(this).attr('href')+'"]').tab('show');
    });

    var w = 800, h = 600, m=40;

    var svg = d3.select("#graphic").append("svg:svg")
            .attr("width", w)
            .attr("height", h)
            .attr("xmlns", "http://www.w3.org/2000/svg");

    var w_rect = (w-(2*m))/4, h_rect = (h-(2*m))/3;

    var colors = ["#000000", "#707070", "#A9A9A9", "#0086B2", "#319c21", "#7CFC00"]

    var min_x_lim = -4;
    var max_x_lim = 4;
    var min_y_lim = -1;
    var max_y_lim = 1;
    var d_x_label = (max_x_lim - min_x_lim)/4;
    var d_y_label = (max_y_lim - min_y_lim)/3;

    for(var i=0; i<4; i++) {
        for(var j=0; j<3; j++) {
            svg.append("svg:rect")
                    .attr("x", m + w_rect*i)
                    .attr("y", m + h_rect*j)
                    .attr("width", w_rect)
                    .attr("height", h_rect)
                    .style("stroke", "#000")
                    .style("fill", colors[i-j+2])
                    .attr("fill-opacity", 0.2)
                    .attr("stroke-opacity", 0.2)
        }
    }

    svg.append("svg:text")
            .attr("x", (w_rect*2) + 2*m)
            .attr("y", (h_rect*3) + 1.5*m)
            .text("Indice de Magnitude");

    svg.append("svg:text")
            .attr("x", -1*(h_rect*1.5) + m)
            .attr("y", m)
            .attr("transform", "rotate(270 20,40)")
            .text("Indice de Segurança");

    for(var i=0; i<=4; i++) {
        svg.append("svg:text")
                .attr("x", (w_rect * i) + m)
                .attr("y", (h_rect * 3) + 1.5 * m)
                .text(min_x_lim + d_x_label * i);
    }

    for(var j=0; j<=3; j++) {
        svg.append("svg:text")
                .attr("x", -1*(h_rect*j) + 0.5*m)
                .attr("y", m)
                .attr("transform", "rotate(270 35,40)")
                .text(max_y_lim - d_y_label * j);
    }





    /*
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
    */


</script>
</body>
</html>