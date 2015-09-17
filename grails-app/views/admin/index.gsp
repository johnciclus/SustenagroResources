<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Admin</title>
		<asset:javascript src="ace-min-noconflict/ace.js"/>
	</head>
	<body>
        <div class="row main">
			<div id="content" class="col-sm-10 col-sm-offset-1 content">
				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#dsl">DSL Main</a></li>
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

								var dslEditor = ace.edit("dslEditor");
                                dslEditor.setTheme("ace/theme/chrome");
                                dslEditor.getSession().setMode("ace/mode/groovy");
                                dslEditor.setOption("showPrintMargin", false)
								document.getElementById('dslEditor').style.fontSize='13px';

								$( "#dsl_form" ).submit(function( event ) {
									$.post(
											$(this).attr('action'),
											{'code':  dslEditor.getValue() },
											function( data ) {
												if(data=='ok'){
													function resetButton(){
														$('#dsl_form button').removeClass('btn-success').addClass('btn-primary');
													}
													$('#dsl_form button').removeClass('btn-primary').addClass('btn-success');
													setTimeout(resetButton, 1000);
												}
											}
									);
									event.preventDefault();
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