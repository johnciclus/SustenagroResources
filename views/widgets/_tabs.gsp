<g:if test="${tabs}">
    <ul id="<%=id%>" class="nav nav-tabs">
        <g:each var="tab" in="${tabs}">
            <g:render template="/widgets/tab" model="${tab.value.attrs}" />
        </g:each>
    </ul>

    <div id="<%=id%>_content" class="tab-content">
        <g:each var="tab" in="${tabs}">
            <div role="tabpanel" class="tab-pane ind-content <%=tab.value.attrs.widgetClass%>"  id="<%=tab.value.attrs.id%>">
                <g:if test="${tab.value.widgets}">
                    <g:each in="${tab.value.widgets}">
                        <g:render template="/widgets/${it.widget}" model="${it.attrs}"/>
                    </g:each>
                </g:if>
                <div>
                    <nav>
                        <ul class="pager">
                            <g:if test="${tab.value.attrs.initialPagLabel}">
                                <li><a href="#<%=tab.value.attrs.initialPag%>"><%=tab.value.attrs.initialPagLabel%></a></li>
                            </g:if>
                            <g:if test="${tab.value.attrs.previous}">
                                <li><a href="#<%=tab.value.attrs.previous%>"><%=tab.value.attrs.previousLabel%></a></li>
                            </g:if>
                            <g:if test="${tab.value.attrs.next}">
                                <li><a href="#<%=tab.value.attrs.next%>"><%=tab.value.attrs.nextLabel%></a></li>
                            </g:if>
                            <g:if test="${tab.value.attrs.finalPagLabel}">
                                <li><a href="#<%=tab.value.attrs.finalPag%>"><%=tab.value.attrs.finalPagLabel%></a></li>
                            </g:if>
                            <g:if test="${tab.value.attrs.submitLabel}">
                                <g:render template="/widgets/submit" model="[value: tab.value.attrs.submitLabel]"/>
                            </g:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </g:each>
    </div>
</g:if>