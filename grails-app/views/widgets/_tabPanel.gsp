<div role="tabpanel" class="tab-pane ind-content <%=widgetClass%>"  id="<%=id%>">
    <g:render template="/widgets/${id}"></g:render>
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
