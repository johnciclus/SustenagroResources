<g:if test="${tabs}">
    <ul id="<%=id%>" class="nav nav-tabs">
        <g:each var="tab" in="${tabs}">
            <g:render template="/widgets/tab" model="${tab.value.attrs}" />
        </g:each>

        <g:if test="${submitTopButton}">
            <g:render template="/widgets/submitTopButton" model="[label: submitTopLabel]" />
        </g:if>

        <g:if test="${saveTopButton}">
            <g:render template="/widgets/saveTopButton" model="[label: saveTopLabel]" />
        </g:if>

        <g:if test="${generateTopButton}">
            <g:render template="/widgets/generateTopButton" model="[label: generateTopLabel]" />
        </g:if>
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
                    <g:if test="${pagination}">
                        <nav>
                            <ul class="pager">
                                <g:if test="${tab.value.attrs.initialPagLabel}">
                                    <li><a href="#<%=tab.value.attrs.initialPag%>"><g:message code="default.paginate.prev" /></a></li>
                                </g:if>
                                <g:if test="${tab.value.attrs.previous}">
                                    <li><a href="#<%=tab.value.attrs.previous%>"><g:message code="default.paginate.prev" /></a></li>
                                </g:if>
                                <g:if test="${tab.value.attrs.next}">
                                    <li><a href="#<%=tab.value.attrs.next%>"><g:message code="default.paginate.next" /></a></li>
                                </g:if>
                                <g:if test="${tab.value.attrs.finalPagLabel}">
                                    <li><a href="#<%=tab.value.attrs.finalPag%>"><g:message code="default.paginate.next" /></a></li>
                                </g:if>
                                <g:if test="${tab.value.attrs.submitLabel}">
                                    <g:render template="/widgets/submit" model="[value: tab.value.attrs.submitLabel]"/>
                                </g:if>
                            </ul>
                        </nav>
                    </g:if>
                </div>
            </div>
        </g:each>
    </div>
</g:if>