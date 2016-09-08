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
    <meta name="layout" content="${gspLayout ?: 'main'}"/>
    <title><g:message code='springSecurity.denied.title' /></title>
</head>

<body>
<div class="body">
    <div class="jumbotron">
        <h2><g:message code='springSecurity.denied.title' /></h2>
        <p><g:message code='springSecurity.denied.message' /></p>
        <p><a class="btn btn-primary btn-lg" href="/login" role="button"><g:message code='actions.login' /></a></p>
    </div>
</div>
</body>

</html>