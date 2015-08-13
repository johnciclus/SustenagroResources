function Matrix(container, parameters){

    var params = parameters || {};

    var width = params['width'] || 800;
    var height= params['height'] || 600;
    var x= params['x'] || 0;
    var y= params['y'] || 0;
    var label_x= params['label_x'] || 'label X';
    var lable_y= params['label_y'] || 'label Y';
    var rx= params['range_x'] || [-4, 4];
    var ry= params['range_y'] || [-1, 1];

    var svg = d3.select(container).append("svg:svg")
        .attr("width", width)
        .attr("height", height)
        .attr("xmlns", "http://www.w3.org/2000/svg");

    var margin = 40;

    var w_rect = (width-(2*margin))/4, h_rect = (height-(2*margin))/3;

    var colors = ["#000000", "#707070", "#A9A9A9", "#0086B2", "#319c21", "#7CFC00"]

    var dx = (rx[1] - rx[0])/4;
    var dy = (ry[1] - ry[0])/3;

    for(var i=0; i<4; i++) {
        for(var j=0; j<3; j++) {
            svg.append("svg:rect")
                .attr("x", margin + w_rect*i)
                .attr("y", margin + h_rect*j)
                .attr("width", w_rect)
                .attr("height", h_rect)
                .style("stroke", "#000")
                .style("fill", colors[i-j+2])
                .attr("fill-opacity", 0.2)
                .attr("stroke-opacity", 0.2)
        }
    }

    svg.append("svg:text")
        .attr("x", (w_rect*2) + 2*margin)
        .attr("y", (h_rect*3) + 1.5*margin)
        .text(label_x);

    svg.append("svg:text")
        .attr("x", -1*(h_rect*1.5) + margin)
        .attr("y", margin)
        .attr("transform", "rotate(270 20,40)")
        .text(lable_y);

    for(var i=0; i<=4; i++) {
        svg.append("svg:text")
            .attr("x", (w_rect * i) + margin)
            .attr("y", (h_rect * 3) + 1.5 * margin)
            .text(rx[0] + dx * i);
    }

    for(var j=0; j<=3; j++) {
        svg.append("svg:text")
            .attr("x", -1*(h_rect*j) + 0.5*margin)
            .attr("y", margin)
            .attr("transform", "rotate(270 35,40)")
            .text(ry[1] - (dy * j));
    }

    svg.append("svg:circle")
        .attr("cx", margin + w_rect*4*((x - rx[0])/(rx[1] - rx[0])) )
        .attr("cy", margin + (h_rect*3 - h_rect*3*((y - ry[0])/(ry[1] - ry[0]))))
        .attr("r", 5)
        .style("stroke-width", "1")
        .style("fill", "#000")
        .style("stroke", "#fff");
}