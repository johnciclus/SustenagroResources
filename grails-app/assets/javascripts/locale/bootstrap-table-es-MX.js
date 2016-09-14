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
 * Bootstrap Table Spanish (México) translation (Obtenido de traducción de Argentina)
 * Author: Felix Vera (felix.vera@gmail.com) 
 * Copiado: Mauricio Vera (mauricioa.vera@gmail.com)
 */
(function ($) {
    'use strict';

    $.fn.bootstrapTable.locales['es-MX'] = {
        formatLoadingMessage: function () {
            return 'Cargando, espere por favor...';
        },
        formatRecordsPerPage: function (pageNumber) {
            return pageNumber + ' registros por página';
        },
        formatShowingRows: function (pageFrom, pageTo, totalRows) {
            return 'Mostrando ' + pageFrom + ' a ' + pageTo + ' de ' + totalRows + ' filas';
        },
        formatSearch: function () {
            return 'Buscar';
        },
        formatNoMatches: function () {
            return 'No se encontraron registros';
        },
        formatAllRows: function () {
            return 'Todo';
        }
    };

    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales['es-MX']);

})(jQuery);
