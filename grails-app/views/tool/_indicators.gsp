<ul id="indicator_tabs" class="nav nav-tabs">
    <li role="presentation" class="active"><a href="#environmental_indicators">Ambientais</a></li>
    <li role="presentation"><a href="#economic_indicators">Econômicos</a></li>
    <li role="presentation"><a href="#social_indicators">Sociais</a></li>
</ul>
<div id="tabs_content" class="tab-content">
    <div role="tabpanel" class="tab-pane indicators active" id="environmental_indicators">
        <g:each var="indicator" in="${environmental_indicators}">
            <div class="page-header">
                <b>${indicator.title}</b>
            </div>

            <g:if test="${indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Categorical'}">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
            </g:if>
        </g:each>
    </div>
    <div role="tabpanel" class="tab-pane indicators" id="economic_indicators">
        <g:each var="indicator" in="${economic_indicators}">
            <div class="page-header">
                <b>${indicator.title}</b>
            </div>

            <g:if test="${indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Categorical'}">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
            </g:if>
        </g:each>
    </div>
    <div role="tabpanel" class="tab-pane indicators" id="social_indicators">
        <g:each var="indicator" in="${social_indicators}">
            <div class="page-header">
                <b>${indicator.title}</b>
            </div>

            <g:if test="${indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Categorical'}">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
            </g:if>
        </g:each>
    </div>
</div>

<script type="text/javascript">
    $('#indicator_tabs a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })
</script>

<!--
vinhaça/área

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

-->