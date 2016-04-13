function Matrix(container, parameters){

    var params = parameters || {};

    var width = params['width'] || 640;
    var height= params['height'] || 480;
    var x= params['x'] || 0;
    var y= params['y'] || 0;
    var label_x= params['label_x'] || 'label X';
    var label_y= params['label_y'] || 'label Y';
    var rx= params['range_x'] || [-4, 4];
    var ry= params['range_y'] || [-1, 1];
    var qds= params['quadrants'] || [4, 3];
    var recomendations= params['recomendations'] || [];

    var ndx = qds[0];
    var ndy = qds[1];
    var n = (ndx+ndy-1);
    var margin = 40;
    var colors = []
    var w_rect = (width-(2*margin))/ndx, h_rect = (height-(2*margin))/ndy;
    var R = '00', G = '00', B = '00';
    var dc = 255/(n-1);

    var svg = d3.select(container).append("svg:svg")
        .attr("width", width)
        .attr("height", height)
        .attr("xmlns", "http://www.w3.org/2000/svg");

    for(var i=0; i<n; i++){
        if(i == 0 || i == (n-1)){
            G = parseInt((i*dc)).toString(16);
            R = parseInt((n-1-i)*dc).toString(16);
        }
        else{
            G = parseInt((i*dc)+dc).toString(16);
            R = parseInt(((n-1-i)*dc)+dc).toString(16);
        }
        if(G.length == 1){
            G = G+G;
        }
        if(R.length == 1){
            R = R+R;
        }
        colors.push('#'+R+G+B)
    }

    var dx = (rx[1] - rx[0])/qds[0];
    var dy = (ry[1] - ry[0])/qds[1];

    function formatFloat(num){
        return Math.round(num*100)/100;
    }

    for(var i=0; i<qds[0]; i++) {
        for(var j=0; j<qds[1]; j++) {
            svg.append("svg:rect")
                .attr("x", margin + w_rect*i)
                .attr("y", margin + h_rect*j)
                .attr("width", w_rect)
                .attr("height", h_rect)
                .style("stroke", "#000")
                .style("fill", colors[i+ndy-j-1])
                .attr("fill-opacity", 0.4)
                .attr("stroke-opacity", 0.4)
        }
    }
    svg.append("svg:line")
        .attr("x1", margin)
        .attr("y1", margin)
        .attr("x2", margin)
        .attr("y2", margin + qds[1]*h_rect)
        .style("stroke-width", "1")
        .style("stroke", "#000");

    svg.append("svg:line")
        .attr("x1", margin)
        .attr("y1", margin + qds[1]*h_rect)
        .attr("x2", margin + qds[0]*w_rect)
        .attr("y2", margin + qds[1]*h_rect)
        .style("stroke-width", "1")
        .style("stroke", "#000");

    svg.append("svg:text")
        .attr("x", (w_rect*(qds[0]/2)) + margin)
        .attr("y", (h_rect*qds[1]) + 2*margin)
        .text(label_x);

    svg.append("svg:text")
        .attr("x", -1*(1.75*h_rect) )
        .attr("y", margin)
        .attr("transform", "rotate(270 20,40)")
        .text(label_y);

    for(var i=0; i<=qds[0]; i++) {
        svg.append("svg:text")
            .attr("x", (w_rect * i) + margin)
            .attr("y", (h_rect * qds[1]) + 1.5 * margin)
            .text(formatFloat(rx[0] + dx * i));
    }

    for(var j=0; j<=qds[1]; j++) {
        svg.append("svg:text")
            .attr("x", -1*(h_rect*j) + 0.5*margin)
            .attr("y", margin)
            .attr("transform", "rotate(270 35,40)")
            .text(formatFloat(ry[1] - (dy * j)));
    }
    var rx = (x - rx[0])/(rx[1] - rx[0]);
    var ry = (y - ry[0])/(ry[1] - ry[0]);
    var cx = margin + w_rect * qds[0]*rx;
    var cy = margin + (h_rect*qds[1] - h_rect*qds[1]*ry);

    svg.append("svg:circle")
        .attr("cx", cx)
        .attr("cy", cy)
        .attr("r", 5)
        .style("stroke-width", "1")
        .style("fill", "#000")
        .style("stroke", "#fff");

    var rqx = Math.floor(qds[0]*rx);
    var rqy = Math.floor(qds[1]*ry);

    var recid = (rqy*qds[0])+rqx
    var recomendation = (recid >= 0 && recid < recomendations.length) ? recomendations[recid] : "Não tem recomendações";

    return {'quadrant': recid+1, 'recomendation': recomendation}
}