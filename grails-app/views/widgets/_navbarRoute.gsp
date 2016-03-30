<g:if test="${evaluationObjects}">
    <div class="btn-group" role="group" aria-label="route">
        <div class="btn-group" role="group">
            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Production Unit
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <g:each in="${evaluationObjects}">
                    <li><a href="/tool/scenario/${it.id}">${it.label}</a></li>
                </g:each>
                <li role="separator" class="divider"></li>
                <li><a href="/tool/index"><b>New production unit</b></a></li>
            </ul>
        </div>
        <g:if test="${analysis}">
            <div class="btn-group" role="group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Please select one analyses
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                    <g:each in="${analysis}">
                        <li><a href="/tool/analysis/${it.id}">${it.label}</a></li>
                    </g:each>
                    <li role="separator" class="divider"></li>
                    <li><a href="#"><b>New Analyse</b></a></li>
                </ul>
            </div>
        </g:if>
    </div>
</g:if>