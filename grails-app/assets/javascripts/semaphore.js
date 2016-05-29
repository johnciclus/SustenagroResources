function Semaphore(container, parameters){

    var params = parameters || {};

    var width = params['width'] || 720;
    var height= params['height'] || 480;
    var margin = 40;
    var semHeight = 320;
    var semWidth = width - 2*margin;
    var value= params['value'] || 0;
    var label= params['label'] || 'label';
    var range = params['range'] || [-100, 100];
    var legend = params['legend'] || [];
    var dx = [-100, -60, -20, 20, 60, 100];
    var n = dx.length;
    var x = (width-(2*margin))/2 + margin;
    var y = (semHeight) - 0.5 * margin;
    var w, h;

    function formatFloat(num){
        return Math.round(num*100)/100;
    }

    var color = d3.scale.linear()
        .domain([0, 0.5, 1])
        .range(["#FF0000", "#FFFF00", "#008000"]);

    var svg = d3.select(container).append("svg:svg")
        .attr("width", width)
        .attr("height", height)
        .attr("xmlns", "http://www.w3.org/2000/svg");

    var gradient = svg.append("defs")
        .append("linearGradient")
        .attr("id", "gradient")
        .attr("x1", "0%")
        .attr("y1", "0%")
        .attr("x2", "100%")
        .attr("y2", "0%")
        .attr("spreadMethod", "pad");

    gradient.append("stop")
        .attr("offset", "0%")
        .attr("stop-color", "#FF0000")
        .attr("stop-opacity", 1);

    gradient.append("stop")
        .attr("offset", "50%")
        .attr("stop-color", "#FFFF00")
        .attr("stop-opacity", 1);

    gradient.append("stop")
        .attr("offset", "100%")
        .attr("stop-color", "#008000")
        .attr("stop-opacity", 1);

    svg.append("svg:rect")
        .attr("x", margin)
        .attr("y", margin + 70)
        .attr("width", (width - 2*margin))
        .attr("height", (semHeight - 2*margin - 140))
        .style("stroke", "#000")
        .style("fill", "url(#gradient)")
        .attr("fill-opacity", 0.75)
        .attr("stroke-opacity", 0.4);

    svg.append("svg:line")
        .attr("x1", margin)
        .attr("y1", semHeight - margin)
        .attr("x2", width - margin)
        .attr("y2", semHeight - margin)
        .style("stroke-width", "1")
        .style("stroke", "#888");

    svg.append("svg:line")
        .attr("x1", margin)
        .attr("y1", margin)
        .attr("x2", width - margin)
        .attr("y2", margin)
        .style("stroke-width", "1")
        .style("stroke", "#888");

    svg.append("svg:text")
        .attr("x", width/2 )
        .attr("y", semHeight + 10)
        .attr("text-anchor", "middle")
        .text(label);

    svg.append("svg:text")
        .attr("x", x)
        .attr("y", y)
        .attr("text-anchor", "middle")
        .text(formatFloat(0));

    svg.append("svg:line")
        .attr("x1", x)
        .attr("y1", margin)
        .attr("x2", x)
        .attr("y2", semHeight - margin)
        .style("stroke-width", "1")
        .style("stroke", "#888");

    for(var i=0; i<n; i++) {
        x = (width-(2*margin))*((dx[i]+100)/200) + margin;

        svg.append("svg:text")
            .attr("x", x)
            .attr("y", y)
            .attr("text-anchor", "middle")
            .text(formatFloat(dx[i]));

        svg.append("svg:line")
            .attr("x1", x)
            .attr("y1", margin)
            .attr("x2", x)
            .attr("y2", semHeight - margin)
            .style("stroke-width", "1")
            .style("stroke", "#888");

        if(i != (n-1)){
            w = semWidth/5
            svg.append("svg:rect")
                .attr("x", margin + i*w)
                .attr("y", margin + semHeight)
                .attr("width", w)
                .attr("height", 40)
                .style("stroke", "#888")
                .style("fill", color(i/4))
                .attr("fill-opacity", 0.6);

            svg.append("svg:rect")
                .attr("x", margin + i*w)
                .attr("y", margin + semHeight + 40)
                .attr("width", w)
                .attr("height", 40)
                .style("stroke", "#888")
                .style("fill", "#FFF")
                .attr("fill-opacity", 0.6);

            svg.append("svg:text")
                .attr("x", margin + i*w + w/2)
                .attr("y", margin + semHeight + 25)
                .attr("text-anchor", "middle")
                .text(dx[i]+" à "+dx[i+1]);

            svg.append("svg:text")
                .attr("x", margin + i*w + w/2)
                .attr("y", margin + semHeight + 65)
                .attr("font-size","15px")
                .attr("text-anchor", "middle")
                .text(legend[i]);
        }
    }

    value = formatFloat((value - range[0])/(range[1] - range[0]));
    x = (width-2*margin)*value + margin;

    svg.append("svg:rect")
        .attr("x", x-2)
        .attr("y", margin)
        .attr("width", 4)
        .attr("height", semHeight - 2*margin)
        .style("stroke-width", "1")
        .style("stroke", "#888")
        .style("fill", "#000");

    value = (value * 200) - 100;

    return {'value': value}
}