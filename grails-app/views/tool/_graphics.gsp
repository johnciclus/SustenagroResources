<script type="text/javascript">

    var w = 960,
            h = 960;

    var svg = d3.select("#graphics").append("svg:svg")
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