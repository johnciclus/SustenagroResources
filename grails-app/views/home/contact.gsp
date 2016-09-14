<!DOCTYPE html>
<!--
  Copyright (c) 2015-$today.year Dilvan Moreira.
  Copyright (c) 2015-$today.year John Garavito.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  -->

<html>
	<head>
		<meta name="layout" content="main"/>
		<title>SustenAgro - Contact</title>
	</head>
	<body>
		<div class="row main">
			<div class="col-sm-10 col-sm-offset-1 content">
				<g:if test="${inputs}">
					<g:each in="${inputs}">
						<div class="section">
							<g:render template="/widgets/${it.widget}" model="${it.attrs}" />
						</div>
					</g:each>
				</g:if>
			</div>
		</div>
	</body>
</html>