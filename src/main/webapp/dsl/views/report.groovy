text text: '**Análise ID: '+analysisId+'**'

tabs([pagination: false], [
    ['widget': 'tab', attrs: [label: message('sustainabilityAssessment')], widgets: reportView]
])


text text: '**'+message('efficiencyAssessment')+'**'
text text: '**'+message('productionEfficiency')+'**'
tableReport header: ['label': message('variable'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value')], data: dataReader.':ProductionEfficiencyFeature'
text text: message('justification')
tableReport header: ['label': message('variable'), 'justification': message('justification')], data: dataReader.':ProductionEfficiencyFeature'
tableReport header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('productionEfficiency'), totalValue: vars['cost_production_efficiency'].round(2)]]
ln()
text text: '**'+message('efficiencyInTheField.title')+'**'
tableReport header: ['label': message('variable'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'weightTypeLabel': message('RegisteredWeight'), 'weight': message('weight'), 'totalValue': message('totalValue')], data: dataReader.':TechnologicalEfficiencyInTheField'
text text: message('justification')
tableReport header: ['label': message('variable'), 'justification': message('justification')], data: dataReader.':TechnologicalEfficiencyInTheField'
tableReport header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('efficiencyInTheField.title'), totalValue: vars['technologicalEfficiencyInTheField'].round(2)]]
ln() 
text text: '**'+message('efficiencyInTheIndustrial.title')+'**'
tableReport header: ['label': message('variable'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'weightTypeLabel': message('RegisteredWeight'), 'weight': message('weight'), 'totalValue': message('totalValue')], data: dataReader.':TechnologicalEfficiencyInTheIndustrial'
text text: message('justification')
tableReport header: ['label': message('variable'), 'justification': message('justification')], data: dataReader.':TechnologicalEfficiencyInTheIndustrial'
tableReport header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('efficiencyInTheIndustrial.title'), totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)]]

ln()
text text: '**'+message('efficiencyAssessment')+'**'
tableReport header: ['label': message('indixes'), 'totalValue': message('totalValue')], data: [[label: message('productionEfficiency'), totalValue: vars['cost_production_efficiency'].round(2)],
                                                                                                [label: message('efficiencyInTheField.title'), totalValue: vars['technologicalEfficiencyInTheField'].round(2)],
                                                                                                [label: message('efficiencyInTheIndustrial.title'), totalValue: vars['technologicalEfficiencyInTheIndustrial'].round(2)],
                                                                                                [label: message('efficiencyIndex.title'), totalValue: vars['efficiency'].round(2)]]
text text: '**'+message('formula.title')+'**'
efficiencyEquation [:]

ln()
text text: '**'+message('sustainabilityAssessment')+'**'
text text: '**'+message('environmentalDimension')+'**'
tableReport header: ['label': message('indicator'), 'relevance': message('relevance'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'totalValue': message('totalValue')], data: dataReader.':EnvironmentalIndicator'
text text: message('justification')
tableReport header: ['label': message('indicator'), 'justification': message('justification')], data: dataReader.':EnvironmentalIndicator'
tableReport header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('environmentalIndex'), totalValue: vars['environment'].round(2)]]
ln()
text text: '**'+message('economicDimension')+'**'
tableReport header: ['label': message('indicator'), 'relevance': message('relevance'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'totalValue': message('totalValue')], data: dataReader.':EconomicIndicator'
text text: message('justification')
tableReport header: ['label': message('indicator'), 'justification': message('justification')], data: dataReader.':EconomicIndicator'
tableReport header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('economicIndex'), totalValue: vars['economic'].round(2)]]
ln()
text text: '**'+message('socialDimension')+'**'
tableReport header: ['label': message('indicator'), 'relevance': message('relevance'), 'valueTypeLabel': message('RegisteredValue'), 'value': message('value'), 'totalValue': message('totalValue'), 'justification': message('justification')], data: dataReader.':SocialIndicator'
text text: message('justification')
tableReport header: ['label': message('indicator'), 'justification': message('justification')], data: dataReader.':SocialIndicator'
tableReport header: ['label': message('index'), 'totalValue': message('totalValue')], data: [[label: message('socialIndex'), totalValue: vars['social'].round(2)]]
ln()
text text: '**'+message('sustainabilityAssessment')+'**'
tableReport header: ['label': message('indixes'), 'totalValue': message('totalValue')], data:   [[label: message('environmentalIndex'), totalValue: vars['environment'].round(2)],
                                                                                                  [label: message('economicIndex'), totalValue: vars['economic'].round(2)],
                                                                                                  [label: message('socialIndex'), totalValue: vars['social'].round(2)],
                                                                                                  [label: message('sustainabilityIndex'), totalValue: vars['sustainability'].round(2)]]
text text: '**'+message('formula.title')+'**'
sustainabilityEquation [:]

