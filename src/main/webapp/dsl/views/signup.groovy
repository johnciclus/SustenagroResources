package dsl.views

pageHeader text: message('default.greeting.welcome')+'!'
div text: message('default.form.fill')
form([id: 'signUpForm', action: '/user/createUser'], [[widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('label.name')+':', placeholder: message('label.name'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasName']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('label.lastname')+':', placeholder: message('label.lastname'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasSurname']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('label.institution')+':', placeholder: message('label.institution'), required: false, id: 'http://purl.org/biodiv/semanticUI#hasOrganization']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('label.position')+':', placeholder: message('label.position'), required: false, id: 'http://purl.org/biodiv/semanticUI#hasPosition']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('label.telephone')+':', placeholder: message('label.telephone.placeholder'), required: false, id: 'http://purl.org/biodiv/semanticUI#hasPhoneNumber']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'emailForm', model: [label: message('label.email')+':', placeholder: message('label.email.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasEmail']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('user.login.username')+':', placeholder: message('label.username.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasUsername']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'passwordForm', model: [label: message('user.login.password')+':', placeholder: message('label.password.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasPassword']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'passwordForm', model: [label: message('user.login.passwordRepeat')+':', placeholder: message('label.passwordRepeat.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasPassword-confirm']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'checkboxForm', model: [label: message('label.useterms'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasTermsofuse', value: 'yes']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'submit', widgetClass: 'col-sm-12 text-center', model: [ value: message('default.form.register')]]]

])