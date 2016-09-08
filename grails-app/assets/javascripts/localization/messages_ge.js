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

/**
 * @author  @tatocaster <kutaliatato@gmail.com>
 * Translated default messages for the jQuery validation plugin.
 * Locale: GE (Georgian; ქართული)
 */
$.extend( $.validator.messages, {
	required: "ეს ველი სავალდებულოა",
	remote: "გთხოვთ შეასწოროთ.",
	email: "გთხოვთ შეიყვანოთ სწორი ფორმატით.",
	url: "გთხოვთ შეიყვანოთ სწორი ფორმატით.",
	date: "გთხოვთ შეიყვანოთ სწორი თარიღი.",
	dateISO: "გთხოვთ შეიყვანოთ სწორი ფორმატით ( ISO ).",
	number: "გთხოვთ შეიყვანოთ რიცხვი.",
	digits: "დაშვებულია მხოლოდ ციფრები.",
	creditcard: "გთხოვთ შეიყვანოთ სწორი ფორმატის ბარათის კოდი.",
	equalTo: "გთხოვთ შეიყვანოთ იგივე მნიშვნელობა.",
	maxlength: $.validator.format( "გთხოვთ შეიყვანოთ არა უმეტეს {0} სიმბოლოსი." ),
	minlength: $.validator.format( "შეიყვანეთ მინიმუმ {0} სიმბოლო." ),
	rangelength: $.validator.format( "გთხოვთ შეიყვანოთ {0} -დან {1} -მდე რაოდენობის სიმბოლოები." ),
	range: $.validator.format( "შეიყვანეთ {0} -სა {1} -ს შორის." ),
	max: $.validator.format( "გთხოვთ შეიყვანოთ მნიშვნელობა ნაკლები ან ტოლი {0} -ს." ),
	min: $.validator.format( "გთხოვთ შეიყვანოთ მნიშვნელობა მეტი ან ტოლი {0} -ს." )
} );

}));