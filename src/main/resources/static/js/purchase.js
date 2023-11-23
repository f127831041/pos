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
function deleteRow(id) {
    let url = "/purchase/delete/" + id
    let table = $("#dataTable");
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
    let url = utils.getHandlerPath("/purchase/query");
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
    let companyId = -1;
    let tariff = -1;
    //新增的時候需要取本次進貨品牌 & 稅率
    if (id === -1) {
        companyId = $("#companyId").val();
        if (utils.isEmpty(companyId)) {
            utils.msgRemind("請先選擇進貨品牌！", "warning");
            return;
        }
        tariff = $("#tariff").val();
        if (utils.isEmpty(tariff)) {
            utils.msgRemind("請先輸入稅率！", "warning");
            return;
        }
    }
    let url = "/purchase/get/" + id + "/" + companyId;
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath(url),
        cache: false,
    }).done(function (response) {
        $('#purchaseModal').find('.modal-content').html(response);
    }).always(function () {
        $('#purchaseModal').modal({backdrop: 'static', keyboard: false})
        $('#purchaseModal').modal('show');
        $('#purchaseModal').find('.select2').select2({
            theme: "bootstrap-5",
            dropdownParent: $('#purchaseModal')
        });
        if (id === -1) {
            $("#purchaseForm").find("input[name='tariff']").val(tariff);
        }
    })
}

function changeProdNo(val) {
    if (utils.isEmpty(val)) {
        $("#price").val(0);
        return;
    }
    let product = productObj[val];
    $("#price").val(product.purchasePrice);

}

function changeCompany() {
    let companyId = $("#queryForm").find("select[name='companyId']").val();
    let params = {
        companyId: companyId,
    }
    $.ajax({
        type: "POST",
        url: utils.getHandlerPath("/product/get/data"),
        data: JSON.stringify(params),
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    }).done(function (response) {
        if (response.code === 200) {
            let result = response.data;
            let prodNoHtml = `<option selected="selected" value="">- 請選擇 -</option>`;
            for (let prodNo of result.prodNoList) {
                prodNoHtml += `<option value="${prodNo}">${prodNo}</option>`
            }
            $("#queryForm").find("select[name='prodNo']").empty().append(prodNoHtml);
        }
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
        {
            data: null, title: "品項", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                return row.product.name;
            }
        },
        {
            data: null, title: "圖案", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                return row.product.design;
            }
        },
        {
            data: null, title: "顏色", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                return row.product.color;
            }
        },
        {
            data: "sizeId", title: "尺寸", className: "text-center", "render": function (data, type, row, meta) {
                return row.sizeChart.size;
            }
        },
        {data: "cnt", title: "數量", className: "text-center"},
        {
            data: "price", title: "價格", className: "text-center", "render": function (data, type, row, meta) {
                return data.toLocaleString();
            }
        },
        {data: "tariff", title: "稅率%", className: "text-center"},
        {
            data: null, title: "稅前金額", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                return (row.price * row.cnt).toLocaleString();
            }
        },
        {
            data: null, title: "稅後金額", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                let sum = row.price * row.cnt;
                let tariff = row.tariff;
                let tariffPay = sum * (0.01 * tariff);
                return (sum + tariffPay).toLocaleString();
            }
        },
        {
            data: null, title: "操作", sortable: false, className: "text-center", orderable: false,
            "render": function (data, type, row, meta) {
                let id = data.id;
                let edit = `<button type='button' class='btn btn-sm btn-primary' onclick='dialogOpen(${id})'><i class="fa fa-pencil-alt"></i>編輯</button>`;
                let del = `<button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(${id})'><i class="fa fa-trash"></i>刪除</button>`;
                return edit + "&nbsp;&nbsp;" + del;
            }
        }];
}

/**
 * 儲存
 */
function save() {
    let actionType = $('#action').val();
    $('#purchaseForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'companyId': {
                required: true
            },
            'prodNo': {
                required: true
            },
            'sizeId': {
                required: true
            },
            'cnt': {
                required: true,
                number: true
            },
            'price': {
                required: true,
                number: true
            }
        }, errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let msg = actionType === 'add' ? utils.msg.addSuccess : utils.msg.editSuccess;
            let url = actionType === 'add' ? "/purchase/add" : "/purchase/update";
            let reset = actionType === 'add';
            let param = $("#purchaseForm").serializeObject();
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
                    $('#purchaseModal').modal('hide');
                    utils.msgAlert(msg, "success");
                }
            })
        }
    });
}

function sendOrder(){
    $('#dateModal').modal({backdrop: 'static', keyboard: false})
    $('#dateModal').modal('show');
}

function saveBuy() {
    let param = $("#dateForm").serializeObject();
    let url = "/purchase/save/buy";
    $.ajax({
        type: "POST",
        url: utils.getHandlerPath(url),
        cache: false,
        data: JSON.stringify(param),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    }).done(function (response) {
        if (response.code === 200) {
            $("#buyNo").val(response.data);
            $('#dateModal').modal('hide');
            utils.msgAlert(utils.msg.addSuccess, "success", msg);
        }
    })

    let msg = function () {
        utils.msgConfirm('是否直接轉入庫存？', saveInventory, deleteBuy);
    }

    let saveInventory = function () {
        let param = {buyNo: $("#buyNo").val()};
        $.ajax({
            type: "post",
            url: utils.getHandlerPath("/purchase/save/inventory"),
            data: JSON.stringify(param),
            cache: false,
            dataType: 'json',
            contentType: "application/json; charset=utf-8",
        }).done(function (response) {
            if (response.code === 200) {
                $("#buyNo").val('');
                $("#dataTable").DataTable().ajax.reload(null, true);
                utils.msgAlert(utils.msg.addSuccess, "success");
            }
        })
    }
}

function deleteBuy() {
    let url = "/purchase/delete/buy";
    $.ajax({
        type: "delete",
        url: utils.getHandlerPath(url),
        cache: false,
        dataType: 'json'
    }).done(function (response) {
        if (response.code === 200) {
            $("#dataTable").DataTable().ajax.reload(null, true);
        }
    })
}

$(function () {
    init();
});