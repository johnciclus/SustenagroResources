navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

form([action: '/tool/createAnalysis', formClass: ''], [
    ['widget': 'hiddenInput', attrs: [id: 'evalObjInstance', value: uri]],
    ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
    ['widget': 'tabs', attrs: [id: 'main', pagination: false, submitTopButton: true, submitTopLabel: message('default.form.assess'), saveTopButton: true, saveTopLabel: message('default.form.save')], widgets: [
        ['widget': 'tab', attrs: [label: message('label.efficiencyAssessment')], widgets: [
                ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: message('default.paginate.next')], widgets: efficiencyTabs]
        ]],
        ['widget': 'tab', attrs: [label: message('label.sustainabilityAssessment')], widgets: [
                ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_1', initialPagLabel: message('default.paginate.prev')], widgets: sustainabilityTabs]
        ]]
    ]]
])