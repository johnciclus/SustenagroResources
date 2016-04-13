function Semaphore(container, parameters){

    var params = parameters || {};

    var width = params['width'] || 640;
    var height= params['height'] || 240;
    var value= params['value'] || 0;
    var label= params['label'] || 'label X';
    var range = params['range'] || [-100, 100];
    var dx = (range[1] - range[0])/4;
    var margin = 40;

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
        .attr("stop-color", "#00FF00")
        .attr("stop-opacity", 1);

    function formatFloat(num){
        return Math.round(num*100)/100;
    }

    svg.append("svg:rect")
        .attr("x", margin)
        .attr("y", margin)
        .attr("width", (width - 2*margin))
        .attr("height", (height - 2*margin))
        .style("stroke", "#000")
        .style("fill", "url(#gradient)")
        .attr("fill-opacity", 0.4)
        .attr("stroke-opacity", 0.4);

    svg.append("svg:line")
        .attr("x1", margin)
        .attr("y1", height - margin)
        .attr("x2", width - margin)
        .attr("y2", height - margin)
        .style("stroke-width", "1")
        .style("stroke", "#000");

    svg.append("svg:text")
        .attr("x", width/2)
        .attr("y", height)
        .text(label);

    for(var i=0; i<5; i++) {
        svg.append("svg:text")
            .attr("x", ((width-2*margin)/4 * i) + margin)
            .attr("y", (height) - 0.5 * margin)
            .text(formatFloat(range[0] + dx * i));
    }

    var rx = (value - range[0])/(range[1] - range[0]);
    var cx = margin + (width-2*margin)*rx;

    svg.append("svg:line")
        .attr("x1", cx)
        .attr("y1", margin)
        .attr("x2", cx)
        .attr("y2", height - margin)
        .style("stroke-width", "2")
        .style("stroke", "#000");

}