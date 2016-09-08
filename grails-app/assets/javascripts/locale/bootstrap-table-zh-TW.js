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
 * Bootstrap Table Chinese translation
 * Author: Zhixin Wen<wenzhixin2010@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['zh-TW'] = {
        formatLoadingMessage: function () {
            return '正在努力地載入資料，請稍候……';
        },
        formatRecordsPerPage: function (pageNumber) {
            return '每頁顯示 ' + pageNumber + ' 項記錄';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '顯示第 ' + pageFrom + ' 到第 ' + pageTo + ' 項記錄，總共 ' + totalRows + ' 項記錄';
        },
        formatSearch: function () {
            return '搜尋';
        },
        formatNoMatches: function () {
            return '沒有找到符合的結果';
        },
        formatPaginationSwitch: function () {
            return '隱藏/顯示分頁';
        },
        formatRefresh: function () {
            return '重新整理';
        },
        formatToggle: function () {
            return '切換';
        },
        formatColumns: function () {
            return '列';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['zh-TW']);

})(jQuery);
