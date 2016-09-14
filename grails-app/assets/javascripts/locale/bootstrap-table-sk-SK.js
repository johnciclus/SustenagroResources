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

/**
 * Bootstrap Table Slovak translation
 * Author: Jozef Dúc<jozef.d13@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['sk-SK'] = {
        formatLoadingMessage: function () {
            return 'Prosím čakajte ...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' záznamov na stranu';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'Zobrazená ' + pageFrom + '. - ' + pageTo + '. položka z celkových ' + totalRows;
        },
        formatSearch: function () {
            return 'Vyhľadávanie';
        },
        formatNoMatches: function () {
            return 'Nenájdená žiadna vyhovujúca položka';
        },
        formatRefresh: function () {
            return 'Obnoviť';
        },
        formatToggle: function () {
            return 'Prepni';
        },
        formatColumns: function () {
            return 'Stĺpce';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['sk-SK']);

})(jQuery);
