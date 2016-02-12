<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Admin</title>
		<asset:javascript src="ace-min-noconflict/ace.js"/>
        <asset:javascript src="ace-min-noconflict/ext-language_tools.js"/>
        <asset:javascript src="bootstrap-table.min.js"/>
        <asset:stylesheet href="bootstrap-table.min.css"/>
	</head>
	<body>
        <div class="row main">
			<div id="content" class="col-md-12 content">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#dsl">DSL Main</a></li>
					<li><a data-toggle="tab" href="#indicators">Indicators Editor</a></li>
					<li><a data-toggle="tab" href="#ontology">Ontology Editor</a></li>
					<li><a data-toggle="tab" href="#widgets">Widgets</a></li>
					<li><a data-toggle="tab" href="#default">Widgets Default</a></li>
				</ul>
				<div class="tab-content">
					<div id="dsl" class="tab-pane fade in active">
						<div class="row">
							<div class="col-md-8">
								<h5 class="text-primary">DSL Code</h5>
							</div>
							<div class="col-md-4">
								<form id="dsl_form" action="/admin/dsl" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
								</form>
								<form id="reset_dsl_form" action="/admin/dslReset" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
								</form>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<pre id="dslEditor" class="ace_editor editor ace-tm">${code}</pre>
							</div>

							<script type="application/javascript">
                                var Range = ace.require("ace/range").Range;
                                var langTools = ace.require("ace/ext/language_tools");
                                var dslEditor = ace.edit("dslEditor");
                                var session = dslEditor.getSession();

                                dslEditor.setTheme("ace/theme/chrome");
                                dslEditor.setOption("showPrintMargin", false);
                                dslEditor.setOptions({
                                    enableBasicAutocompletion: true,
                                    enableSnippets: true
                                });
                                session.setMode("ace/mode/groovy");

								document.getElementById('dslEditor').style.fontSize='13px';

                                var dslCompleter = {
                                    getCompletions: function(editor, session, pos, prefix, callback) {
                                        $.get('/admin/autoComplete?word='+prefix, function( respond ) {
                                            callback(null, respond);
                                        });

                                        //callback(null, [
                                        //        {'name': 'title', 'value': 'title', 'score': 400, 'meta': 'commands'},
                                        //        {'name': 'description', 'value': 'Description', 'score': 400, 'meta': 'commands'}]);
                                    }
                                }

                                langTools.addCompleter(dslCompleter);

								$( "#dsl_form" ).submit(function( event ) {
                                    var markers = session.getMarkers(false);
                                    for( var i in markers){
                                        if(markers[i].clazz=='ace_error-line')
                                            session.removeMarker(markers[i].id);
                                    }

									$.post(
                                        $(this).attr('action'),
                                        {'code':  dslEditor.getValue() },
                                        function( data ) {
                                            var res = $(data);
                                            var status = res.find("entry[key='status']");

                                            if(status.text() =='ok'){
                                                $('#dsl_form button').removeClass('btn-primary').addClass('btn-success');
                                                setTimeout(function(){
                                                    $('#dsl_form button').removeClass('btn-success').addClass('btn-primary');
                                                }, 1000);
                                            }
                                            else if(status.text() =='error'){
                                                $('#dsl_form button').removeClass('btn-primary').addClass('btn-danger');
                                                setTimeout(function(){
                                                    $('#dsl_form button').removeClass('btn-danger').addClass('btn-primary');
                                                }, 1000);
                                                var line = Number(res.find("entry[key='line']").text())-1;
                                                session.addMarker(new Range(line, 0, line, 200), "ace_error-line", "fullLine");
                                            }
                                        }
									);
									event.preventDefault();
								});
							</script>
						</div>
					</div>
					<div id="indicators" class="tab-pane fade">
						<div class="row">
                            <div class="col-md-3 col-sm-4 col-xs-6">
                                <g:each var="el" in="${dimensions}">
                                    <div class="panel-group" id="accordion-${el.id}">
                                        <div class="panel panel-default">
                                            <div class="panel-heading">
                                                <h4 class="panel-title">
                                                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse-${el.id}">${el.label}</a>
                                                </h4>
                                            </div>
                                            <div id="collapse-${el.id}" class="panel-collapse collapse in">
                                                <div class="panel-body">
                                                    <div class="list-group">
                                                    <g:each status="i" var="row" in="${indicators}">
                                                        <g:if test="${ row.dimension == el.id}">
                                                            <button id="${row.id}" type="button" class="list-group-item indicator">${row['label@pt']}</button>
                                                        </g:if>
                                                    </g:each>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </g:each>
                            </div>
                            <div id="indicator_editor" class="col-md-9 col-sm-8 col-xs-6">

                            </div>
                            <script type="application/javascript">

                                $('#indicators .indicator').click(function(){
                                    var id = $(this).attr('id');
                                    $('#indicators .indicator.active').removeClass('active');
                                    $(this).addClass('active');

                                    $.post('/admin/indicatorForm',
                                        {'id':  id ,
                                         '${_csrf.parameterName}': '${_csrf.token}'},
                                        function(data){
                                            $("#indicator_editor").html(data)
                                            $("#valuetype-table").bootstrapTable()
                                            $('#indicator_form').submit(function(event){
                                                $.post(
                                                    $(this).attr('action'),
                                                    $(this).serializeArray(),
                                                    function( data ) {
                                                        if(data.result == 'ok'){

                                                        }
                                                    }
                                                );
                                                event.preventDefault();
                                            });
                                            $('#indicator_editor .select-dimension').change( function(){
                                                var id = $(this).attr('id');
                                                var dim = $(this).val();
                                                id = id.substring(0, id.indexOf('_'));
                                                var attribute = $('#'+id+'_attribute');
                                                attribute.empty();

                                                $.post('/admin/attributes',
                                                        {'dimension':  dim },
                                                        function(data){
                                                            var res = $(data);
                                                            $.each(res.find("entry[key='attribute']"), function(){
                                                                attribute.append($('<option></option>').attr('value', $(this).text()).text($(this).text()));
                                                            });
                                                        }
                                                );
                                            });
                                        }
                                    );
                                });
                            </script>
						</div>
					</div>
					<div id="ontology" class="tab-pane fade">
						<div class="row">
							<div class="col-md-8">
								<h5 class="text-primary">Ontology</h5>
							</div>
							<div class="col-md-4">
								<form id="ontology_form" action="/admin/ontology" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
								</form>
								<form id="reset_ontology_form" action="/admin/ontologyReset" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
								</form>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<pre id="ontEditor" class="ace_editor editor ace-tm">${ontology}</pre>
							</div>

							<script type="application/javascript">
                                var ontEditor = ace.edit("ontEditor");
                                ontEditor.setTheme("ace/theme/chrome");
                                ontEditor.getSession().setMode("ace/mode/plain_text");
                                ontEditor.setOption("showPrintMargin", false);
                                document.getElementById('ontEditor').style.fontSize='13px';

								$( "#ontology_form" ).submit(function( event ) {
									$.post(
                                        $(this).attr('action'),
                                        {'ontology':  ontEditor.getValue() },
                                        function( data ) {
                                            if(data=='ok'){
                                                function resetButton(){
                                                    $('#ontology_form button').removeClass('btn-success').addClass('btn-primary');
                                                }
                                                $('#ontology_form button').removeClass('btn-primary').addClass('btn-success');
                                                setTimeout(resetButton, 1000);
                                            }
                                        }
									);
									event.preventDefault();
								});
							</script>
						</div>
					</div>
					<div id="widgets" class="tab-pane fade"></div>
					<div id="default" class="tab-pane fade"></div>
				</div>
			</div>
		</div>
	</body>
</html>