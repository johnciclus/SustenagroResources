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
 * Bootstrap Table Hebrew translation
 * Author: legshooter
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['he-IL'] = {
        formatLoadingMessage: function () {
            return 'טוען, נא להמתין...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' שורות בעמוד';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'מציג ' + pageFrom + ' עד ' + pageTo + ' מ-' + totalRows + ' שורות';
        },
        formatSearch: function () {
            return 'חיפוש';
        },
        formatNoMatches: function () {
            return 'לא נמצאו רשומות תואמות';
        },
        formatPaginationSwitch: function () {
            return 'הסתר/הצג מספור דפים';
        },
        formatRefresh: function () {
            return 'רענן';
        },
        formatToggle: function () {
            return 'החלף תצוגה';
        },
        formatColumns: function () {
            return 'עמודות';
        },
        formatAllRows: function () {
            return 'הכל';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['he-IL']);

})(jQuery);
