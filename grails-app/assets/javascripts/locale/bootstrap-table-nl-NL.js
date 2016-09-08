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
 * Bootstrap Table Dutch translation
 * Author: Your Name <info@a2hankes.nl>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['nl-NL'] = {
        formatLoadingMessage: function () {
            return 'Laden, even geduld...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' records per pagina';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'Toon ' + pageFrom + ' tot ' + pageTo + ' van ' + totalRows + ' record' + ((totalRows > 1) ? 's' : '');
        },
        formatDetailPagination: function (totalRows) {
            return 'Toon ' + totalRows + ' record' + ((totalRows > 1) ? 's' : '');
        },
        formatSearch: function () {
            return 'Zoeken';
        },
        formatNoMatches: function () {
            return 'Geen resultaten gevonden';
        },
        formatRefresh: function () {
           return 'Vernieuwen';
        },
        formatToggle: function () {
          return 'Omschakelen';
        },
        formatColumns: function () {
          return 'Kolommen';
        },
        formatAllRows: function () {
          return 'Alle';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['nl-NL']);

})(jQuery);
