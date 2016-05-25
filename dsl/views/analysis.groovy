navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

tabs([id: 'main', activeTab: 'tab_1', pagination: false],
        [['widget': 'tab', attrs: [label: 'Avaliação'], widgets: [
                ['widget': 'form', attrs: [action: '/tool/updateAnalysis', formClass: ''], widgets: [
                        ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
                        ['widget': 'tabs', attrs: [id : 'evaluation', pagination: false, submitTopButton: true, submitTopLabel: 'Atualizar'], widgets: [
                                ['widget': 'tab', attrs: [label: 'Avaliação da eficiência e custo'], widgets: [
                                        ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: 'Próximo'], widgets: efficiencyTabs]
                                ]],
                                ['widget': 'tab', attrs: [label: 'Avaliação da sustentabilidade'], widgets: [
                                        ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: 'Anterior'], widgets: sustainabilityTabs]
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
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor'], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'text',       attrs: [text: 'Justificativa']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'justification': 'Justificativa'], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência da produção', totalValue: vars['cost_production_efficiency'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência tecnológica no campo**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso', 'totalValue': 'Valor total'], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'text',       attrs: [text: 'Justificativa']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'justification': 'Justificativa'], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência tecnológica no campo', totalValue: vars['technologicalEfficiencyInTheField'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência tecnológica na industria**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso', 'totalValue': 'Valor total'], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'text',       attrs: [text: 'Justificativa']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Variável', 'justification': 'Justificativa'], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência tecnológica na industria', totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Avaliação da eficiência**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índices', 'totalValue': 'Valor Total'], data: [[label: 'Eficiência da produção', totalValue: vars['cost_production_efficiency'].round(2)],
                                                                                                                                   [label: 'Eficiência tecnológica no campo', totalValue: vars['technologicalEfficiencyInTheField'].round(2)],
                                                                                                                                   [label: 'Eficiência tecnológica na industria', totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)],
                                                                                                                                   [label: 'Índice de Eficiência do Sistema Agroindustrial da cana', totalValue: vars['efficiency'].round(2)]]]],
                                 ['widget': 'text',       attrs: [text: '**Formulas**']],
                                 ['widget': 'efficiencyEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: 'Planilhas da sustentabilidade'], widgets: [
                                 ['widget': 'text',       attrs: [text: '**Avaliação da sustentabilidade**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Dimensão Ambiental**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'relevance': 'Relevância', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'totalValue': 'Valor total'], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'text',       attrs: [text: 'Justificativa']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'justification': 'Justificativa'], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Índice ambiental', totalValue: vars['environment'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Dimensão Econômica**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'relevance': 'Relevância', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'totalValue': 'Valor total'], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'text',       attrs: [text: 'Justificativa']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'justification': 'Justificativa'], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Índice econômico', totalValue: vars['economic'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Dimensão Social**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'relevance': 'Relevância', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'totalValue': 'Valor total', 'justification': 'Justificativa'], data: dataReader.':SocialIndicator']],
                                 ['widget': 'text',       attrs: [text: 'Justificativa']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Indicador', 'justification': 'Justificativa'], data: dataReader.':SocialIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índice', 'totalValue': 'Valor Total'], data: [[label: 'Índice social', totalValue: vars['social'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Avaliação da sustentabilidade**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': 'Índices', 'totalValue': 'Valor Total'], data: [[label: 'Índice ambiental', totalValue: vars['environment'].round(2)],
                                                                                                                                   [label: 'Índice econômico', totalValue: vars['economic'].round(2)],
                                                                                                                                   [label: 'Índice social', totalValue: vars['social'].round(2)],
                                                                                                                                   [label: 'Índice da sustentabilidade', totalValue: vars['sustainability'].round(2)]]]],
                                 ['widget': 'text',       attrs: [text: '**Formulas**']],
                                 ['widget': 'sustainabilityEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: 'Relatório da Avaliação'], widgets: reportView]
                 ]]
        ]]
        ])