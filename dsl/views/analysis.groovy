navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

tabs([id: 'main', activeTab: 'tab_1', pagination: false],
        [['widget': 'tab', attrs: [label: message('label.assessment')], widgets: [
                ['widget': 'form', attrs: [action: '/tool/updateAnalysis', formClass: ''], widgets: [
                        ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
                        ['widget': 'tabs', attrs: [id : 'evaluation', pagination: false, submitTopButton: true, submitTopLabel: message('label.update')], widgets: [
                                ['widget': 'tab', attrs: [label: message('label.efficiencyAssessment')], widgets: [
                                        ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: message('default.paginate.next')], widgets: efficiencyTabs]
                                ]],
                                ['widget': 'tab', attrs: [label: message('label.sustainabilityAssessment')], widgets: [
                                        ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: message('default.paginate.prev')], widgets: sustainabilityTabs]
                                ]]
                        ]]
                ]]
        ]],
         ['widget': 'tab', attrs: [label: message('label.results')], widgets: [
                 ['widget': 'tabs', attrs: [id : 'results'], widgets: [
                         ['widget': 'tab', attrs: [label: message('label.efficiencyAndCostSpreadsheets')], widgets: [
                                 ['widget': 'text',       attrs: [text: '**'+message('label.efficiencyAssessment')+'**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.productionEfficiency')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.variable'), 'valueTypeLabel': message('label.RegisteredValue'), 'value': message('label.value')], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'text',       attrs: [text: message('label.justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.variable'), 'justification': message('label.justification')], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.index'), 'totalValue': message('label.totalValue')], data: [[label: message('label.productionEfficiency'), totalValue: vars['cost_production_efficiency'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.efficiencyInTheField')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.variable'), 'valueTypeLabel': message('label.RegisteredValue'), 'value': message('label.value'), 'weightTypeLabel': message('label.RegisteredWeight'), 'weight': message('label.weight'), 'totalValue': message('label.totalValue')], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'text',       attrs: [text: message('label.justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.variable'), 'justification': message('label.justification')], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.index'), 'totalValue': message('label.totalValue')], data: [[label: message('label.efficiencyInTheField'), totalValue: vars['technologicalEfficiencyInTheField'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.efficiencyInTheIndustrial')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.variable'), 'valueTypeLabel': message('label.RegisteredValue'), 'value': message('label.value'), 'weightTypeLabel': message('label.RegisteredWeight'), 'weight': message('label.weight'), 'totalValue': message('label.totalValue')], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'text',       attrs: [text: message('label.justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.variable'), 'justification': message('label.justification')], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.index'), 'totalValue': message('label.totalValue')], data: [[label: message('label.efficiencyInTheIndustrial'), totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.efficiencyAssessment')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indixes'), 'totalValue': message('label.totalValue')], data: [[label: message('label.productionEfficiency'), totalValue: vars['cost_production_efficiency'].round(2)],
                                                                                                                                                                [label: message('label.efficiencyInTheField'), totalValue: vars['technologicalEfficiencyInTheField'].round(2)],
                                                                                                                                                                [label: message('label.efficiencyInTheIndustrial'), totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)],
                                                                                                                                                                [label: message('label.efficiencyIndex'), totalValue: vars['efficiency'].round(2)]]]],
                                 ['widget': 'text',       attrs: [text: '**'+message('formula.title')+'**']],
                                 ['widget': 'efficiencyEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: message('label.sustainabilitySpreadsheets')], widgets: [
                                 ['widget': 'text',       attrs: [text: '**'+message('label.sustainabilityAssessment')+'**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.environmentalDimension')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indicator'), 'relevance': message('label.relevance'), 'valueTypeLabel': message('label.RegisteredValue'), 'value': message('label.value'), 'totalValue': message('label.totalValue')], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'text',       attrs: [text: message('label.justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indicator'), 'justification': message('label.justification')], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.index'), 'totalValue': message('label.totalValue')], data: [[label: message('label.environmentalIndex'), totalValue: vars['environment'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.economicDimension')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indicator'), 'relevance': message('label.relevance'), 'valueTypeLabel': message('label.RegisteredValue'), 'value': message('label.value'), 'totalValue': message('label.totalValue')], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'text',       attrs: [text: message('label.justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indicator'), 'justification': message('label.justification')], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.index'), 'totalValue': message('label.totalValue')], data: [[label: message('label.economicIndex'), totalValue: vars['economic'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.socialDimension')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indicator'), 'relevance': message('label.relevance'), 'valueTypeLabel': message('label.RegisteredValue'), 'value': message('label.value'), 'totalValue': message('label.totalValue'), 'justification': message('label.justification')], data: dataReader.':SocialIndicator']],
                                 ['widget': 'text',       attrs: [text: message('label.justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indicator'), 'justification': message('label.justification')], data: dataReader.':SocialIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.index'), 'totalValue': message('label.totalValue')], data: [[label: message('label.socialIndex'), totalValue: vars['social'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('label.sustainabilityAssessment')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('label.indixes'), 'totalValue': message('label.totalValue')], data:   [[label: message('label.environmentalIndex'), totalValue: vars['environment'].round(2)],
                                                                                                                                                                    [label: message('label.economicIndex'), totalValue: vars['economic'].round(2)],
                                                                                                                                                                    [label: message('label.socialIndex'), totalValue: vars['social'].round(2)],
                                                                                                                                                                    [label: message('sustainabilityIndex.label'), totalValue: vars['sustainability'].round(2)]]]],
                                 ['widget': 'text',       attrs: [text: '**'+message('formula.title')+'**']],
                                 ['widget': 'sustainabilityEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: message('label.AssessemntReport')], widgets: reportView]
                 ]]
        ]]
        ])