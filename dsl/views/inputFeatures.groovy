navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

form([action: '/tool/createAnalysis', formClass: ''], [
    ['widget': 'hiddenInput', attrs: [id: 'evalObjInstance', value: uri]],
    ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
    ['widget': 'tabs', attrs: [id: 'main', pagination: false, submitTopButton: true, submitTopLabel: 'Avaliar', saveTopButton: true, saveTopLabel: 'Salvar'], widgets: [
        ['widget': 'tab', attrs: [label: 'Avaliação da eficiência e custo'], widgets: [
                ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: 'Próximo'], widgets: efficiencyTabs]
        ]],
        ['widget': 'tab', attrs: [label: 'Avaliação da sustentabilidade'], widgets: [
                ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: 'Anterior'], widgets: sustainabilityTabs]
        ]]
    ]]
])