navBarRoute 'evaluationObjects': evaluationObjects, 'evalObjId': evalObjId, 'analyses': analyses, 'analysisId': analysisId

tabs([id: 'main', pagination: false],
        [['widget': 'tab', attrs: [label: 'Avaliação'], widgets: [
                ['widget': 'tabs', attrs: [id : 'evaluation', pagination: false], widgets: [
                        ['widget': 'tab', attrs: [label: 'Avaliação da eficiência e custo'], widgets: [
                                ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: 'Próximo'], widgets: efficiencyTabs]
                        ]],
                        ['widget': 'tab', attrs: [label: 'Avaliação da sustentabilidade'], widgets: [
                                ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: 'Anterior'], widgets: sustainabilityTabs]
                        ]]
                ]]
        ]],
         ['widget': 'tab', attrs: [label: 'Resultados'], widgets: [
                 ['widget': 'tabs', attrs: [id : 'results'], widgets: [
                         ['widget': 'tab', attrs: [label: 'Resultados da Avaliação'], widgets: [
                                 ['widget': 'text',       attrs: [text: '**Avaliação da sustentabilidade**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Indicadores Ambientais**']],
                                 ['widget': 'table',      attrs: [header: ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'relevance': 'Relevância'], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'text',       attrs: [text: 'Índice ambiental: '+ vars['environment']]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Indicadores Econômicos**']],
                                 ['widget': 'table',      attrs: [header: ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'relevance': 'Relevância'], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'text',       attrs: [text: 'Índice econômico: '+ vars['economic']]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Indicadores Sociais**']],
                                 ['widget': 'table',      attrs: [header: ['label': 'Indicador', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'relevance': 'Relevância'], data: dataReader.':SocialIndicator']],
                                 ['widget': 'text',       attrs: [text: 'Índice social: '+ vars['social'], widgetClass: 'bg-info']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Avaliação da sustentabilidade**']],
                                 ['widget': 'text',       attrs: [text: 'Índice da sustentabilidade: '+ vars['sustainability']]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência da produção**']],
                                 ['widget': 'table',      attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor'], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'text',       attrs: [text: 'Índice de eficiência da produção: '+ vars['cost_production_efficiency']]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência tecnológica no campo**']],
                                 ['widget': 'table',      attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso'], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'text',       attrs: [text: 'Índice de tecnológica no campo: '+ vars['technologicalEfficiencyInTheField']]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Eficiência tecnológica na industria**']],
                                 ['widget': 'table',      attrs: [header: ['label': 'Variável', 'valueTypeLabel': 'Valor cadastrado', 'value': 'Valor', 'weightTypeLabel': 'Peso cadastrado', 'weight': 'Peso'], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'text',       attrs: [text: 'Índice de tecnológica na industria: '+ vars['technologicalEfficiencyInTheIndustrial']]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**Avaliação da eficiência**']],
                                 ['widget': 'text',       attrs: [text: 'Índice da eficiência: '+ vars['efficiency']]],
                                 ['widget': 'ln']
                         ]],
                         ['widget': 'tab', attrs: [label: 'Relatório da Avaliação'], widgets: reportView]
                 ]]
         ]]
        ])