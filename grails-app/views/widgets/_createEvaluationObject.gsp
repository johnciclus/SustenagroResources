<g:render template="/widgets/title" model="[title: title]"/>

<g:render template="/widgets/formCreateEvaluationObject"
          model="[id: 'create_form',
                  action: '/tool/createEvaluationObject',
                  method: 'post',
                  formClass: 'form-horizontal']"/>
