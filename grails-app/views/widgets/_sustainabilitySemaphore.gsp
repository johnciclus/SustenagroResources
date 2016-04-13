<div class="section">
    <p><strong>Semáforo da sustentabilidade</strong></p>
    <p>Índice da sustentabilidade: <%=value%></p>

    <div id="semaphore">

    </div>
    <script src="/assets/semaphore.js" type="text/javascript"></script>
    <script type="text/javascript">
        var result = Semaphore("#semaphore", {value: <%=value%>, label: "<%=label%>", range: <%=range%>});
    </script>
</div>