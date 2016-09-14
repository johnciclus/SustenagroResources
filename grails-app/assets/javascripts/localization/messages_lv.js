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
 * Locale: LV (Latvian; latviešu valoda)
 */
$.extend( $.validator.messages, {
	required: "Šis lauks ir obligāts.",
	remote: "Lūdzu, pārbaudiet šo lauku.",
	email: "Lūdzu, ievadiet derīgu e-pasta adresi.",
	url: "Lūdzu, ievadiet derīgu URL adresi.",
	date: "Lūdzu, ievadiet derīgu datumu.",
	dateISO: "Lūdzu, ievadiet derīgu datumu (ISO).",
	number: "Lūdzu, ievadiet derīgu numuru.",
	digits: "Lūdzu, ievadiet tikai ciparus.",
	creditcard: "Lūdzu, ievadiet derīgu kredītkartes numuru.",
	equalTo: "Lūdzu, ievadiet to pašu vēlreiz.",
	extension: "Lūdzu, ievadiet vērtību ar derīgu paplašinājumu.",
	maxlength: $.validator.format( "Lūdzu, ievadiet ne vairāk kā {0} rakstzīmes." ),
	minlength: $.validator.format( "Lūdzu, ievadiet vismaz {0} rakstzīmes." ),
	rangelength: $.validator.format( "Lūdzu ievadiet {0} līdz {1} rakstzīmes." ),
	range: $.validator.format( "Lūdzu, ievadiet skaitli no {0} līdz {1}." ),
	max: $.validator.format( "Lūdzu, ievadiet skaitli, kurš ir mazāks vai vienāds ar {0}." ),
	min: $.validator.format( "Lūdzu, ievadiet skaitli, kurš ir lielāks vai vienāds ar {0}." )
} );

}));