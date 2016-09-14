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

ace.define("ace/theme/vibrant_ink",["require","exports","module","ace/lib/dom"],function(e,t,n){t.isDark=!0,t.cssClass="ace-vibrant-ink",t.cssText=".ace-vibrant-ink .ace_gutter {background: #1a1a1a;color: #BEBEBE}.ace-vibrant-ink .ace_print-margin {width: 1px;background: #1a1a1a}.ace-vibrant-ink {background-color: #0F0F0F;color: #FFFFFF}.ace-vibrant-ink .ace_cursor {color: #FFFFFF}.ace-vibrant-ink .ace_marker-layer .ace_selection {background: #6699CC}.ace-vibrant-ink.ace_multiselect .ace_selection.ace_start {box-shadow: 0 0 3px 0px #0F0F0F;border-radius: 2px}.ace-vibrant-ink .ace_marker-layer .ace_step {background: rgb(102, 82, 0)}.ace-vibrant-ink .ace_marker-layer .ace_bracket {margin: -1px 0 0 -1px;border: 1px solid #404040}.ace-vibrant-ink .ace_marker-layer .ace_active-line {background: #333333}.ace-vibrant-ink .ace_gutter-active-line {background-color: #333333}.ace-vibrant-ink .ace_marker-layer .ace_selected-word {border: 1px solid #6699CC}.ace-vibrant-ink .ace_invisible {color: #404040}.ace-vibrant-ink .ace_keyword,.ace-vibrant-ink .ace_meta {color: #FF6600}.ace-vibrant-ink .ace_constant,.ace-vibrant-ink .ace_constant.ace_character,.ace-vibrant-ink .ace_constant.ace_character.ace_escape,.ace-vibrant-ink .ace_constant.ace_other {color: #339999}.ace-vibrant-ink .ace_constant.ace_numeric {color: #99CC99}.ace-vibrant-ink .ace_invalid,.ace-vibrant-ink .ace_invalid.ace_deprecated {color: #CCFF33;background-color: #000000}.ace-vibrant-ink .ace_fold {background-color: #FFCC00;border-color: #FFFFFF}.ace-vibrant-ink .ace_entity.ace_name.ace_function,.ace-vibrant-ink .ace_support.ace_function,.ace-vibrant-ink .ace_variable {color: #FFCC00}.ace-vibrant-ink .ace_variable.ace_parameter {font-style: italic}.ace-vibrant-ink .ace_string {color: #66FF00}.ace-vibrant-ink .ace_string.ace_regexp {color: #44B4CC}.ace-vibrant-ink .ace_comment {color: #9933CC}.ace-vibrant-ink .ace_entity.ace_other.ace_attribute-name {font-style: italic;color: #99CC99}.ace-vibrant-ink .ace_indent-guide {background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQImWNgYGBgYNDTc/oPAALPAZ7hxlbYAAAAAElFTkSuQmCC) right repeat-y}";var r=e("../lib/dom");r.importCssString(t.cssText,t.cssClass)})