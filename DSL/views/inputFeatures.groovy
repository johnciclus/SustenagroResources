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

form([action: '/tool/createAnalysis', formClass: ''], [
    ['widget': 'hiddenInput', attrs: [id: 'evalObjInstance', value: uri]],
    ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
    ['widget': 'tabs', attrs: [id: 'main', pagination: false, submitTopButton: true, submitTopLabel: message('default.form.assess'), saveTopButton: true, saveTopLabel: message('default.form.save')], widgets: [
        ['widget': 'tab', attrs: [label: message('efficiencyAssessment')], widgets: [
                ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: message('default.paginate.next')], widgets: efficiencyTabs]
        ]],
        ['widget': 'tab', attrs: [label: message('sustainabilityAssessment')], widgets: [
                ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_1', initialPagLabel: message('default.paginate.prev')], widgets: sustainabilityTabs]
        ]]
    ]]
])