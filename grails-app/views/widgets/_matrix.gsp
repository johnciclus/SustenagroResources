<div class="section">
    <div id="recomendations">

    </div>
    <div id="graphic">

    </div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js" charset="utf-8"></script>
    <script src="/assets/matrix.js" type="text/javascript"></script>
    <script type="text/javascript">
        var recomendations = [];
        <g:each var="recomendation" in="${recomendations}">
            recomendations.push("${recomendation}");
        </g:each>

        var result = Matrix("#graphic", {x: <%=x%>, y: <%=y%>, label_x: "<%=label_x%>", label_y: "<%=label_y%>", range_x: <%=range_x%>, range_y: <%=range_y%>, quadrants: ${quadrants}, recomendations: recomendations });
        $("#recomendations").append("<p>" + <g:message code="matrix.quadrant.label" /> + ": "+result.quadrant+"</p>");
        $("#recomendations").append("<p>" + <g:message code="matrix.recommendation.label" /> + ": "+result.recomendation+"</p>");
    </script>
</div>