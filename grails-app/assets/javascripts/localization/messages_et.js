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
 * Locale: ET (Estonian; eesti, eesti keel)
 */
$.extend( $.validator.messages, {
	required: "See väli peab olema täidetud.",
	maxlength: $.validator.format( "Palun sisestage vähem kui {0} tähemärki." ),
	minlength: $.validator.format( "Palun sisestage vähemalt {0} tähemärki." ),
	rangelength: $.validator.format( "Palun sisestage väärtus vahemikus {0} kuni {1} tähemärki." ),
	email: "Palun sisestage korrektne e-maili aadress.",
	url: "Palun sisestage korrektne URL.",
	date: "Palun sisestage korrektne kuupäev.",
	dateISO: "Palun sisestage korrektne kuupäev (YYYY-MM-DD).",
	number: "Palun sisestage korrektne number.",
	digits: "Palun sisestage ainult numbreid.",
	equalTo: "Palun sisestage sama väärtus uuesti.",
	range: $.validator.format( "Palun sisestage väärtus vahemikus {0} kuni {1}." ),
	max: $.validator.format( "Palun sisestage väärtus, mis on väiksem või võrdne arvuga {0}." ),
	min: $.validator.format( "Palun sisestage väärtus, mis on suurem või võrdne arvuga {0}." ),
	creditcard: "Palun sisestage korrektne krediitkaardi number."
} );

}));