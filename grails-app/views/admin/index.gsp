<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="decisioner"/>
		<title>SustenAgro - Admin</title>
        <script src="/assets/ace-min-noconflict/ace.js" type="text/javascript" charset="utf-8"></script>
        <script src="/assets/ace-min-noconflict/ext-language_tools.js" type="text/javascript" charset="utf-8"></script>
        <script src="/assets/bootstrap-treeview.min.js" type="text/javascript" charset="utf-8"></script>
        <link rel="stylesheet" href="/assets/bootstrap-treeview.min.css" />
	</head>
	<body>
        <div class="row main">
			<div id="content" class="col-md-12 content">
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                        <li class="active"><a data-toggle="tab" href="#ontology">Ontology</a></li>
                        <li><a data-toggle="tab" href="#dsls">DSL</a></li>
                        <li><a data-toggle="tab" href="#views">Views</a></li>
                        <li><a data-toggle="tab" href="#internationalization">Internationalization</a></li>
                    </ul>
                    <div class="btn-group navbar-btn navbar-right">
                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
                            <sec:username/> <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a id="welcome" href="#">Welcome <sec:username/>!</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a id="logout" href="/">Exit</a></li>
                        </ul>
                    </div>
                </div>
				<div class="tab-content">
                    <div id="ontology" class="tab-pane fade in active">
                        <g:render template="ontology" />
                    </div>
                    <div id="dsls" class="tab-pane fade ">
                        <g:render template="dsls" />
                    </div>
                    <div id="views" class="tab-pane fade">
                        <g:render template="views" />
                    </div>
                    <div id="internationalization" class="tab-pane fade">
                        <g:render template="internationalization" />
					</div>
                    <div id="widgets" class="tab-pane fade">

                    </div>
				</div>
			</div>
		</div>
	</body>
</html>