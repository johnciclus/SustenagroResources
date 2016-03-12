<ul id="indicator_tabs" class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"> <a href="#environmental_indicators" aria-controls="environmental_indicators" role="tab" data-toggle="tab">  1. Ambientais  </a></li>
    <li role="presentation">                <a href="#economic_indicators" aria-controls="economic_indicators" role="tab" data-toggle="tab">            2. Econômicos  </a></li>
    <li role="presentation">                <a href="#social_indicators" aria-controls="social_indicators" role="tab" data-toggle="tab">                3. Sociais     </a></li>
</ul>

<div id="indicator_content" class="tab-content">
    <div role="tabpanel" class="tab-pane" >

    </div>
</div>

<g:if test="${indicators}">
    <g:each in="${indicators}">
        <g:render template="/widgets/title" model="[text: it.value.label]" />
        <g:render template="/widgets/indicatorList" model="${['subClasses': it.value.subClass,
                                                              'values': values,
                                                              'categories': categories]}" />
    </g:each>
</g:if>
