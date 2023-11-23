/**
 * dataTable查詢
 * @param url
 * @param table
 * @param form
 * @param customObj
 */
function getDataTableQuery(url, table, form, customObj) {
    let option = $.extend(utils.dataTableOption(), {
        ajax: function (data, callback, settings) {
            //封装请求参数
            let param = getQueryCondition(data, form);
            $.ajax({
                type: 'POST', url: url, cache: false,	//禁用缓存
                data: JSON.stringify(param),	//已封装的参数
                dataType: 'json', contentType: "application/json; charset=utf-8",
            }).done(function (response) {
                let result = response.data;
                //封装返回数据
                let returnData = {
                    draw: result.draw,
                    recordsTotal: result.pageCount,
                    recordsFiltered: result.totalCount,
                    data: result.data
                };
                callback(returnData);
            })
        }
    })
    $.extend(option, customObj);
    $(table).DataTable(option);
}

/**
 * 刪除選擇行
 */
function deleteRow(target, id) {
    let url = "/" + target + "/delete/" + id
    let table = target === 'product' ? $("#dataTable") : $("#dataTable2");
    let sendDelete = function () {
        $.ajax({
            type: "delete",
            url: utils.getHandlerPath(url),
            cache: false,
            dataType: 'json',
        }).done(function (response) {
            if (response.code === 200) {
                $(table).DataTable().ajax.reload(null, false);
                utils.msgAlert(utils.msg.deleteSuccess, "success");
            }
        })
    }
    utils.msgConfirm(utils.msg.confirmDelete, sendDelete);
}

/**
 * 套件初始化
 * select2、datepicker、dataTable
 */
function init() {
    $('.select2').select2({theme: 'bootstrap-5'});
    let url = utils.getHandlerPath("/product/query");
    let customObj = {
        order: [0, 'desc'],
        columns: getColumns()
    };
    getDataTableQuery(url, $("#dataTable"), $("#queryForm"), customObj);
}

/**
 * 查詢
 */
function query() {
    $("#dataTable").DataTable().ajax.reload();
}

/**
 * 取得查詢條件
 * @param data
 * @param form
 * @returns {{startIndex: *, pageSize: *, draw: null, orderDir: *, orderColumn: string}}
 */
function getQueryCondition(data, form) {
    //组装排序参数
    let orderColumn = '';
    if (data.order && data.order.length && data.order[0]) {
        let index = data.order[0].column;
        orderColumn = data.columns[index].data;
    }
    let formData = $(form).serializeObject();
    let param = {
        'page': data.start,
        'pageSize': data.length,
        'draw': data.draw,
        'orderDir': data.order[0].dir,
        'orderColumn': orderColumn,
    }
    $.extend(param, formData);
    return param;
}

/**
 * 新增/編輯
 * @param actionType
 * @param id   編輯才有
 */
function dialogOpen(id) {
    let url = "/product/get/" + id;
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath(url),
        cache: false,
    }).done(function (response) {
        $('#productModal').find('.modal-content').html(response);
    }).always(function () {
        $('#productModal').modal({backdrop: 'static', keyboard: false})
        $('#productModal').modal('show');
        $('#productModal').find('.select2').select2({
            theme: "bootstrap-5",
            dropdownParent: $('#productModal')
        });
    })
}

/**
 * 取得顯示欄位
 */
function getColumns() {
    return [
        {data: "id", visible: false},
        {
            data: "companyId", title: "品牌", className: "text-center", "render": function (data, type, row, meta) {
                return row.company.brand;
            }
        },
        {data: "prodNo", title: "型號", className: "text-center"},
        {data: "name", title: "品項", className: "text-center"},
        {data: "design", title: "圖案", className: "text-center"},
        {data: "color", title: "顏色", className: "text-center"},
        {
            data: "purchasePrice", title: "進貨價格", className: "text-center",
            "render": function (data, type, row, meta) {
                return data.toLocaleString();
            }
        },
        {
            data: "price", title: "販售價格", className: "text-center",
            "render": function (data, type, row, meta) {
                return data.toLocaleString();
            }
        },
        {
            data: "status", title: "狀態", className: "text-center",
            "render": function (data, type, row, meta) {
                return data === '1' ? '上架' : '下架';
            }
        },
        {
            data: null, title: "操作", sortable: false, className: "text-center", orderable: false, width: '30%',
            "render": function (data, type, row, meta) {
                let id = data.id;
                // let barcode = `<button type='button' class='btn btn-sm btn-dark' onclick=''><i class="fa fa-barcode"></i>條碼</button>`;
                let edit = `<button type='button' class='btn btn-sm btn-primary' onclick='dialogOpen(${id})'><i class="fa fa-pencil-alt"></i>編輯</button>`;
                let del = `<button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(\"product\",${id})'><i class="fa fa-trash"></i>刪除</button>`;
                return edit + "&nbsp;&nbsp;" + del;
            }
        }];
}

/**
 * 儲存
 */
function save() {
    let actionType = $('#action').val();
    $('#productForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'brand': {
                required: true
            }, 'prodNo': {
                required: true
            }, 'name': {
                required: true
            }, 'price': {
                required: true,
                number: true
            }, 'purchasePrice': {
                required: true,
                number: true
            }, 'status': {
                required: true,
            },

        }, errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let msg = actionType === 'add' ? utils.msg.addSuccess : utils.msg.editSuccess;
            let url = actionType === 'add' ? "/product/add" : "/product/update";
            let reset = actionType === 'add';
            let param = $("#productForm").serializeObject();
            $.ajax({
                type: "post",
                url: utils.getHandlerPath(url),
                data: JSON.stringify(param),
                cache: false,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
            }).done(function (response) {
                if (response.code === 200) {
                    $("#dataTable").DataTable().ajax.reload(null, reset);
                    $('#productModal').modal('hide');
                    utils.msgAlert(msg, "success");
                }
            })
        }
    });
}

$(function () {
    init();
});