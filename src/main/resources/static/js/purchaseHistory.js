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
                if(url.indexOf("/purchaseHistory/data/query") > 0){
                    let dataMap = result.dataMap;
                    $("#total").text(dataMap['total'].toLocaleString());
                    $("#tariffPay").text(dataMap['tariffPay'].toLocaleString());
                }
                callback(returnData);
            })
        }
    })
    $.extend(option, customObj);
    $(table).DataTable(option);
}

/**
 * 套件初始化
 * select2、datepicker、dataTable
 */
function init() {
    $('.select2').select2({theme: 'bootstrap-5'});
    let url = utils.getHandlerPath("/purchaseHistory/query");
    let customObj = {
        order: [0, 'desc'],
        columns: getColumns()
    };
    $(".date").daterangepicker({
        showDropdowns: true,
        locale: {
            format: "YYYY-MM-DD",
            separator: " 至 ",
            applyLabel: "確定",
            cancelLabel: "取消",
            fromLabel: "起始日期",
            toLabel: "結束日期",
            customRangeLabel: "自訂範圍",
            weekLabel: "周",
            daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
            monthNames: [
                "一月",
                "二月",
                "三月",
                "四月",
                "五月",
                "六月",
                "七月",
                "八月",
                "九月",
                "十月",
                "十一月",
                "十二月"
            ],
            firstDay: 1
        },
        startDate: getToday(),
        endDate: getToday()
    });
    getDataTableQuery(url, $("#dataTable"), $("#queryForm"), customObj);
}

/**
 * 查詢
 */
function query() {
    $("#dataTable").DataTable().ajax.reload();
}

function getToday() {
    // 創建當前日期的 Date 物件
    let today = new Date();
    // 將日期格式化為 "yyyy-mm-dd" 字符串
    let yyyy = today.getFullYear();
    let mm = today.getMonth() + 1; // 月份從 0 開始
    let dd = today.getDate();
    if (mm < 10) {
        mm = '0' + mm;
    } // 月份補 0
    if (dd < 10) {
        dd = '0' + dd;
    } // 日補 0
    return yyyy + '-' + mm + '-' + dd;
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
function dialogOpen(buyNo) {
    $('#historyListModal').modal({backdrop: 'static', keyboard: false});
    $('#historyListModal').modal('show');
    let url = utils.getHandlerPath("/purchaseHistory/data/query");
    let customObj = {
        order: [0, 'desc'],
        columns: historyListColumns()
    };
    $("#buyNo").val(buyNo);
    getDataTableQuery(url, $("#dataTable2"), $("#historyListForm"), customObj);
}

/**
 * 新增/編輯
 * @param actionType
 * @param id   編輯才有
 */
function dialogOpen2(id) {
    let url = "/purchaseHistory/get/" + id;
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath(url),
        cache: false,
    }).done(function (response) {
        $('#historyModal').find('.modal-content').html(response);
    }).always(function () {
        $('#historyModal').find('.select_option').select2();
        $('#historyModal').modal({backdrop: 'static', keyboard: false})
        $('#historyModal').modal('show');
    })
}

/**
 * 取得顯示欄位
 */
function getColumns() {
    return [
        {data: "id", visible: false},
        {data: "purchaseDate", title: "進貨日期", className: "text-center", sortable: false, orderable: false},
        {data: "buyNo", title: "訂單號", className: "text-center", sortable: false, orderable: false},
        {data: "companyOrderNo", title: "廠商訂單號", className: "text-center", sortable: false, orderable: false},
        {
            data: "companyId", title: "品牌", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                return row.company.brand;
            }
        },
        {data: "num", title: "品項數量", className: "text-center", sortable: false, orderable: false},
        {
            data: null, title: "庫存操作", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                let inventory = `<button type='button' class='btn btn-sm btn-success' onclick='saveInventory("${data.buyNo}")'><i class="fa fa-cart-plus"></i>轉入庫存</button>`;
                return data.status === '2' ? inventory : '已轉入';
            }
        },
        {
            data: null,
            title: "操作",
            sortable: false,
            className: "text-center",
            orderable: false,
            "render": function (data, type, row, meta) {
                return `<button type='button' class='btn btn-sm btn-primary' onclick='dialogOpen("${data.buyNo}")'><i class="fa fa-clipboard"></i>明細</button>`;
            }
        }];
}

function historyListColumns() {
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
            data: "sizeId", title: "尺寸", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.sizeChart.size;
            }
        },
        {data: "cnt", title: "數量", className: "text-center"},
        {data: "price", title: "價格", className: "text-center"},
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
                let edit = `<button type='button' class='btn btn-sm btn-primary' onclick='dialogOpen2(${id})'><i class="fa fa-pencil-alt"></i>編輯</button>`;
                let del = `<button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(${id})'><i class="fa fa-trash"></i>刪除</button>`;
                return edit + "&nbsp;&nbsp;" + del;
            }
        }
    ];
}


/**
 * 刪除選擇行
 */
function deleteRow(id) {
    let url = "/purchaseHistory/delete/" + id
    let table = $("#dataTable2");
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
 * 儲存
 */
function saveInventory(buyNo) {
    let param = {buyNo: buyNo};
    $.ajax({
        type: "post",
        url: utils.getHandlerPath("/purchaseHistory/save/inventory"),
        data: JSON.stringify(param),
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    }).done(function (response) {
        if (response.code === 200) {
            $("#dataTable").DataTable().ajax.reload(null, true);
            utils.msgAlert(utils.msg.addSuccess, "success");
        }
    })
}

/**
 * 儲存
 */
function save() {
    $('#historyForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'price': {
                required: true,
                number: true
            }
        }, errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let param = $("#historyForm").serializeObject();
            $.ajax({
                type: "post",
                url: utils.getHandlerPath("/purchaseHistory/update"),
                data: JSON.stringify(param),
                cache: false,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
            }).done(function (response) {
                if (response.code === 200) {
                    $("#dataTable2").DataTable().ajax.reload(null, false);
                    $('#historyModal').modal('hide');
                    utils.msgAlert(utils.msg.editSuccess, "success");
                }
            })
        }
    });
}

$(function () {
    init();
});