<ul id="indicator_tabs" class="nav nav-tabs">
    <li role="presentation" class="active"> <a href="#environmental_indicators">1.1 Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators">     1.2 Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators">       1.3 Sociais     </a></li>
</ul>

<div id="tabs_content" class="tab-content">
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
                    <label for="${indicator.id}Real">Numérico</label>
                    <input type="text" class="form-control" name="${indicator.id}-real">
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
                    <label for="${indicator.id}Real">Numérico</label>
                    <input type="text" class="form-control" name="${indicator.id}-real" value="0">
                    <div class="input-group-btn-vertical">
                        <button class="btn btn-default" type="button"><span class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span></button>
                        <button class="btn btn-default" type="button"><span class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span></button>
                    </div>
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
                <g:each var="category" in="${categorical[indicator.class]}">
                    <div class="radio">
                        <label>
                            <input type="radio" name="<%= indicator.class %>" value="<%= category.id %>"> <%= category.title %>
                        </label>
                    </div>
                </g:each>
            </g:if>
            <g:elseif test="${indicator.class =='http://biomac.icmc.usp.br/sustenagro#Real' }">
                <div class="form-group">
                    <label for="${indicator.id}Real">Numérico</label>
                    <input type="text" class="form-control" name="${indicator.id}-real">
                </div>
            </g:elseif>
        </g:each>
    </div>
</div>

<script type="text/javascript">
    $('#indicator_tabs a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })
</script>