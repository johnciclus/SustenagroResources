navBarRoute 'evaluationObjects': evaluationObjects, 'evalObjId': evalObjId, 'analyses': analyses, 'analysisId': analysisId

form([action: '/tool/createScenario', formClass: ''], [
    ['widget': 'hiddenInput', attrs: [id: 'evalObjInstance', value: uri]],
    ['widget': 'tabs', attrs: [id: 'main', pagination: false], widgets: [
        ['widget': 'tab', attrs: [label: 'Avaliação da eficiência e custo'], widgets: [
                ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: 'Próximo'], widgets: efficiencyTabs]
        ]],
        ['widget': 'tab', attrs: [label: 'Avaliação da sustentabilidade'], widgets: [
                ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: 'Anterior', submit: true], widgets: sustainabilityTabs]
        ]]
    ]]
])