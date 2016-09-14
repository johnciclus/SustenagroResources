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

ace.define("ace/theme/clouds",["require","exports","module","ace/lib/dom"],function(e,t,n){t.isDark=!1,t.cssClass="ace-clouds",t.cssText='.ace-clouds .ace_gutter {background: #ebebeb;color: #333}.ace-clouds .ace_print-margin {width: 1px;background: #e8e8e8}.ace-clouds {background-color: #FFFFFF;color: #000000}.ace-clouds .ace_cursor {color: #000000}.ace-clouds .ace_marker-layer .ace_selection {background: #BDD5FC}.ace-clouds.ace_multiselect .ace_selection.ace_start {box-shadow: 0 0 3px 0px #FFFFFF;border-radius: 2px}.ace-clouds .ace_marker-layer .ace_step {background: rgb(255, 255, 0)}.ace-clouds .ace_marker-layer .ace_bracket {margin: -1px 0 0 -1px;border: 1px solid #BFBFBF}.ace-clouds .ace_marker-layer .ace_active-line {background: #FFFBD1}.ace-clouds .ace_gutter-active-line {background-color : #dcdcdc}.ace-clouds .ace_marker-layer .ace_selected-word {border: 1px solid #BDD5FC}.ace-clouds .ace_invisible {color: #BFBFBF}.ace-clouds .ace_keyword,.ace-clouds .ace_meta,.ace-clouds .ace_support.ace_constant.ace_property-value {color: #AF956F}.ace-clouds .ace_keyword.ace_operator {color: #484848}.ace-clouds .ace_keyword.ace_other.ace_unit {color: #96DC5F}.ace-clouds .ace_constant.ace_language {color: #39946A}.ace-clouds .ace_constant.ace_numeric {color: #46A609}.ace-clouds .ace_constant.ace_character.ace_entity {color: #BF78CC}.ace-clouds .ace_invalid {background-color: #FF002A}.ace-clouds .ace_fold {background-color: #AF956F;border-color: #000000}.ace-clouds .ace_storage,.ace-clouds .ace_support.ace_class,.ace-clouds .ace_support.ace_function,.ace-clouds .ace_support.ace_other,.ace-clouds .ace_support.ace_type {color: #C52727}.ace-clouds .ace_string {color: #5D90CD}.ace-clouds .ace_comment {color: #BCC8BA}.ace-clouds .ace_entity.ace_name.ace_tag,.ace-clouds .ace_entity.ace_other.ace_attribute-name {color: #606060}.ace-clouds .ace_indent-guide {background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==") right repeat-y}';var r=e("../lib/dom");r.importCssString(t.cssText,t.cssClass)})