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
			<div id="content" class="col-sm-12 content">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#dsl">DSL Main</a></li>
					<li><a data-toggle="tab" href="#indicators">Indicators Editor</a></li>
					<li><a data-toggle="tab" href="#ontology">Ontology Editor</a></li>
					<li><a data-toggle="tab" href="#widgets">DSL Widgets</a></li>
					<li><a data-toggle="tab" href="#default">DSL Default</a></li>
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
                                    enableBasicAutocompletion: true
                                });
                                session.setMode("ace/mode/groovy");

								document.getElementById('dslEditor').style.fontSize='13px';

                                var dslCompleter = {
                                    getCompletions: function(editor, session, pos, prefix, callback) {
                                        console.log(prefix);

                                        $.get('/admin/autoComplete', function( respond ) {
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
							<div class="col-md-8">
							</div>
							<div class="col-md-4">
								<form id="indicators_form" action="/admin/indicators" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
								</form>
								<form id="reset_indicators_form" action="/admin/indicatorsReset" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
								</form>
							</div>
						</div>
						<div class="row">
                            <div id="indicator_editor" class="col-sm-10 col-sm-offset-1">
                                <form class="form-horizontal">
                                <g:each status="i" var="row" in="${indicators}">
                                    <div class="form-group">
                                        <h5>${row['title']}</h5>
                                        <input type="hidden" name="id" value="${row['id']}">
                                        <g:each var="tag" in="${ind_tags}">
                                            <label for="${i}_${tag}" class="col-sm-4 control-label">${tag.capitalize()}</label>
                                            <div class="col-sm-8">
                                                <g:if test="${tag == 'class'}">
                                                    <select id="${i}_class" class="form-control">
                                                        <g:each var="el" in="${classes}">
                                                            <option value="${el.class}" <g:if test="${ row['class'] == el.class}"> selected </g:if>> ${el.class}</option>
                                                        </g:each>
                                                    </select>
                                                </g:if>
                                                <g:elseif test="${tag == 'dimension'}">
                                                    <select id="${i}_dimension" class="form-control select-dimension">
                                                        <g:each var="el" in="${dimensions}">
                                                            <option value="${el.dimension}" <g:if test="${ row['dimension'] == el.dimension}"> selected </g:if>> ${el.dimension}</option>
                                                        </g:each>
                                                    </select>
                                                </g:elseif>
                                                <g:elseif test="${tag == 'attribute'}">
                                                    <select id="${i}_attribute" class="form-control select-attribute">
                                                        <g:each var="el" in="${attributes[row['dimension']]}">
                                                            <option value="${el.attribute}" <g:if test="${ row['attribute'] == el.attribute}"> selected </g:if>> ${el.attribute}</option>
                                                        </g:each>
                                                    </select>
                                                </g:elseif>
                                                <g:else>
                                                    <input id="${i}_${tag}" type="text" class="form-control input input-text-lg" name="${i}_${tag}" value="${row[tag]}" >
                                                </g:else>
                                            </div>
                                        </g:each>
                                    </div>
                                    <div>
                                        <table data-toggle="table"
                                               data-sort-name="id"
                                               data-sort-order="desc">
                                            <thead>
                                            <tr>
                                                <th data-field="id" data-sortable="true">ID</th>
                                                <th data-field="name" data-sortable="true">Name</th>
                                                <th data-field="value" data-sortable="true">Value</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                                <g:each var="el" in="${options[row['class']]}">
                                                    <tr data-index="${i}">
                                                        <td>
                                                            <label for="${i}_id" class="col-sm-4 control-label">${el.option}</label>
                                                        </td>
                                                        <td>
                                                            <input id="${i}_label" type="text" class="form-control input input-text-lg" value="${el.label}" >
                                                        </td>
                                                        <td>
                                                            <input id="${i}_value" type="text" class="form-control input input-text-lg" value="${el.value}" >
                                                        </td>
                                                    </tr>
                                                </g:each>
                                            </tbody>
                                        </table>
                                    </div>
                                </g:each>
                                </form>
                            </div>




                            <script type="application/javascript">
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