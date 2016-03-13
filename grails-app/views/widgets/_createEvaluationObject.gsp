<g:render template="/widgets/title" model="[text: title]"/>

<g:render template="/widgets/formCreateEvaluationObject"
          model="[id: 'create_form',
                  action: '/tool/createEvaluationObject',
                  method: 'post',
                  formClass: 'form-horizontal']"/>
