<g:each in="${data}">
    <fieldset>
        <legend><h5>${it.value.label}</h5></legend>
        <g:render template="/widgets/individualsList" model="${[  'subClasses': it.value.subClass,
                                                                  'values':     values]}" />
    </fieldset>
</g:each>