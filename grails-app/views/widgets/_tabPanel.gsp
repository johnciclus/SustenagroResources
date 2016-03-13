<div role="tabpanel" class="tab-pane ind-content <%=widgetClass%>"  id="<%=id%>">
    <g:if test="${widgets}">
        <g:render template="/widgets/${widgets[id].widget}" model="${widgets[id].args}"/>
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
            </ul>
        </nav>
    </div>
</div>
