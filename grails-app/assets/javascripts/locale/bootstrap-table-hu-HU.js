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
 * Bootstrap Table Hungarian translation
 * Author: Nagy Gergely <info@nagygergely.eu>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['hu-HU'] = {
        formatLoadingMessage: function () {
            return 'Betöltés, kérem várjon...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' rekord per oldal';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'Megjelenítve ' + pageFrom + ' - ' + pageTo + ' / ' + totalRows + ' összesen';
        },
        formatSearch: function () {
            return 'Keresés';
        },
        formatNoMatches: function () {
            return 'Nincs találat';
        },
        formatPaginationSwitch: function () {
            return 'Lapozó elrejtése/megjelenítése';
        },
        formatRefresh: function () {
            return 'Frissítés';
        },
        formatToggle: function () {
            return 'Összecsuk/Kinyit';
        },
        formatColumns: function () {
            return 'Oszlopok';
        },
        formatAllRows: function () {
            return 'Összes';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['hu-HU']);

})(jQuery);
