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
    <title>SustenAgro - login</title>
    <asset:javascript src="jquery.validate.min.js"/>
    <asset:javascript src="localization/messages_pt_BR.min.js"/>
</head>

<body>
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3 login">
            <div class="page-header">
                <h1><g:message code="greeting.welcome" />!</h1>
            </div>

            <g:if test='${flash.message}'>
                <div class="alert alert-success" role="alert">
                <p>${flash.message}</p>
                </div>
            </g:if>

            <div>
                <p><g:message code="user.enterData" /></p>
            </div>

            <form action='/login/authenticate' method='post' id='loginForm' class='form-horizontal'>
                <div class="form-group required">
                    <label for="username" class="col-sm-4 control-label"><g:message code="user.username" />:</label>
                    <div class="col-sm-8">
                        <input type='text' name='username' id='username' class='form-control' required/>
                    </div>
                </div>

                <div class="form-group required">
                    <label for='password' class='col-sm-4 control-label'><g:message code="user.password" />:</label>
                    <div class="col-sm-8">
                        <input type="password" name='password' id='password' class="form-control" required/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <label>
                            <input type="checkbox" name='remember_me' id='remember_me' <g:if test='${hasCookie}'>checked="checked"</g:if>/><g:message code="user.remainConnected" />
                        </label>
                    </div>
                </div>

                <p id="remember_me_holder">

                </p>

                <div class="form-group col-sm-12 text-center">
                    <input type="submit" id="submit" value="<g:message code="default.form.enter" />" class="btn btn-primary"/>
                </div>
            </form>
        </div>
    </div>

<script type="text/javascript">
    $(document).ready(function() {
        $("form").each(function (index) {
            $(this).validate({
                errorClass: "has-error",
                errorPlacement: function (error, element) {
                    var form_group = $(element).parents('.form-group');
                    form_group.children(':last-child').append(error);
                },
                highlight: function (element, errorClass, validClass) {
                    //console.log('highlight');
                    var form_group = $(element).parents('.form-group');
                    form_group.addClass(errorClass).removeClass(validClass);
                },
                unhighlight: function (element, errorClass, validClass) {
                    //console.log('unhighlight');
                    var form_group = $(element).parents('.form-group');
                    form_group.removeClass(errorClass).addClass(validClass);
                }
            });
        });
    });
    </script>

</body>
</html>