<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Tool</title>
	</head>
	<body>
		<div class="row main">
			<div class="col-sm-10 col-sm-offset-1 content">
				
				<ul class="nav nav-tabs">
				  <li class="active">		<a href="#description" data-toggle="tab">				Descrição</a></li>
				  <li class="">					<a href="#location" data-toggle="tab">					1. Localização</a></li>
				  <li class="">					<a href="#crop" data-toggle="tab">							2. Cultura</a></li>
				  <li class="">					<a href="#technology" data-toggle="tab">				3. Tecnologia</a></li>
				  <li class="">					<a href="#characterization" data-toggle="tab">	4. Caracterização</a></li>
				  <li class="">					<a href="#indicators" data-toggle="tab">				5. Indicadores</a></li>
				  <li class="">					<a href="#recomendations" data-toggle="tab">		6. Recomendações</a></li>
				</ul>
				<div id="toolContent" class="tab-content">
				  <div class="tab-pane fade active in" id="description">
				    <h5 class="text-primary page-header">Avaliação da sustentabilidade em agricultura</h5>
						<p>O processo de avaliação da sustentabilidade esta composto pelas seguintes etapas:</p>
						<ol>
							<li>Localização da laovura</li>
							<li>Definição da cultura</li>
							<li>Definição da tecnologia</li>
							<li>Caracterização dos sistemas produtivos</li>
							<li>Definição dos indicadores</li>
							<li>Recomendações de sustentabilidade</li>
						</ol>
				  </div>
				  <div class="tab-pane fade" id="location">
				    <h5 class="text-primary page-header">Localização</h5> 
						<p>Por favor identifique a localização da sua lavoura no seguinte mapa:</p>
						
						<g:img dir='images' file='agriculture.jpg' class="img-centered" width="600" />
						
						<h5 class="text-primary page-header">Microrregião</h5>
						<p>As coordenadas correspondem à microrregião: <a href="#">Microrregião 342</a></p>
						
						<div class="text-center">
							<a href="#" class="btn btn-primary">Seguinte</a>	
						</div>
						
				  </div>
				  <div class="tab-pane fade" id="crop">
				    <h5 class="text-primary page-header">Culturas disponiveis</h5>
				    <p>Temos disponíveis as seguintes culturas para analisar os indicadores de sustentabilidade:</p>
				    
				    <table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Cultura</th>
						      <th>Clima da microrregião</th>
						      <th>Seleção</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Cana de açucar</td>
						      <td>Tropical</td>
						      <td>
						     		<input type="radio" name="cropSelection" id="cropSelection1" value="option1" checked="">
                  </td>
						    </tr>
						    <tr>
						      <td>2</td>
						      <td>Soja</td>
						      <td>Subtropical</td>
						      <td>
						      	<input type="radio" name="cropSelection" id="cropSelection2" value="option2" >
						      </td>
						    </tr>
						    <tr>
						      <td>3</td>
						      <td>Milho</td>
						      <td>Semiárido</td>
						      <td>
						      	<input type="radio" name="cropSelection" id="cropSelection3" value="option3" >
						      </td>
						    </tr>
						  </tbody>
						</table> 
				    
				    <p>A cultura seleccionada é: <a href="#">Cana de açucar</a></p>
				    <div class="text-center">
							<a href="#" class="btn btn-primary">Seguinte</a>	
						</div>
				    
				  </div>
				  <div class="tab-pane fade" id="technology">
				    <h5 class="text-primary page-header">Tecnologias disponiveis</h5>
				    <p>Temos disponíveis as seguintes tecnologias para caracterizar os sistemas de produção de cana-deaçúcar no Centro-Sul do Brasil:</p>
				    
				    <table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Tecnologia</th>
						      <th>Descrição</th>
						      <th>Seleção</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Baixa eficiência tecnológica</td>
						      <td>Semimecanizado no plantio</td>
						      <td>
						     		<input type="radio" name="techSelection" id="cropSelection1" value="option1" >
                  </td>
						    </tr>
						    <tr>
						      <td>2</td>
						      <td>Media eficiência tecnológica</td>
						      <td>Mecanização agrícola no plantio e não na colheita</td>
						      <td>
						      	<input type="radio" name="techSelection" id="cropSelection2" value="option2" >
						      </td>
						    </tr>
						    <tr>
						      <td>3</td>
						      <td>Alta eficiência tecnológica</td>
						      <td>Mecanização no plantio e colheita</td>
						      <td>
						      	<input type="radio" name="techSelection" id="cropSelection3" value="option3" checked="">
						      </td>
						    </tr>
						  </tbody>
						</table> 
				    
				    <p>A tecnologia seleccionada é: <a href="#">Alta tecnologia</a></p>
				    <div class="text-center">
							<a href="#" class="btn btn-primary">Seguinte</a>	
						</div>
				  </div>
				  <div class="tab-pane fade" id="characterization">
				    <h5 class="text-primary page-header">Caracterização dos sistemas produtivos no Centro-Sul</h5>
				    <p>Temos disponíveis as seguintes caraterizações:</p>
				    
				    <table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Caracterização</th>
						      <th>Seleção</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Fornecedor</td>
						      <td>
						     		<input type="radio" name="chaSelection" id="cropSelection1" value="option1" checked="">
                  </td>
						    </tr>
						    <tr>
						      <td>2</td>
						      <td>Usina</td>
						      <td>
						      	<input type="radio" name="chaSelection" id="cropSelection2" value="option2" >
						      </td>
						    </tr>
						  </tbody>
						</table> 
				    
				    <p>A tecnologia seleccionada é: <a href="#">Alta tecnologia</a></p>
				    <div class="text-center">
							<a href="#" class="btn btn-primary">Seguinte</a>	
						</div>
				  </div>
				  <div class="tab-pane fade" id="indicators">
				    <div class="btn-group btn-group-justified">
						  <a href="#" class="btn btn-primary">Ambientais</a>
						  <a href="#" class="btn btn-primary">Econômicos</a>
						  <a href="#" class="btn btn-primary">Sociais</a>
						</div>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Indicador</th>
						      <th>Descrição</th>
						      <th>Valor</th>
						      <th>Unidades</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Indicador 1</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
                  <td>Lorem Ipsum</td>
						    </tr>
						    <tr>
						      <td>2</td>
						      <td>Indicador 2</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td><input type="text" class="form-control"  id="idicador_2"></td>
                  <td>Lorem Ipsum</td>
						    </tr>
						    <tr>
						      <td>3</td>
						      <td>Indicador 3</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td><input type="text" class="form-control"  id="idicador_3"></td>
                  <td>Lorem Ipsum</td>
						    </tr>
						    <tr>
						      <td>4</td>
						      <td>Indicador 4</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td><input type="text" class="form-control"  id="idicador_4"></td>
                  <td>Lorem Ipsum</td>
						    </tr>
						    <tr>
						      <td>5</td>
						      <td>Indicador 5</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td><input type="text" class="form-control"  id="idicador_5"></td>
                  <td>Lorem Ipsum</td>
						    </tr>
						    <tr>
						      <td>6</td>
						      <td><input type="text" class="form-control"  id="idicador_5"></td>
						      <td><input type="text" class="form-control"  id="idicador_5"></td>
						      <td><input type="text" class="form-control"  id="idicador_5"></td>
                  <td><input type="text" class="form-control"  id="idicador_5"></td>
						    </tr>
						  </tbody>
						</table> 
				    
				    <div class="text-center">
							<a href="#" class="btn btn-primary">Seguinte</a>	
						</div>
				  </div>
				  <div class="tab-pane fade" id="recomendations">
				    <div class="btn-group btn-group-justified">
						  <a href="#" class="btn btn-primary">Ambientais</a>
						  <a href="#" class="btn btn-primary">Econômicos</a>
						  <a href="#" class="btn btn-primary">Sociais</a>
						</div>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Descrição</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						    </tr>
						    <tr>
						      <td>2</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						    </tr>
						    <tr>
						      <td>3</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						    </tr>
						    <tr>
						      <td>4</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						    </tr>
						    <tr>
						      <td>5</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						    </tr>
						  </tbody>
						</table> 
				  </div>
				</div>
			</div>
		</div>
	</body>
</html>