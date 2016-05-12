navBarRoute 'evaluationObjects': evaluationObjects, 'evalObjId': evalObjId, 'analyses': analyses, 'analysisId': analysisId

tabs([id: 'main', activeTab: 'tab_1', pagination: false],
        [['widget': 'tab', attrs: [label: 'Avaliação'], widgets: [
                ['widget': 'form', attrs: [action: '/tool/updateScenario', formClass: ''], widgets: [
                        ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
                        ['widget': 'tabs', attrs: [id : 'evaluation', pagination: false], widgets: [
                                ['widget': 'tab', attrs: [label: 'Avaliação da eficiência e custo'], widgets: [
                                        ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: 'Próximo'], widgets: efficiencyTabs]
                                ]],
                                ['widget': 'tab', attrs: [label: 'Avaliação da sustentabilidade'], widgets: [
                                        ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: 'Anterior', submit: true, submitLabel: 'Atualizar'], widgets: sustainabilityTabs]
                                ]]
                        ]]
                ]]
         ]],
         ['widget': 'tab', attrs: [label: 'Resultados'], widgets: [
                 ['widget': 'tabs', attrs: [id : 'results'], widgets: [
                         ['widget': 'tab', attrs: [label: 'Planilhas da eficiência e custo'], widgets: [
                                 ['widget': 'text',       attrs: [text: '**Avaliação da eficiência**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência da produção**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'justification': 'Justificativa'], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência da produção', totalValue: vars['cost_production_efficiency']]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência tecnológica no campo**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso', 'totalValue': 'Valor total', 'justification': 'Justificativa'], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência tecnológica no campo', totalValue: vars['technologicalEfficiencyInTheField']]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência tecnológica na industria**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso', 'totalValue': 'Valor total', 'justification': 'Justificativa'], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência tecnológica na industria', totalValue: vars['technologicalEfficiencyInTheIndustrial']]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Avaliação da eficiência**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índices', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência da produção', totalValue: vars['cost_production_efficiency']],
                                                                                                                                   [label: 'Eficiência tecnológica no campo', totalValue: vars['technologicalEfficiencyInTheField']],
                                                                                                                                   [label: 'Eficiência tecnológica na industria', totalValue: vars['technologicalEfficiencyInTheIndustrial']],
                                                                                                                                   [label: 'Índice de Eficiência do Sistema Agroindustrial da cana', totalValue: vars['efficiency']]]]],
                                 ['widget': 'text',       attrs: [text: '**Formulas**']],
                                 ['widget': 'efficiencyEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: 'Planilhas da sustentabilidade'], widgets: [
                                 ['widget': 'text',       attrs: [text: '**Avaliação da sustentabilidade**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Dimensão Ambiental**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'relevance': 'Relevância', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'totalValue': 'Valor total', 'justification': 'Justificativa'], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Índice ambiental', totalValue: vars['environment']]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Dimensão Econômica**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'relevance': 'Relevância', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'totalValue': 'Valor total', 'justification': 'Justificativa'], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Índice econômico', totalValue: vars['economic']]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Dimensão Social**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'relevance': 'Relevância', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'totalValue': 'Valor total', 'justification': 'Justificativa'], data: dataReader.':SocialIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Índice social', totalValue: vars['social']]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Avaliação da sustentabilidade**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índices', 'totalValue': 'Valor Total'], data: [[label: 'Índice ambiental', totalValue: vars['environment']],
                                                                                                                                   [label: 'Índice econômico', totalValue: vars['economic']],
                                                                                                                                   [label: 'Índice social', totalValue: vars['social']],
                                                                                                                                   [label: 'Índice da sustentabilidade', totalValue: vars['sustainability']]]]],
                                 ['widget': 'text',       attrs: [text: '**Formula**']],
                                 ['widget': 'sustainabilityEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: 'Relatório da Avaliação'], widgets: reportView]
                 ]]
        ]]
        ])