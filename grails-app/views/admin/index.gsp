<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Admin</title>
		<asset:javascript src="ace-min-noconflict/ace.js"/>
	</head>
	<body>
        <div class="content main">

				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#dsl">DSL Main</a></li>
					<li><a data-toggle="tab" href="#ontology">Ontology Editor</a></li>
					<li><a data-toggle="tab" href="#widgets">DSL Widgets</a></li>
					<li><a data-toggle="tab" href="#default">DSL Default</a></li>
				</ul>
				<div class="tab-content">
					<div id="dsl" class="tab-pane fade in active">
						<div class="row">
							<div class="col-md-6 col-sm-offset-1">
								<h5 class="text-primary">DSL Code</h5>
							</div>
							<div class="col-md-4">
								<form id="dsl_form" action="/admin/dsl" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar </button>
								</form>
								<form id="reset_form" action="/admin/reset" method="post" class="form-inline-block pull-right" role="form">
									<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restaurar </button>
								</form>
							</div>
						</div>
						<div class="row">
							<div class="col-md-10 col-sm-offset-1">
								<pre id="editor" class="ace_editor ace-tm">${code}</pre>
								<div id='result'></div>
							</div>

							<script type="application/javascript">

								var editor = ace.edit("editor");
								editor.setTheme("ace/theme/chrome");
								editor.getSession().setMode("ace/mode/groovy");
								editor.setOption("showPrintMargin", false)
								document.getElementById('editor').style.fontSize='14px';

								$( "#dsl_form, #example_form" ).submit(function( event ) {
									$.post(
											$(this).attr('action'),
											{'code':  editor.getValue() },
											function( data ) {
												if(data=='ok'){
													function redo(){
														$('#dsl_form button').removeClass('btn-success').addClass('btn-primary');
													}
													$('#dsl_form button').removeClass('btn-primary').addClass('btn-success');
													setTimeout(redo, 1000);
												}
											}
									);
									event.preventDefault();
								});
							</script>
						</div>
					</div>
					<div id="ontology" class="tab-pane fade"></div>
					<div id="widgets" class="tab-pane fade"></div>
					<div id="default" class="tab-pane fade"></div>
				</div>
			</div>

	</body>
</html>