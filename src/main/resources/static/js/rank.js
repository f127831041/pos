
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

function changeCompany(val) {
    let params = {
        companyId: val,
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
            let html = `<option selected="selected" value="">- 請選擇 -</option>`;
            for (let product of result.productList) {
                let design = utils.isEmpty(product.design) ? '' : '-' + product.design;
                let color =  utils.isEmpty(product.color) ? '' : '-' + product.color;
                html += `<option value="${product.id}">${product.prodNo + '-' + product.name + design + color}</option>`
            }
            $("#productId").empty().append(html);
        }
    })
}

/**
 * 刪除選擇行
 */
function deleteRow(id) {
    let url = "/company/delete/"+id
    let table =  $("#dataTable");
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
    let url = utils.getHandlerPath("/rank/query");
    let customObj = {
        order: [0, 'desc'],
        columns: getColumns()
    };
    getDataTableQuery(url, $("#dataTable"), $("#queryForm"), customObj);
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
    let url = "/company/get/" + id;
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath(url),
        cache: false,
    }).done(function (response) {
        $('#companyModal').find('.modal-content').html(response);
    }).always(function () {
        $('#companyModal').find('.select_option').select2();
        $('#companyModal').modal({backdrop: 'static', keyboard: false})
        $('#companyModal').modal('show');
    })
}

/**
 * 取得顯示欄位
 */
function getColumns() {
    return [
        {data: "id", visible: false},
        {data: "saleDate", title: "銷售日期", className: "text-center"},
        {
            data: null, title: "品牌", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.company.brand;
            }
        },
        {data: "null", title: "型號", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.product.prodNo;
            }
        },
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
        {data: "cnt", title: "數量", className: "text-center"}
       ];
}

/**
 * 儲存
 */
function save() {
    $('#companyForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'name': {
                required: true
            }
        }, errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let actionType = $('#action').val();
            let msg = actionType === 'add' ? utils.msg.addSuccess : utils.msg.editSuccess;
            let url = actionType === 'add' ? "/company/add" : "/company/update";
            let reset = actionType === 'add';
            let param = $("#companyForm").serializeObject();
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
                    $('#companyModal').modal('hide');
                    utils.msgAlert(msg, "success");
                }
            })
        }
    });
}

$(function () {
    init();
});