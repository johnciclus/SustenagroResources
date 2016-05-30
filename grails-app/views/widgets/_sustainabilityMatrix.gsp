<div class="section">
    <p><strong><g:message code="sustainabilityMatrix.title" /></strong></p>
    <div id="matrixResult">

    </div>
    <div id="matrix">

    </div>
    <script src="/assets/matrix.js" type="text/javascript"></script>
    <script type="text/javascript">
        function formatFloat(num){
            return Math.round(num*100)/100;
        }

        var recomendations = [];
        <g:each var="recomendation" in="${recomendations}">
            recomendations.push("${recomendation}");
        </g:each>

        var result = Matrix("#matrix", {x: <%=x%>, y: <%=y%>, label_x: "<%=label_x%>", label_y: "<%=label_y%>", range_x: <%=range_x%>, range_y: <%=range_y%>, quadrants: ${quadrants}, recomendations: recomendations });

        $("#matrixResult").append("<p><g:message code='sustainabilityIndex.label' />: <b>"+formatFloat(<%=x%>)+"</b></p>");
        $("#matrixResult").append("<p><g:message code='sustainabilityIndex.label' />: <b>"+formatFloat(<%=y%>)+"</b></p>");
        $("#matrixResult").append("<p><g:message code='matrix.quadrant.label' />: <b>"+result.quadrant+"</b></p>");
        $("#matrixResult").append("<p><g:message code='matrix.recommendation.label' />: <b>"+result.recomendation+"</b></p>");
    </script>
</div>