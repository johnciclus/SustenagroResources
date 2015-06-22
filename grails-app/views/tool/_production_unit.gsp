<!--<h5 class="text-primary page-header">Selecionar unidade produtiva</h5>

<select class="form-control">
</select>-->

<h5 class="text-primary page-header">Cadastrar nova unidade produtiva</h5>

<form id="location_form" action="/tool/location" method="post">
    <div class="form-group">
        <label for="production_unit_name">Nome da unidade produtiva que será avaliada</label>
        <input id="production_unit_name" name="production_unit_name" type="text" class="form-control" placeholder="Nome">
    </div>
    <div class="form-group">
        <label for="production_unit_microregion">Microrregião da unidade produtiva</label>
        <select id="production_unit_microregion" name="production_unit_microregion" class="form-control">
            <g:each in="${microregion_names}">
                <option value="${it}">${it}</option>
            </g:each>
        </select>
    </div>
    <div class="form-group">
        <label>Culturas disponiveis</label>
        <p>Temos disponíveis as seguintes culturas para analisar os indicadores de sustentabilidade:</p>

        <table class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>Cultura</th>
                <th>Seleção</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${culture_names}">
                <tr>
                    <td>${it}</td>
                    <td>
                        <input type="radio" name="production_unit_culture" value="${it}">
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div class="form-group">
        <label>Tecnologias disponiveis</label>
        <p>Temos disponíveis as seguintes tecnologias para caracterizar os sistemas de produção de cana-deaçúcar no Centro-Sul do Brasil:</p>

        <table class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>Tecnologia</th>
                <th>Descrição</th>
                <th>Seleção</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${technologies}">
            <tr>
                <td>${it.name}</td>
                <td>${it.description}</td>
                <td>
                    <input type="radio" name="production_unit_technology" value="${it.id}" >
                </td>
            </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div class="form-group">
        <label>Caracterização dos sistemas produtivos no Centro-Sul</label>
        <p>Temos disponíveis as seguintes caraterizações:</p>

        <table class="table table-striped table-hover ">
            <thead>
            <tr>
                <th>Caracterização</th>
                <th>Seleção</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${production_units}">
            <tr>
                <td>${it}</td>
                <td>
                    <input type="radio" name="production_unit_type_${it}" id="production_unit_type_${it}" value="${it}">
                </td>
            </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <!--<div class="form-group">
        <label for="production_unit_location">Endereço da unidade produtiva</label>
        <input id="production_unit_location" name="production_unit_location" type="text" class="form-control" placeholder="Localização" readonly>
    </div>
    <div class="form-group">
        <p>Por favor identifique a localização da sua lavoura no seguinte mapa:</p>
        <div id="map-canvas"></div>
    </div>-->

    <div class="form-group">
        <input type="submit" class="btn btn-primary" value="Cadastrar" />
    </div>
</form>

<script type="application/javascript">
    $( "#location_form" ).submit(function( event ) {
        $.post(
            $(this).attr('action'),
            $(this).serialize(),
            function( data ) {
                $('#content').html(data);
            }
        );
        event.preventDefault();
    });

    function initialize() {
        /*
        var mapOptions = {
            center: { lat: -23.54547, lng: -46.64340},
            zoom: 8
        };


        var map = new google.maps.Map(document.getElementById('map-canvas'),
                mapOptions);

        var marker = new google.maps.Marker({
            position: mapOptions.center,
            map: map
        })

        google.maps.event.addListener(map, 'click', function(e) {
            marker.setPosition(e.latLng);
            $("#production_unit_location").val(e.latLng);
        });*/
    }
    //google.maps.event.addDomListener(window, 'load', initialize);
</script>