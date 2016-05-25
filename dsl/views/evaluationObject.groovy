navBarRoute 'username': username, 'userId': userId, 'evalObjId': evalObjId, 'analysisId': analysisId

title 'text': 'Etapas para preenchimento do Software:'

description 'text': '''O emprego do Software SustenAgro para avaliação da sustentabilidade do sistema de produção de cana-de-açúcar prevê as seguintes etapas::

1. Cadastro ou seleção da unidade produtiva
2. Caracterização do sistema de produção
3. Seleção e avaliação dos indicadores de sustentabilidade – Índice de Sustentabilidade
4. Análise da Eficiência da Tecnologia e de custos
5. Análise dos resultados na Matriz de Sustentabilidade
6. Gerenciamento da sustentabilidade

'''

tabs([activeTab: activeTab, pagination: false], [
    ['widget': 'tab', attrs: [label: 'Cadastrar nova unidade produtiva / fazenda'], widgets:[
        ['widget': 'createEvaluationObject', attrs : [id: evaluationObject.getURI()], widgets: evaluationObject.widgets]
    ]],
    ['widget': 'tab', attrs: [label: 'Selecionar unidade produtiva / fazenda'], widgets:[
        ['widget': 'listEvaluationObjects', attrs : [id: evalObjId, userId: userId]]
    ]]
])