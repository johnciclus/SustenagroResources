navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

tabs([id: 'main', activeTab: 'tab_1', pagination: false],
        [['widget': 'tab', attrs: [label: message('assessment')], widgets: [
                ['widget': 'form', attrs: [action: '/tool/updateAnalysis', formClass: ''], widgets: [
                        ['widget': 'hiddenInput', attrs: [id: 'analysisId', value: analysisId]],
                        ['widget': 'tabs', attrs: [id : 'evaluation', pagination: false, submitTopButton: true, submitTopLabel: message('update')], widgets: [
                                ['widget': 'tab', attrs: [label: message('efficiencyAssessment')], widgets: [
                                        ['widget': 'tabs', attrs: [id : 'efficiency', finalPag: 'sustainability_tab_0', finalPagLabel: message('default.paginate.next')], widgets: efficiencyTabs]
                                ]],
                                ['widget': 'tab', attrs: [label: message('sustainabilityAssessment')], widgets: [
                                        ['widget': 'tabs', attrs: [id: 'sustainability', initialPag: 'efficiency_tab_4', initialPagLabel: message('default.paginate.prev')], widgets: sustainabilityTabs]
                                ]]
                        ]]
                ]]
        ]],
         ['widget': 'tab', attrs: [label: message('results')], widgets: [
                 ['widget': 'tabs', attrs: [id : 'results'], widgets: [
                         ['widget': 'tab', attrs: [label: message('efficiencyAndCostSpreadsheets')], widgets: [
                                 ['widget': 'text',       attrs: [text: '**'+message('efficiencyAssessment')+'**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('productionEfficiency')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('variable'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value')], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'text',       attrs: [text: message('justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('variable'), 'justification': message('justification')], data: dataReader.':ProductionEfficiencyFeature']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('productionEfficiency'), totalValue: vars['cost_production_efficiency'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('efficiencyInTheField.title')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('variable'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'weightTypeLabel': message('RegisteredWeight'), 'weight': message('weight'), 'totalValue': message('totalValue')], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'text',       attrs: [text: message('justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('variable'), 'justification': message('justification')], data: dataReader.':TechnologicalEfficiencyInTheField']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('efficiencyInTheField.title'), totalValue: vars['technologicalEfficiencyInTheField'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('efficiencyInTheIndustrial.title')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('variable'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'weightTypeLabel': message('RegisteredWeight'), 'weight': message('weight'), 'totalValue': message('totalValue')], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'text',       attrs: [text: message('justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('variable'), 'justification': message('justification')], data: dataReader.':TechnologicalEfficiencyInTheIndustrial']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('efficiencyInTheIndustrial.title'), totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('efficiencyAssessment')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indixes'), 'totalValue': message('totalValue')], data: [[label: message('productionEfficiency'), totalValue: vars['cost_production_efficiency'].round(2)],
                                                                                                                                                                [label: message('efficiencyInTheField.title'), totalValue: vars['technologicalEfficiencyInTheField'].round(2)],
                                                                                                                                                                [label: message('efficiencyInTheIndustrial.title'), totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)],
                                                                                                                                                                [label: message('efficiencyIndex.title'), totalValue: vars['efficiency'].round(2)]]]],
                                 ['widget': 'text',       attrs: [text: '**'+message('formula.title')+'**']],
                                 ['widget': 'efficiencyEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: message('sustainabilitySpreadsheets')], widgets: [
                                 ['widget': 'text',       attrs: [text: '**'+message('sustainabilityAssessment')+'**']],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('environmentalDimension')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indicator'), 'relevance': message('relevance'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'totalValue': message('totalValue')], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'text',       attrs: [text: message('justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indicator'), 'justification': message('justification')], data: dataReader.':EnvironmentalIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('environmentalIndex'), totalValue: vars['environment'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('economicDimension')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indicator'), 'relevance': message('relevance'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'totalValue': message('totalValue')], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'text',       attrs: [text: message('justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indicator'), 'justification': message('justification')], data: dataReader.':EconomicIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('economicIndex'), totalValue: vars['economic'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('socialDimension')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indicator'), 'relevance': message('relevance'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'totalValue': message('totalValue'), 'justification': message('justification')], data: dataReader.':SocialIndicator']],
                                 ['widget': 'text',       attrs: [text: message('justification')]],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indicator'), 'justification': message('justification')], data: dataReader.':SocialIndicator']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('socialIndex'), totalValue: vars['social'].round(2)]]]],
                                 ['widget': 'ln'],
                                 ['widget': 'text',       attrs: [text: '**'+message('sustainabilityAssessment')+'**']],
                                 ['widget': 'tableReport',attrs: [header: ['label': message('indixes'), 'totalValue': message('totalValue')], data:   [[label: message('environmentalIndex'), totalValue: vars['environment'].round(2)],
                                                                                                                                                             [label: message('economicIndex'), totalValue: vars['economic'].round(2)],
                                                                                                                                                             [label: message('socialIndex'), totalValue: vars['social'].round(2)],
                                                                                                                                                             [label: message('sustainabilityIndex'), totalValue: vars['sustainability'].round(2)]]]],
                                 ['widget': 'text',       attrs: [text: '**'+message('formula.title')+'**']],
                                 ['widget': 'sustainabilityEquation',       attrs: [:]],
                         ]],
                         ['widget': 'tab', attrs: [label: message('AssessementReport')], widgets: reportView]
                 ]]
        ]]
        ])