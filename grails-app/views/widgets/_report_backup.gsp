<g:each in="${report}">
    <g:if test="${it[0] == 'show'}">
        <%= it[1] %>
    </g:if>
    <g:elseif test="${it[0] == 'linebreak'}">
        <p>&nbsp;</p>
    </g:elseif>
    <g:elseif test="${it[0] == 'recommendation'}">
        <div class="section">
            <div class='text-primary'>Recomendação</div>

            <div><%= it[1]  %></div>
        </div>
    </g:elseif>
    <g:elseif test="${it[0] == 'table'}">
        <div class="section">
            <table data-toggle="table" class="table">
                <thead>
                    <tr>
                        <g:each var="col" in="${it[2]}">
                            <th data-field="${col.key}">${col.value}</th>
                        </g:each>
                    </tr>
                </thead>
                <tbody>
                <g:each status="i" var="row" in="${it[1]}">
                    <tr data-index="${i}">
                    <g:each var="col" in="${it[2]}">
                        <td>${row[col.key]}</td>
                    </g:each>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </g:elseif>
    <g:elseif test="${it[0] == 'matrix'}">
        <div class="section">
            <div id="recomendations">

            </div>
            <div id="graphic">

            </div>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js" charset="utf-8"></script>
            <script src="/assets/matrix.js" type="text/javascript"></script>
            <script type="text/javascript">
                var recomendations = [];
                <g:each var="recomendation" in="${it[8]}">
                    recomendations.push("${recomendation}");
                </g:each>

                var result = Matrix("#graphic", {x: ${it[1]}, y: ${it[2]}, label_x: "${it[3]}", label_y: "${it[4]}", range_x: ${it[5]}, range_y: ${it[6]}, quadrants: ${it[7]}, recomendations: recomendations });
                $("#recomendations").append("<p>Quadrante : "+result.quadrant+"</p>");
                $("#recomendations").append("<p>Recomendação: "+result.recomendation+"</p>");
            </script>
        </div>
    </g:elseif>
    <g:elseif test="${it[0] == 'map'}">
        <div class="section">
            <div id="map">

            </div>
        </div>
        <script type="text/javascript">
            $('#map').load('<%=it[1]%>', function(){
                $('#map svg').attr('width', '175mm');
                $('#map svg').attr('height', '125mm')
            });
        </script>
    </g:elseif>
</g:each>

<!--
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
 -->

