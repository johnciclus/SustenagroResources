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
				  <li class="">					<a href="#characterization" data-toggle="tab">	2. Caracterização</a></li>
				  <li class="">					<a href="#indicators" data-toggle="tab">				3. Indicadores</a></li>
				  <li class="">					<a href="#recomendations" data-toggle="tab">		4. Recomendações</a></li>
				</ul>
				<div id="toolContent" class="tab-content">
				  <div class="tab-pane fade active in" id="description">
				    <h5 class="text-primary page-header">Avaliação da sustentabilidade em agricultura</h5>
						<p>O processo de avaliação da sustentabilidade esta composto pelas seguintes etapas:</p>
						<ol>
							<li>Localização da laovura</li>
							<li>Caracterização da cultura, tecnologia e tipo de sistema produtivo</li>
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
				  <div class="tab-pane fade" id="characterization">
				    <h5 class="text-primary page-header">Culturas disponiveis</h5>
				    <p>Temos disponíveis as seguintes culturas para analisar os indicadores de sustentabilidade:</p>
				    
				    <table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Cultura</th>
						      <th>Seleção</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Cana de açucar</td>
						      <td>
						     		<input type="radio" name="cropSelection" id="cropSelection1" value="option1" checked="">
                  </td>
						    </tr>
						    <tr>
						      <td>2</td>
						      <td>Soja</td>
						      <td>
						      	<input type="radio" name="cropSelection" id="cropSelection2" value="option2" >
						      </td>
						    </tr>
						    <tr>
						      <td>3</td>
						      <td>Milho</td>
						      <td>
						      	<input type="radio" name="cropSelection" id="cropSelection3" value="option3" >
						      </td>
						    </tr>
						  </tbody>
						</table> 
				    
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
				    
				    <h5 class="text-primary page-header">Resumo</h5>
				    
				    <p>A cultura seleccionada é: <a href="#">Cana de açucar</a></p>
				    
				    <p>A tecnologia seleccionada é: <a href="#">Alta tecnologia</a></p>
				    
				    <p>A caracterização seleccionada é: <a href="#">Fornecedor</a></p>
				    <div class="text-center">
							<a href="#" class="btn btn-primary">Seguinte</a>	
						</div>
				  </div>
				  <div class="tab-pane fade" id="indicators">
				    
						<div class="alert alert-dismissable alert-info">
						  <button type="button" class="close" data-dismiss="alert">×</button>
						  <strong>Indicadores!</strong> Por favor preencher as três dimensões dos indicadores, ambiental, econômica e social.
						</div>
						
						<div class="btn-group btn-group-justified">
						  <a href="#" class="btn btn-primary">Ambientais</a>
						  <a href="#" class="btn btn-default">Econômicos</a>
						  <a href="#" class="btn btn-default">Sociais</a>
						</div>
									    
				    <table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>1</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Quantidade de vinhaça/área aplicada com relação ao Potássio (K) e Nitrogênio (N)</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>A concentração máxima de potássio no solo não poderá exceder 5% da Capacidade de Troca Catiônica – CTC. Quando esse limite for atingido, a aplicação de vinhaça ficará restrita à reposição desse nutriente em função da extração média pela cultura, que é de 185 kg de K 2 O por hectare por corte (PIRES; FERREIRA, 2008). Quanto aos nutrientes extraídos (requeridos) pela cultura de cana-de-açúcar, o nitrogênio é o mais importante. No plantio da cana é necessário aplicar 30 kg/ha de nitrogênio; já na adubação da cana-soca, a quantidade recomendada para a cultura é de 60 kg/ha (SOUZA; LOBATO, 2004).</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td>vinhaça/área</td>
						    </tr>
						  </tbody>
						</table>
						
						<div class="text-center">
							<ul class="pagination">
							  <li class="disabled"><a href="#">«</a></li>
							  <li class="active"><a href="#">1</a></li>
							  <li><a href="#">2</a></li>
							  <li><a href="#">3</a></li>
							  <li><a href="#">4</a></li>
							  <li><a href="#">5</a></li>
							  <li><a href="#">»</a></li>
							</ul>
						</div>
						<!--
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>2</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Quantificação da erosão potencial segundo a Equação Universal de Perda de Solo (USLE – Universal Soil Loss Equation).</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>Existem 5 classes de erosão com relação a Perda de Solo (PS): 
											Muito baixa/baixa (PS < 5): Mais sustentável (+1);
											Moderada (PS 5 – 10): Sustentável (0);
											Alta/Severa (PS>10): Menos sustentável (-1).
											A USLE é representada por:
											PS = R x K x L x S x C x P,
											onde PS é a perda de solo média anual ou Erosão Específica (ton/ha -1 ano -1 ), R é o
											Fator climático ou Fator erosividade da chuva (MJ mm ha -1 h -1 ), K é o Fator deErodibilidade do Solo (t ha -1 /MJ ha -1 mm h -1 ), L é o Comprimento da Encosta, S é o
											Fator de Declividade de Encosta, C é a Cobertura Vegetal ou Fator de uso e manejo
											do Solo e P é o Fator Antrópico ou Práticas de Conservação.</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>3</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Balanço de Carbono (C) e Nitrogênio (N) no solo.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>Em condições tropicais, são requeridas cerca de 7 e
											10 mg ha -1 ano -1 de resíduos com elevada e baixa relação C:N, respectivamente,
											para manter o teor de C orgânico total no solo em 1 dag kg -1 (LEITE et al., 2003;
											MANFOGOYA et al., 1997).</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>4</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td> Compactação do solo</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>Os sintomas visuais mais freqüentes notados em
											plantas cultivadas em solos compactados são: 1) emergência lenta da plântula; 2)
											plantas com tamanhos variados, tendo mais plantas pequenas que normais; 3)
											plantas de coloração deficiente; 4) sistema radicular raso; e 5) raízes malformadas
											com maior incidência de pelos absorventes. Quanto aos sintomas no solo, podem
											ser citados os seguintes: 1) formação de crosta superficial; 2) fendas nas marcas
											das rodas do trator; 3) zonas compactadas de subsuperfície; 4) empoçamento de
											água; 5) erosão excessiva pela água; 6) aumento da necessidade de potência de
											máquinas para cultivos; e 7) presença de restos de resíduos não decompostos
											meses após a incorporação (CAMARGO; ALLEONI, 2006).
											Outro modo de aferição é através da resistência à penetração. Canarache (1990)
											sugere que valores acima de 2,5 MPa começam a restringir o pleno crescimento das
											raízes das plantas; já Sene et al. (1985) consideram críticos os valores que variam
											de 6,0 a 7,0 MPa para solos arenosos e em torno de 2,5 MPa para solos argilosos
											(CAMARGO; ALLEONI, 2006).</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>5</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Balanço de gases como CO, HC, NOX e material particulado em veículos pesados.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>É preciso considerar também os gases provenientes da utilização de
											combustíveis fósseis. Estes são oriundos da utilização de máquinas como tratores,
											caminhões e colheitadeiras utilizados no setor sucroalcooleiro. Os gases mais
											nocivos ao ambiente são CO (monóxido de carbono), CO 2 (dióxido de carbono ou
											gás carbono), NO X (óxido de nitrogênio), SO 2 (dióxido de enxofre) e material
											particulado (ALVARENGA; QUEIROZ, 2009).
											</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>6</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Ocorrência de queimada de palha no campo.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>Segundo Leme (2005), haveria redução de 36% na emissão de gases
											do efeito estufa (GEE) se a palha fosse queimada nas caldeiras das usinas e
											destilarias, ao invés de ser queimada no campo. No mesmo estudo, calculou em
											5,94 KgCO 2 eq/tc a taxa dessa redução (ANDRADE; DINIZ, 2007).</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>7</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Emissão de Ozônio.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>Além do gás carbônico acumulado e liberado à atmosfera, outros
											gases também são formados e lançados. Dentre esses se cita o ozônio, um gás que
											não se dissipa facilmente em baixa altitude e que é extremamente poluente. Como
											impacto negativo ao ambiente, tal gás prejudica o crescimento de plantas e o
											desenvolvimento de seres vivos (PIACENTE, 2005). Dados do INPE indicam que a
											emissão de ozônio chega a duplicar nas épocas de queimadas, atingindo
											concentrações inadequadas. Tais episódios ocorreram em dias quentes e secos,
											nos meses de setembro e outubro, propícios à formação de ozônio. Deve-se
											destacar que, nessa época, as queimadas são fontes de óxidos de nitrogênio (NO x )
											precursores de ozônio (O 3 ) e, portanto, podem ter influenciado nas ultrapassagens
											observadas (ANDRADE; DINIZ, 2007).
											</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>8</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Emissão e suspensão de microparticulas (fuligem).</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>A fuligem espalha-se pelas cidades causando incômodo às
											populações, pela sujeira que deixa nas residências. Parece também que as
											partículas respiráveis da fuligem em muito contribuem para aumentar a incidência de
											doenças respiratórias que atingem, principalmente, as crianças e os idosos durante
											o período da safra (SCOPINHO, 1999; FRANCO, 1992).
											</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>9</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Emissão e suspensão de microparticulas (fuligem).</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>- Mais de 1000 metros de centros urbanos 4 : mais sustentável (+1);
											- Menos de 1000 metros de centros urbanos: menos sustentável (-1).</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>11</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Localização geográfica da cultura em relação à aptidão edáfica.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>1) Fertilidade natural alta (solos eutróficos), profundidade favorável e ausência de
											pedregosidade (Latossolos, Argissolos, Luvissolos, Nitossolos, Cambissolos e
											Neossolos quartzarênicos): Mais sustentável (+1);
											2) Fertilidade natural média (solos distróficos), e/ou profundidade desfavorável
											(Neossolos litólicos e Plintossolos): Sustentável (0);
											3) Fertilidade natural baixa (solos ácricos, álicos, alumínicos e alíticos) e/ou solos
											com grande limitação física ao crescimento radicular em profundidade: Menos
											Sustentável (-1).
											</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>12</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Localização geográfica da cultura em relação à aptidão edafoclimática.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>Planejamento prévio para o uso e ocupação do solo.
										Justificativa: A caracterização climática sob o ponto de vista espacial e temporal,
										aliada aos detalhes de fertilidade e manejo do solo, serão os atributos básicos para
										a quantificação edafoclimática e a determinação das regiões aptas ao cultivo de
										culturas de interesse comercial ou subsistência às populações (CIIAGRO, 2009).
									</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>13</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Áreas de Preservação Permanente (AAP) recuperadas/conservadas.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>- Mais do que 70% recuperadas/conservadas: Mais sustentável (+1);
											- Menos do que 70% recuperadas/conservadas: Menos sustentáveis (-1).

									</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>14</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Comprovação de Averbação da área de Reserva Legal.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>- Anexação de documentos de comprovação da averbação em cartório: mais sustentável (+1);
											- Ausência de documentos de comprovação: menos sustentável (-1).
									</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>15</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Número de autuações nos últimos anos.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>- Até 100 autuações em 16 meses: mais sustentável (+1);
											- Mais que 100 autuações em 16 meses: menos sustentável (-1).
									</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>16</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Cumprimento com os Termos de Compromisso de Recuperação Ambiental (TCRAs).</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>- Ausência de passivo ambiental: mais sustentável (+1);
											- Existência de passivo ambiental e em processo de cumprimento: Menos
											sustentável (0);
											- Presença de 1 ou mais passivos ambientais: não sustentável (-1).

									</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						  	<tr>
						  		<td class="text-primary">#</td>
						      <td>17</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td>Localização geográfica da cultura em relação à aptidão agroclimática.</td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td>A) Temperatura média anual superior a 21oC, deficiência hídrica anual superior a 10
											e inferior a 250 mm e índice hídrico entre 60 e superior a -20: Mais sustentável (+1);
											B) Temperatura média anual entre 20 e 21oC, deficiência hídrica anual entre 5 e 10
											mm e índice hídrico entre 60 e 80: Sustentável (0);
											C) Temperatura média anual de 20oC, deficiência hídrica anual inferior a 5 mm e
											índice hídrico anual superior a 80: Menos Sustentável (-1).
											</td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control" id="idicador_1"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td></td>
						    </tr>
						  </tbody>
						</table>
						-->
						<h5 class="text-primary page-header">Cadastrar novo indicador</h5>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td class="text-primary">Nome</td>
						      <td><input type="text" class="form-control"  id="indicator_name"></td>
						    </tr>
						    <tr>
						      <td class="text-primary">Descrição</td>
						      <td><input type="text" class="form-control"  id="indicator_description"></td>
						    </tr>
						    <tr>
						  		<td class="text-primary">Valor</td>
						      <td><input type="text" class="form-control"  id="indicator_value"></td>
						    </tr>
						    <tr>
						    	<td class="text-primary">Unidades</td>
                  <td><input type="text" class="form-control"  id="indicator_units"></td>
						    </tr>
						    <tr >
						    	<td colspan="2" class="text-center"><a href="#" class="btn btn-primary">Cadastrar</a></td>
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
						  <a href="#" class="btn btn-default">Econômicos</a>
						  <a href="#" class="btn btn-default">Sociais</a>
						</div>
						
						<table class="table table-striped table-hover ">
						  <thead>
						    <tr class="text-primary">
						      <th>#</th>
						      <th>Descrição</th>
						      <th>Importância</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						      <td>1</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td>Baixa</td>
						    </tr>
						    <tr class="warning">
						      <td>2</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td>Meia</td>
						    </tr>
						    <tr class="danger">
						      <td>3</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td>Alta</td>
						    </tr>
						    <tr class="warning">
						      <td>4</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td>Meia</td>
						    </tr>
						    <tr>
						      <td>5</td>
						      <td>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book</td>
						      <td>Baixa</td>
						    </tr>
						  </tbody>
						</table> 
				  </div>
				</div>
			</div>
		</div>
	</body>
</html>