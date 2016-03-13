<g:if test="${indicators}">
    <g:each in="${indicators}">
        <g:render template="/widgets/title" model="[text: it.value.label]" />
        <g:render template="/widgets/indicatorList" model="${['subClasses': it.value.subClass,
                                                              'values': values,
                                                              'categories': categories]}" />
    </g:each>
</g:if>
<g:render template="/widgets/formGroup" model="[widgetName: 'submit', widgetClass: 'col-sm-12 text-center', model: [value: submitLabel]]"/>
