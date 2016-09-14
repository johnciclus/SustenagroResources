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

ace.define("ace/theme/tomorrow",["require","exports","module","ace/lib/dom"],function(e,t,n){t.isDark=!1,t.cssClass="ace-tomorrow",t.cssText=".ace-tomorrow .ace_gutter {background: #f6f6f6;color: #4D4D4C}.ace-tomorrow .ace_print-margin {width: 1px;background: #f6f6f6}.ace-tomorrow {background-color: #FFFFFF;color: #4D4D4C}.ace-tomorrow .ace_cursor {color: #AEAFAD}.ace-tomorrow .ace_marker-layer .ace_selection {background: #D6D6D6}.ace-tomorrow.ace_multiselect .ace_selection.ace_start {box-shadow: 0 0 3px 0px #FFFFFF;border-radius: 2px}.ace-tomorrow .ace_marker-layer .ace_step {background: rgb(255, 255, 0)}.ace-tomorrow .ace_marker-layer .ace_bracket {margin: -1px 0 0 -1px;border: 1px solid #D1D1D1}.ace-tomorrow .ace_marker-layer .ace_active-line {background: #EFEFEF}.ace-tomorrow .ace_gutter-active-line {background-color : #dcdcdc}.ace-tomorrow .ace_marker-layer .ace_selected-word {border: 1px solid #D6D6D6}.ace-tomorrow .ace_invisible {color: #D1D1D1}.ace-tomorrow .ace_keyword,.ace-tomorrow .ace_meta,.ace-tomorrow .ace_storage,.ace-tomorrow .ace_storage.ace_type,.ace-tomorrow .ace_support.ace_type {color: #8959A8}.ace-tomorrow .ace_keyword.ace_operator {color: #3E999F}.ace-tomorrow .ace_constant.ace_character,.ace-tomorrow .ace_constant.ace_language,.ace-tomorrow .ace_constant.ace_numeric,.ace-tomorrow .ace_keyword.ace_other.ace_unit,.ace-tomorrow .ace_support.ace_constant,.ace-tomorrow .ace_variable.ace_parameter {color: #F5871F}.ace-tomorrow .ace_constant.ace_other {color: #666969}.ace-tomorrow .ace_invalid {color: #FFFFFF;background-color: #C82829}.ace-tomorrow .ace_invalid.ace_deprecated {color: #FFFFFF;background-color: #8959A8}.ace-tomorrow .ace_fold {background-color: #4271AE;border-color: #4D4D4C}.ace-tomorrow .ace_entity.ace_name.ace_function,.ace-tomorrow .ace_support.ace_function,.ace-tomorrow .ace_variable {color: #4271AE}.ace-tomorrow .ace_support.ace_class,.ace-tomorrow .ace_support.ace_type {color: #C99E00}.ace-tomorrow .ace_heading,.ace-tomorrow .ace_markup.ace_heading,.ace-tomorrow .ace_string {color: #718C00}.ace-tomorrow .ace_entity.ace_name.ace_tag,.ace-tomorrow .ace_entity.ace_other.ace_attribute-name,.ace-tomorrow .ace_meta.ace_tag,.ace-tomorrow .ace_string.ace_regexp,.ace-tomorrow .ace_variable {color: #C82829}.ace-tomorrow .ace_comment {color: #8E908C}.ace-tomorrow .ace_indent-guide {background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bdu3f/BwAlfgctduB85QAAAABJRU5ErkJggg==) right repeat-y}";var r=e("../lib/dom");r.importCssString(t.cssText,t.cssClass)})