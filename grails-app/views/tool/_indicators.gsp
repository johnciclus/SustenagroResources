<ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">1.1 Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">     1.2 Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">       1.3 Sociais     </a></li>
</ul>

<form id="assessment_form" action="/tool/assessmentReport" method="post" class="form-horizontal">
    <input type="hidden" name="production_unit_id" value="${production_unit_id}">
    <div id="indicator_content" class="tab-content">
        <div role="tabpanel" class="tab-pane active" id="environmental_indicators">
            <g:each var="indicator" in="${environmental_indicators}">
                <div class="form-group">
                    <label for="<%= indicator.id %>" class="col-sm-6 control-label">${indicator.title}</label>
                    <div class="col-sm-6">
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
                        <button type="button" id="<%= indicator.id %>-more" class="btn btn-link btn-sm" data-toggle="collapse" data-target="#<%= indicator.id %>-options">Mais opções</button>
                        <div id="<%= indicator.id %>-options" class="collapse">
                            <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear">Apagar</button>
                        </div>
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
                    <label for="<%= indicator.id %>" class="col-sm-6 control-label">${indicator.title}</label>
                    <div class="col-sm-6">
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
                        <button type="button" id="<%= indicator.id %>-more" class="btn btn-link btn-sm" data-toggle="collapse" data-target="#<%= indicator.id %>-options">Mais opções</button>
                        <div id="<%= indicator.id %>-options" class="collapse">
                            <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear">Apagar</button>
                        </div>
                    </div>
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
                    <label for="<%= indicator.id %>" class="col-sm-6 control-label">${indicator.title}</label>
                    <div class="col-sm-6">
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
                        <button type="button" id="<%= indicator.id %>-more" class="btn btn-link btn-sm" data-toggle="collapse" data-target="#<%= indicator.id %>-options">Mais opções</button>
                        <div id="<%= indicator.id %>-options" class="collapse">
                            <button id="<%= indicator.id %>-clear" type="button" class="btn btn-default btn-sm clear">Apagar</button>
                        </div>
                    </div>
                </div>
            </g:each>
            <div>
                <nav>
                    <ul class="pager">
                        <li><a href="#economic_indicators">Anterior</a></li>
                        <li>
                            <input type="submit" class="btn btn-primary" value="Avaliar" />
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
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
