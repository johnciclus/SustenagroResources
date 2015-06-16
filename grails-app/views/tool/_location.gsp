<h5 class="text-primary page-header">Cadastro da unidade produtiva</h5>

<form id="location_form" action="/tool/location" method="post">
    <div class="form-group">
        <label for="production_unit_name">Nome da unidade produtiva que será avaliada</label>
        <input id="production_unit_name" name="production_unit_name" type="text" class="form-control" placeholder="Nome">
    </div>
    <div class="form-group">
        <label for="production_unit_microregion">Microrregião da unidade produtiva</label>
        <input id="production_unit_microregion" name="production_unit_microregion" type="text" class="form-control" placeholder="Microrregião">
    </div>
    <div class="form-group">
        <label for="production_unit_location">Endereço da unidade produtiva</label>
        <input id="production_unit_location" name="production_unit_location" type="text" class="form-control" placeholder="Localização">
    </div>

    <p>Por favor identifique a localização da sua lavoura no seguinte mapa:</p>

    <g:img dir='images' file='agriculture.jpg' class="img-centered" width="600" />

    <h5 class="text-primary page-header">Microrregião</h5>

    <p>As coordenadas correspondem à microrregião: <a href="#">Microrregião 342</a></p>

    <div class="form-group">
        <input type="submit" class="btn btn-primary" value="Proximo" />
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
</script>