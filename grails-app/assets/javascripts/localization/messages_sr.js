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

(function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( ["jquery", "../jquery.validate"], factory );
	} else if (typeof module === "object" && module.exports) {
		module.exports = factory( require( "jquery" ) );
	} else {
		factory( jQuery );
	}
}(function( $ ) {

/*
 * Translated default messages for the jQuery validation plugin.
 * Locale: SR (Serbian; српски језик)
 */
$.extend( $.validator.messages, {
	required: "Поље је обавезно.",
	remote: "Средите ово поље.",
	email: "Унесите исправну и-мејл адресу.",
	url: "Унесите исправан URL.",
	date: "Унесите исправан датум.",
	dateISO: "Унесите исправан датум (ISO).",
	number: "Унесите исправан број.",
	digits: "Унесите само цифе.",
	creditcard: "Унесите исправан број кредитне картице.",
	equalTo: "Унесите исту вредност поново.",
	extension: "Унесите вредност са одговарајућом екстензијом.",
	maxlength: $.validator.format( "Унесите мање од {0} карактера." ),
	minlength: $.validator.format( "Унесите барем {0} карактера." ),
	rangelength: $.validator.format( "Унесите вредност дугачку између {0} и {1} карактера." ),
	range: $.validator.format( "Унесите вредност између {0} и {1}." ),
	max: $.validator.format( "Унесите вредност мању или једнаку {0}." ),
	min: $.validator.format( "Унесите вредност већу или једнаку {0}." )
} );

}));