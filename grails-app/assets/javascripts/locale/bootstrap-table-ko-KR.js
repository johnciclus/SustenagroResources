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
 * Bootstrap Table Korean translation
 * Author: Yi Tae-Hyeong (jsonobject@gmail.com)
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['ko-KR'] = {
        formatLoadingMessage: function () {
            return '데이터를 불러오는 중입니다...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return '페이지 당 ' + pageNumber + '개 데이터 출력';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return '전체 ' + totalRows + '개 중 ' + pageFrom + '~' + pageTo + '번째 데이터 출력,';
        },
        formatSearch: function () {
            return '검색';
        },
        formatNoMatches: function () {
            return '조회된 데이터가 없습니다.';
        },
        formatRefresh: function () {
            return '새로 고침';
        },
        formatToggle: function () {
            return '전환';
        },
        formatColumns: function () {
            return '컬럼 필터링';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['ko-KR']);

})(jQuery);