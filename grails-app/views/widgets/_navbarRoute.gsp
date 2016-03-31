<div class="btn-group" role="group" aria-label="route">
    <div class="btn-group" role="group">
        <button type="button" class="btn btn-default dropdown-toggle <g:if test='${evalObjId}'> active </g:if>" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            <g:if test="${evalObjId && evaluationObjects}">
                <g:each in="${evaluationObjects}">
                    <g:if test="${it.id == evalObjId}"> <%= it.label %> </g:if>
                </g:each>
            </g:if>
            <g:else>
                <%= evalObjTitle %>
            </g:else>
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li><a href="/tool/evalobj"><b><%= newEvalObjLabel %></b></a></li>
            <li role="separator" class="divider"></li>
            <g:if test="${evaluationObjects}">
                <g:each in="${evaluationObjects}">
                    <li <g:if test="${it.id == evalObjId}"> class="active" </g:if>> <a href="/tool/evalobj/${it.id}">${it.label}</a></li>
                </g:each>
            </g:if>
        </ul>
    </div>
    <g:if test="${evalObjId && evaluationObjects}">
        <div class="btn-group" role="group">
            <button type="button" class="btn btn-default dropdown-toggle <g:if test='${analysisId}'> active </g:if>" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <g:if test="${analysisId && analyses}">
                    <g:each in="${analyses}">
                        <g:if test="${it.id == analysisId}"> <%= it.label %> </g:if>
                    </g:each>
                </g:if>
                <g:else>
                    <%= analysesTitle %>
                </g:else>
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="/tool/scenario/${evalObjId}"><b><%= newAnalysisLabel %></b></a></li>
                <li role="separator" class="divider"></li>
                <g:if test="${analyses}">
                    <g:each in="${analyses}">
                        <li <g:if test="${it.id == analysisId}"> class="active" </g:if>><a href="/tool/analysis/${it.id}">${it.label}</a></li>
                    </g:each>
                </g:if>
            </ul>
        </div>
    </g:if>
</div>