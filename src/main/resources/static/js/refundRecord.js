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
                if(url.indexOf("/refundRecord/query") > 0){
                    let dataMap = result.dataMap;
                    $("#sum").text(dataMap['total'].toLocaleString());
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

    let url = utils.getHandlerPath("/refund/query");
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
    $('#recordListModal').modal({backdrop: 'static', keyboard: false});
    $('#recordListModal').modal('show');
    let url = utils.getHandlerPath("/refundRecord/query");
    $("#refundId").val(id);
    let customObj = {
        order: [0, 'desc'],
        columns: getRecordColumns()
    };
    getDataTableQuery(url, $("#dataTable2"), $("#recordListForm"), customObj);
}

/**
 * 取得顯示欄位
 */
function getColumns() {
    return [
        {data: "id", visible: false},
        {
            data: "createDateTime", title: "消費日", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.createDate;
            }
        },
        {
            data: null, title: "會員", className: "text-center",
            "render": function (data, type, row, meta) {
                return utils.isEmpty(row.member.cname) ? "" : row.member.cname;
            }
        },
        {
            data: "total", title: "消費金額", className: "text-center",
            "render": function (data, type, row, meta) {
                return data.toLocaleString();
            }
        },
        {
            data: "bonus", title: "已使用紅利", className: "text-center",
            "render": function (data, type, row, meta) {
                return data.toLocaleString();
            }
        },
        {
            data: "payMethod", title: "支付方式", className: "text-center",
            "render": function (data, type, row, meta) {
                return data === '1' ? '現金': data === '2' ? 'Line Pay' : '信用卡';
            }
        },
        {
            data: "pay", title: "結帳金額", className: "text-center",
            "render": function (data, type, row, meta) {
                return data.toLocaleString();
            }
        },
        {
            data: null, title: "操作", sortable: false, className: "text-center", orderable: false,
            "render": function (data, type, row, meta) {
                return `<button type='button' class='btn btn-sm btn-primary' onclick='dialogOpen("${data.id}")'><i class="fa fa-clipboard"></i>明細</button>`;
            }
        }];
}

/**
 * 取得顯示欄位
 */
function getRecordColumns() {
    return [
        {data: "id", visible: false},
        {
            data: "companyId", title: "品牌", className: "text-center", "render": function (data, type, row, meta) {
                return row.company.brand;
            }
        },
        {data: null, title: "型號", className: "text-center", sortable: false, orderable: false,
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
        {
            data: "sizeId", title: "尺寸", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.sizeChart.size;
            }
        },
        {data: "cnt", title: "數量", className: "text-center"},
        {
            data: null, title: "總金額", className: "text-center", sortable: false, orderable: false,
            "render": function (data, type, row, meta) {
                return (row.product.price * row.cnt).toLocaleString();
            }
        }];
}

$(function () {
    init();
});