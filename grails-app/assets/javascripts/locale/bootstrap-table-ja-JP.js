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
 * Bootstrap Table Japanese translation
 * Author: Azamshul Azizy <azamshul@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['ja-JP'] = {
        formatLoadingMessage: function () {
            return '読み込み中です。少々お待ちください。';
        },
        formatRecordsPerPage: function (pageNumber) {
            return 'ページ当たり最大' + pageNumber + '件';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '全' + totalRows + '件から、'+ pageFrom + 'から' + pageTo + '件目まで表示しています';
        },
        formatSearch: function () {
            return '検索';
        },
        formatNoMatches: function () {
            return '該当するレコードが見つかりません';
        },
        formatPaginationSwitch: function () {
            return 'ページ数を表示・非表示';
        },
        formatRefresh: function () {
            return '更新';
        },
        formatToggle: function () {
            return 'トグル';
        },
        formatColumns: function () {
            return '列';
        },
        formatAllRows: function () {
            return 'すべて';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['ja-JP']);

})(jQuery);