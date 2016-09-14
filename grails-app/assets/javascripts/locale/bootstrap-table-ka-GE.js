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
 * Bootstrap Table Georgian translation
 * Author: Levan Lotuashvili <l.lotuashvili@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['ka-GE'] = {
        formatLoadingMessage: function() {
            return 'იტვირთება, გთხოვთ მოიცადოთ...';
        },
        formatRecordsPerPage: function(pageNumber) {
            return pageNumber + ' ჩანაწერი თითო გვერდზე';
        },
        formatShowingRows: function(pageFrom, pageTo, totalRows) {
            return 'ნაჩვენებია ' + pageFrom + '-დან ' + pageTo + '-მდე ჩანაწერი ჯამური ' + totalRows + '-დან';
        },
        formatSearch: function() {
            return 'ძებნა';
        },
        formatNoMatches: function() {
            return 'მონაცემები არ არის';
        },
        formatPaginationSwitch: function() {
            return 'გვერდების გადამრთველის დამალვა/გამოჩენა';
        },
        formatRefresh: function() {
            return 'განახლება';
        },
        formatToggle: function() {
            return 'ჩართვა/გამორთვა';
        },
        formatColumns: function() {
            return 'სვეტები';
        }
    };
    
    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['ka-GE']);

})(jQuery);
