/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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