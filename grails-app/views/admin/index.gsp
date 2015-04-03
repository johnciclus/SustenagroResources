<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Admin</title>
		<asset:javascript src="ace-min-noconflict/ace.js"/>
	</head>
	<body>
		<div class="row main">
			<div class="col-sm-10 col-sm-offset-1 content">
				<h5 class="text-primary page-header">DSL Code</h5>

				<pre id="editor" class="ace_editor ace-tm"></pre>

				<form id="dsl_form" action="/sustenagro/admin/dslCreate" method="post">
					<div class="form-group">
						<input type="submit" class="btn btn-primary" value="Generate" />
					</div>
				</form>

				<form id="example_form" action="/sustenagro/admin/GroovyArchitecture" method="post">
					<div class="form-group">
						<input type="submit" class="btn btn-primary" value="Example" />
					</div>
				</form>
				
				<h5 class="text-primary page-header">Result</h5>
				<div id='result'>
					
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
						  $('#result').html(data);
						}
					  );
					  event.preventDefault();
					});
				</script>
			</div>
		</div>
	</body>
</html>