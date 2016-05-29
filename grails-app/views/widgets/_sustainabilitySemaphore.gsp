<div class="section">
    <p><strong><g:message code="sustainabilitySemaphore.title" /></strong></p>
    <div id="semaphoreResult">

    </div>
    <div id="semaphore">

    </div>
    <script src="/assets/semaphore.js" type="text/javascript"></script>
    <script type="text/javascript">
        function formatFloat(num){
            return Math.round(num*100)/100;
        }

        var legend = [];
        <g:each var="l" in="${legend}">
        legend.push("${l}");
        </g:each>

        var result = Semaphore("#semaphore", {value: <%=value%>, label: "<%=label%>", legend: legend, range: <%=range%>});
        $("#semaphoreResult").append("<p><%=label%>: <b>"+formatFloat(result.value)+"</b></p>");
    </script>
</div>