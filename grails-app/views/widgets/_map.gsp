<div class="section">
    <div id="map">

    </div>
</div>
<script type="text/javascript">
    $('#map').load('<%=url%>', function(){
        $('#map svg').attr('width', '175mm');
        $('#map svg').attr('height', '125mm')
    });
</script>