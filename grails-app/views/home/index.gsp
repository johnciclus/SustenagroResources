<!DOCTYPE html>
<html lang="en">
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Home</title>
	</head>
	<body>
		<div class="row main">
			<div class="col-sm-10 col-sm-offset-1 content">
				<h5 class="text-primary page-header">Example code</h5>
				<pre id="editor" class="ace_editor ace-tm">h1 {"bootstrap"}

p 'class': 'lead', {"bootstrap"}

blockquote { p { "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante." } }


div 'class': 'well',  {
    div 'class': 'well', {
        p 'class': 'lead', {
            "hola mundo"
        }
    }
}

  
ol 'class': "c1", {
    li 'class': "l1", {1}
    li 'class': "l2", {2}
    li 'class': "l2", {3}
    li 'class': "l2", {4}
    li 'class': "l2", {5}
    li 'class': "l2", {6}
}

table 'class':"table table-hover", {
    thead{
        tr{
          th{'#'}
          th{'First Name'}
          th{'Last Name'}
          th{'Username'}
        }
    }
    tbody{
        tr{
          th 'scope':"row", {'1'}
          td{ 'Mark' }
          td{ 'Otto' }
          td{ '@mdo' }
        }
    }
}

input 'type': "email", 'class':"form-control", 'id':"exampleInputEmail", {}

</pre>
			</div>
		</div>
	</body>
</html>