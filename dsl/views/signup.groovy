pageHeader text: 'Bem-vindo!'
div text: 'Por favor preencha o formulário'
form([id: 'signUpForm', action: '/user/createUser'], [[widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: 'Nome:', placeholder: 'Nome', required: true, id: 'http://purl.org/biodiv/semanticUI#name']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: 'Sobrenome:', placeholder: 'Sobrenome', required: true, id: 'http://purl.org/biodiv/semanticUI#surname']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: 'Instituição / Organização:', placeholder: 'Organização ou institução', required: false, id: 'http://purl.org/biodiv/semanticUI#organization']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: 'Cargo:', placeholder: 'Cargo na institução', required: false, id: 'http://purl.org/biodiv/semanticUI#position']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: 'Número de telefone:', placeholder: 'Número com DDD', required: false, id: 'http://purl.org/biodiv/semanticUI#phoneNumber']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'emailForm', model: [label: 'Email:', placeholder: 'user@domain.org', required: true, id: 'http://purl.org/biodiv/semanticUI#email']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: 'Nome de usuário:', placeholder: 'user', required: true, id: 'http://purl.org/biodiv/semanticUI#userName']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'passwordForm', model: [label: 'Senha:', placeholder: 'Senha (mínimo 5 caracteres)', required: true, id: 'http://purl.org/biodiv/semanticUI#password']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'passwordForm', model: [label: 'Repita a senha:', placeholder: 'Repita a senha (mínimo 5 caracteres)', required: true, id: 'http://purl.org/biodiv/semanticUI#password-confirm']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'checkboxForm', model: [label: 'Você concorda com os nossos termos de uso', required: true, id: 'http://purl.org/biodiv/semanticUI#termsofuse', value: 'yes']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'submit', widgetClass: 'col-sm-12 text-center', model: [ value: 'Cadastrar']]]

])