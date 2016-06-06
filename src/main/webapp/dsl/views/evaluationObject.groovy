navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

title 'text': message('evalObj.title')

description 'text': message('evalObj.description')

tabs([activeTab: activeTab, pagination: false], [
    ['widget': 'tab', attrs: [label: message('evalObj.register')], widgets:[
        ['widget': 'createEvaluationObject', attrs : [id: evaluationObjectURI], widgets: widgets]
    ]],
    ['widget': 'tab', attrs: [label: message('evalObj.select')], widgets:[
        ['widget': 'listEvaluationObjects', attrs : [id: evalObjId, userId: userId]]
    ]]
])