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
 * Bootstrap Table Romanian translation
 * Author: cristake <cristianiosif@me.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['ro-RO'] = {
        formatLoadingMessage: function () {
            return 'Se incarca, va rugam asteptati...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' inregistrari pe pagina';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'Arata de la ' + pageFrom + ' pana la ' + pageTo + ' din ' + totalRows + ' randuri';
        },
        formatSearch: function () {
            return 'Cauta';
        },
        formatNoMatches: function () {
            return 'Nu au fost gasite inregistrari';
        },
        formatPaginationSwitch: function () {
            return 'Ascunde/Arata paginatia';
        },
        formatRefresh: function () {
            return 'Reincarca';
        },
        formatToggle: function () {
            return 'Comuta';
        },
        formatColumns: function () {
            return 'Coloane';
        },
        formatAllRows: function () {
            return 'Toate';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['ro-RO']);

})(jQuery);