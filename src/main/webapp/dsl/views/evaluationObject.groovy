navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

title 'text': message('label.evalObj.title')

description 'text': message('label.evalObj.description')

tabs([activeTab: activeTab, pagination: false], [
    ['widget': 'tab', attrs: [label: message('label.evalObj.new')], widgets:[
        ['widget': 'createEvaluationObject', attrs : [id: evaluationObjectURI], widgets: widgets]
    ]],
    ['widget': 'tab', attrs: [label: message('label.evalObj.select')], widgets:[
        ['widget': 'listEvaluationObjects', attrs : [id: evalObjId, userId: userId]]
    ]]
])