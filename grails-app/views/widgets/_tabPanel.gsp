<div role="tabpanel" class="tab-pane ind-content <%=widgetClass%>"  id="<%=id%>">
    <g:if test="${widgets[id].args}">
        <g:each in="${widgets[id].args.data}">
            <fieldset>
                <legend><h5>${it.value.label}</h5></legend>

                <g:render template="/widgets/individualsList" model="${[  'subClasses': it.value.subClass,
                                                                        'values':     values]}" />
            </fieldset>
        </g:each>

    </g:if>

    <div>
        <nav>
            <ul class="pager">
                <g:if test="${previous}">
                    <li><a href="#<%=previous%>"><%=previousLabel%></a></li>
                </g:if>
                <g:if test="${next}">
                    <li><a href="#<%=next%>"><%=nextLabel%></a></li>
                </g:if>
                <g:if test="${submitLabel}">
                    <g:render template="/widgets/submit" model="[value: submitLabel]"/>
                </g:if>
            </ul>
        </nav>
    </div>
</div>
