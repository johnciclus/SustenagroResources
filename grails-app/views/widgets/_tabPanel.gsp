<div role="tabpanel" class="tab-pane ind-content <%=tab.widgetClass%>"  id="<%=tab.id%>">
    <g:if test="${widgets}">
        <g:each in="${widgets}">
            <g:render template="/widgets/${it.widget}" model="${it.attrs}"/>
        </g:each>
    </g:if>
    <div>
        <nav>
            <ul class="pager">
                <g:if test="${tab.previous}">
                    <li><a href="#<%=tab.previous%>"><%=tab.previousLabel%></a></li>
                </g:if>
                <g:if test="${tab.next}">
                    <li><a href="#<%=tab.next%>"><%=tab.nextLabel%></a></li>
                </g:if>
                <g:if test="${tab.submitLabel}">
                    <g:render template="/widgets/submit" model="[value: tab.submitLabel]"/>
                </g:if>
            </ul>
        </nav>
    </div>
</div>
