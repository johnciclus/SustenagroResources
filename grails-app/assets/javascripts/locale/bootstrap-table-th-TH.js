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
 * Bootstrap Table Thai translation
 * Author: Monchai S.<monchais@gmail.com>
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['th-TH'] = {
        formatLoadingMessage: function () {
            return 'กำลังโหลดข้อมูล, กรุณารอสักครู่...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' รายการต่อหน้า';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'รายการที่ ' + pageFrom + ' ถึง ' + pageTo + ' จากทั้งหมด ' + totalRows + ' รายการ';
        },
        formatSearch: function () {
            return 'ค้นหา';
        },
        formatNoMatches: function () {
            return 'ไม่พบรายการที่ค้นหา !';
        },
        formatRefresh: function () {
            return 'รีเฟรส';
        },
        formatToggle: function () {
            return 'สลับมุมมอง';
        },
        formatColumns: function () {
            return 'คอลัมน์';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['th-TH']);

})(jQuery);
