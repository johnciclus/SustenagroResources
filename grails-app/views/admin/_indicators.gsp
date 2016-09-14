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
                    {'id':  id},
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