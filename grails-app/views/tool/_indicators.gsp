<ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">1.1 Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">     1.2 Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">       1.3 Sociais     </a></li>
</ul>

<div id="indicator_content" class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="environmental_indicators">
        <g:each var="indicator" in="${environmental_indicators}">
            <div class="form-group">
                <b>${indicator.title}</b>
                <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                    <g:each var="category" in="${categorical[indicator.class]}">
                        <div class="radio">
                            <label>
                                <input type="radio" name="<%= indicator.id_name %>" value="<%= category.id %>"> <%= category.title %>
                            </label>
                        </div>
                    </g:each>
                </g:if>
                <g:elseif test="${indicator.class =='http://bio.icmc.usp.br/sustenagro#Real' }">
                    <input type="text" class="spin-button" name="${indicator.id}">
                </g:elseif>
                <button type="button" id="<%= indicator.id_name %>-more" class="btn btn-link btn-sm" data-toggle="collapse" data-target="#<%= indicator.id_name %>-options">Mais opções</button>
                <div id="<%= indicator.id_name %>-options" class="collapse">
                    <button id="<%= indicator.id_name %>-clear" type="button" class="btn btn-default btn-sm clear">Apagar</button>
                </div>
            </div>
        </g:each>
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#economic_indicators">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>
    <div role="tabpanel" class="tab-pane" id="economic_indicators">
        <g:each var="indicator" in="${economic_indicators}">
            <div class="form-group">
            <b>${indicator.title}</b>
            <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.id %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
            </g:if>
            <g:elseif test="${indicator.class =='http://bio.icmc.usp.br/sustenagro#Real' }">
                <input type="text" class="spin-button" name="${indicator.id}">
            </g:elseif>
            </div>
        </g:each>
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#environmental_indicators">Anterior</a></li>
                    <li><a href="#social_indicators">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>
    <div role="tabpanel" class="tab-pane" id="social_indicators">
        <g:each var="indicator" in="${social_indicators}">
            <div class="form-group">
            <b>${indicator.title}</b>
            <g:if test="${indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://bio.icmc.usp.br/sustenagro#Categorical'}">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.id %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
            </g:if>
            <g:elseif test="${indicator.class =='http://bio.icmc.usp.br/sustenagro#Real' }">
                <input type="text" class="spin-button" name="${indicator.id}" >
            </g:elseif>
            </div>
        </g:each>
        <div>
            <nav>
                <ul class="pager">
                    <li><a href="#economic_indicators">Anterior</a></li>
                    <li><a href="#recomendations">Próximo</a></li>
                </ul>
            </nav>
        </div>
    </div>
</div>
<script>
    $(".spin-button").TouchSpin({
        verticalbuttons: true,
        verticalupclass: 'glyphicon glyphicon-plus',
        verticaldownclass: 'glyphicon glyphicon-minus'
    });

    $(".clear").click(function(){
        var id = $(this).attr('id').replace('-clear', '');
        $("input:radio[name='"+id+"']").removeAttr('checked');
    });

</script>
