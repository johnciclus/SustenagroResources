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

ace.define("ace/ext/themelist",["require","exports","module","ace/lib/fixoldbrowsers"],function(e,t,n){"use strict";e("ace/lib/fixoldbrowsers");var r=[["Chrome"],["Clouds"],["Crimson Editor"],["Dawn"],["Dreamweaver"],["Eclipse"],["GitHub"],["Solarized Light"],["TextMate"],["Tomorrow"],["XCode"],["Kuroir"],["KatzenMilch"],["Ambiance","ambiance","dark"],["Chaos","chaos","dark"],["Clouds Midnight","clouds_midnight","dark"],["Cobalt","cobalt","dark"],["idle Fingers","idle_fingers","dark"],["krTheme","kr_theme","dark"],["Merbivore","merbivore","dark"],["Merbivore Soft","merbivore_soft","dark"],["Mono Industrial","mono_industrial","dark"],["Monokai","monokai","dark"],["Pastel on dark","pastel_on_dark","dark"],["Solarized Dark","solarized_dark","dark"],["Terminal","terminal","dark"],["Tomorrow Night","tomorrow_night","dark"],["Tomorrow Night Blue","tomorrow_night_blue","dark"],["Tomorrow Night Bright","tomorrow_night_bright","dark"],["Tomorrow Night 80s","tomorrow_night_eighties","dark"],["Twilight","twilight","dark"],["Vibrant Ink","vibrant_ink","dark"]];t.themesByName={},t.themes=r.map(function(e){var n=e[1]||e[0].replace(/ /g,"_").toLowerCase(),r={caption:e[0],theme:"ace/theme/"+n,isDark:e[2]=="dark",name:n};return t.themesByName[n]=r,r})});
                (function() {
                    ace.require(["ace/ext/themelist"], function() {});
                })();
            