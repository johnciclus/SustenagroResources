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
 * Locale: HY_AM (Armenian; հայերեն լեզու)
 */
$.extend( $.validator.messages, {
	required: "Պարտադիր լրացման դաշտ",
	remote: "Ներմուծեք ճիշտ արժեքը",
	email: "Ներմուծեք վավեր էլեկտրոնային փոստի հասցե",
	url: "Ներմուծեք վավեր URL",
	date: "Ներմուծեք վավեր ամսաթիվ",
	dateISO: "Ներմուծեք ISO ֆորմատով վավեր ամսաթիվ։",
	number: "Ներմուծեք թիվ",
	digits: "Ներմուծեք միայն թվեր",
	creditcard: "Ներմուծեք ճիշտ բանկային քարտի համար",
	equalTo: "Ներմուծեք միևնուն արժեքը ևս մեկ անգամ",
	extension: "Ընտրեք ճիշտ ընդլանումով ֆայլ",
	maxlength: $.validator.format( "Ներմուծեք ոչ ավել քան {0} նիշ" ),
	minlength: $.validator.format( "Ներմուծեք ոչ պակաս քան {0} նիշ" ),
	rangelength: $.validator.format( "Ներմուծեք {0}֊ից {1} երկարությամբ արժեք" ),
	range: $.validator.format( "Ներմուծեք թիվ {0}֊ից {1} միջակայքում" ),
	max: $.validator.format( "Ներմուծեք թիվ, որը փոքր կամ հավասար է {0}֊ին" ),
	min: $.validator.format( "Ներմուծեք թիվ, որը մեծ կամ հավասար է {0}֊ին" )
} );

}));