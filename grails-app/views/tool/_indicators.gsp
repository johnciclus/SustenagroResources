<ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">1.1 Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">     1.2 Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">       1.3 Sociais     </a></li>
</ul>

<div id="indicator_content" class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="environmental_indicators">
        <g:each var="indicator" in="${environmental_indicators}">
            <div class="page-header">
                <b>${indicator.title}</b>
            </div>

            <g:if test="${indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Categorical'}">
                <div class="form-group">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
                </div>
            </g:if>
            <g:elseif test="${indicator.class =='http://biomac.icmc.usp.br/sustenagro#Real' }">
                <div class="form-group">
                    <input type="text" class="spin-button" name="${indicator.id}-real" value="0" >
                </div>
            </g:elseif>
        </g:each>
    </div>
    <div role="tabpanel" class="tab-pane" id="economic_indicators">
        <g:each var="indicator" in="${economic_indicators}">
            <div class="page-header">
                <b>${indicator.title}</b>
            </div>

            <g:if test="${indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Categorical'}">
                <div class="form-group">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
                </div>
            </g:if>
            <g:elseif test="${indicator.class =='http://biomac.icmc.usp.br/sustenagro#Real' }">
                <div class="form-group">
                    <input type="text" class="spin-button" name="${indicator.id}-real" value="0" >
                </div>
            </g:elseif>
        </g:each>
    </div>
    <div role="tabpanel" class="tab-pane" id="social_indicators">
        <g:each var="indicator" in="${social_indicators}">
            <div class="page-header">
                <b>${indicator.title}</b>
            </div>

            <g:if test="${indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Boolean' || indicator.valueType =='http://biomac.icmc.usp.br/sustenagro#Categorical'}">
                <div class="form-group">
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
                </div>
            </g:if>
            <g:elseif test="${indicator.class =='http://biomac.icmc.usp.br/sustenagro#Real' }">
                <div class="form-group">
                    <input type="text" class="spin-button" name="${indicator.id}-real" value="0" >
                </div>
            </g:elseif>
        </g:each>
    </div>
</div>
<script>
    $(".spin-button").TouchSpin({
        verticalbuttons: true,
        verticalupclass: 'glyphicon glyphicon-plus',
        verticaldownclass: 'glyphicon glyphicon-minus'
    });
</script>
