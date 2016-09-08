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

<div class="row">
    <div class="col-md-12">
        <form id="view_form" action="/admin/views" method="post" class="form-inline-block pull-right" role="form">
            <input name="view" type="hidden" value="">
            <button type="submit" class="btn btn-primary" data-loading-text="<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span> Saving..."> Save </button>
        </form>
        <form id="reset_view_form" action="/admin/viewsReset" method="post" class="form-inline-block pull-right" role="form">
            <button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span> Restore </button>
        </form>
    </div>
</div>
<div class="row">
    <div id="viewItems" class="col-md-3 col-sm-3">
        <p class="title">Views</p>
        <ul class="list-group">
            <g:each in="${viewNames}">
                <a href="#<%=it%>" class="list-group-item itemlink"><%=it.capitalize()%></a>
            </g:each>
        </ul>
    </div>

    <div class="col-md-9 col-sm-9">
        <pre id="viewsEditor" class="ace_editor editor ace-tm"></pre>
    </div>

    <script type="application/javascript">
        $( document ).ready(function() {
            var viewsEditor = ace.edit("viewsEditor");
            viewsEditor.setTheme("ace/theme/chrome");
            viewsEditor.getSession().setMode("ace/mode/groovy");
            viewsEditor.setOption("showPrintMargin", false);
            viewsEditor.$blockScrolling = Infinity;

            document.getElementById('viewsEditor').style.fontSize = '14px';

            $("#view_form").submit(function (event) {
                $('#view_form button').button('loading');
                $.post(
                        $(this).attr('action'),
                        {   'id': $("input[name='view']").attr('value'),
                            'code': viewsEditor.getValue()},
                        function (data) {
                            var res = $(data);
                            var status = res.find("entry[key='status']");
                            if (status.text() == 'ok') {
                                $('#view_form button').button('reset');
                                $('#view_form button').removeClass('btn-warning').removeClass('btn-primary').addClass('btn-success');

                                setTimeout(function () {
                                    $('#view_form button').removeClass('btn-success').addClass('btn-primary');
                                }, 1000);

                            }
                        }
                );
                event.preventDefault();
            });

            $('#viewItems .itemlink').click(function(){
                var viewName = $(this).attr('href').substring(1);
                $.post( '/admin/getView',
                    {'id': viewName},
                    function (data) {
                        viewsEditor.setValue(data, -1);
                        $("input[name='view']").attr('value', viewName);
                    }
                );
                event.preventDefault();
            });
        });
    </script>
</div>