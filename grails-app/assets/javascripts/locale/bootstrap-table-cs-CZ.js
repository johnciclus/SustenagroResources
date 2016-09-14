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
 * Bootstrap Table Czech translation
 * Author: Lukas Kral (monarcha@seznam.cz)
 * Author: Jakub Svestka <svestka1999@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['cs-CZ'] = {
        formatLoadingMessage: function () {
            return 'Čekejte, prosím...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' položek na stránku';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'Zobrazena ' + pageFrom + '. - ' + pageTo + '. položka z celkových ' + totalRows;
        },
        formatSearch: function () {
            return 'Vyhledávání';
        },
        formatNoMatches: function () {
            return 'Nenalezena žádná vyhovující položka';
        },
        formatPaginationSwitch: function () {
            return 'Skrýt/Zobrazit stránkování';
        },
        formatRefresh: function () {
            return 'Aktualizovat';
        },
        formatToggle: function () {
            return 'Přepni';
        },
        formatColumns: function () {
            return 'Sloupce';
        },
        formatAllRows: function () {
            return 'Vše';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['cs-CZ']);

})(jQuery);
