/*
 * Copyright (c) 2015-2016 Dilvan Moreira.
 * Copyright (c) 2015-2016 John Garavito.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

pageHeader text: message('greeting.welcome')+'!'
div text: message('default.form.fill')
requireLabel()
form([id: 'signUpForm', action: '/user/createUser'], [[widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('name')+':', placeholder: message('name'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasName']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('lastname')+':', placeholder: message('lastname'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasSurname']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('institution')+':', placeholder: message('institution'), required: false, id: 'http://purl.org/biodiv/semanticUI#hasOrganization']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('position')+':', placeholder: message('position'), required: false, id: 'http://purl.org/biodiv/semanticUI#hasPosition']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('telephone')+':', placeholder: message('telephone.placeholder'), required: false, id: 'http://purl.org/biodiv/semanticUI#hasPhoneNumber']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'emailForm', model: [label: message('email')+':', placeholder: message('email.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasEmail']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'textForm', model: [label: message('user.username')+':', placeholder: message('username.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasUsername']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'passwordForm', model: [label: message('user.password')+':', placeholder: message('password.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasPassword']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'passwordForm', model: [label: message('user.passwordRepeat')+':', placeholder: message('passwordRepeat.placeholder'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasPassword-confirm']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'checkboxForm', model: [label: message('useterms'), required: true, id: 'http://purl.org/biodiv/semanticUI#hasTermsofuse', value: 'yes']]],
                                                      [widget: 'formGroup', attrs: [widgetName: 'submit', widgetClass: 'col-sm-12 text-center', model: [ value: message('default.form.register')]]]

])