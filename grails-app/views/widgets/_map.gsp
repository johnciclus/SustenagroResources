<g:if test="${map}">
    <div class="section">
        <div id="map">

        </div>
    </div>
    <script type="text/javascript">
        //https://commons.wikimedia.org/wiki/Commons:FAQ#What_are_the_strangely_named_components_in_file_paths.3F
        var id = '<%=map%>';
        var name = id.split(' ').join('_');
        var code = md5(name);
        $('#map').load('https://upload.wikimedia.org/wikipedia/commons/'+code[0]+'/'+code.substring(0,2)+'/'+name, function(){
            $('#map svg').attr('width', '175mm');
            $('#map svg').attr('height', '125mm');
            $('#map').prepend("<p><%=label%></p>");
        });

    </script>
</g:if>