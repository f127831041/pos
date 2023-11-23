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
    let url = "/inventory/delete/" + id
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
    let url = utils.getHandlerPath("/inventory/query");
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

function changeCompany(type) {
    let form = type === 'query' ? $("#queryForm") : $("#inventoryForm");
    let companyId = $(form).find("select[name='companyId']").val();

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
            if(type === 'query'){
                let prodNoHtml = `<option selected="selected" value="">- 請選擇 -</option>`;
                for (let prodNo of result.prodNoList) {
                    prodNoHtml += `<option value="${prodNo}">${prodNo}</option>`
                }
                $(form).find("select[name='prodNo']").empty().append(prodNoHtml);
            }else {
                let html = `<option selected="selected" value="">- 請選擇 -</option>`;
                for (let product of result.productList) {
                    let design = utils.isEmpty(product.design) ? '' : '-' + product.design;
                    let color =  utils.isEmpty(product.color) ? '' : '-' + product.color;
                    html += `<option value="${product.id}">${product.prodNo + '-' + product.name + design + color}</option>`
                }
                $(form).find("select[name='productId']").empty().append(html);
            }
        }
    })
}

/**
 * 新增/編輯
 * @param actionType
 * @param id   編輯才有
 */
function dialogOpen(id) {
    let url = "/inventory/get/" + id;
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath(url),
        cache: false,
    }).done(function (response) {
        $('#inventoryModal').find('.modal-content').html(response);
    }).always(function () {
        $('#inventoryModal').modal({backdrop: 'static', keyboard: false})
        $('#inventoryModal').modal('show');
        $('#inventoryModal').find('.select2').select2({
            theme: "bootstrap-5",
            dropdownParent: $('#inventoryModal')
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
        {
            data: "name", title: "品項", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.product.name;
            }
        },
        {
            data: "design", title: "圖案", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.product.design;
            }
        },
        {
            data: "color", title: "顏色", className: "text-center",
            "render": function (data, type, row, meta) {
                return row.product.color;
            }
        },
        {
            data: "sizeId", title: "尺寸", className: "text-center", "render": function (data, type, row, meta) {
                return row.sizeChart.size;
            }
        },
        {
            data: "cnt", title: "數量", className: "text-center", "render": function (data, type, row, meta) {
                let index = meta.row; // 取得行索引
                let up = `<button type='button' class='btn btn-sm btn-secondary' onclick='cntUp(${index})'><i class="fa fa-plus"></i></button>`;
                let down = `<button type='button' class='btn btn-sm btn-secondary' onclick='cntDown(${index})'><i class="fa fa-minus"></i></button>`;
                return down + `&nbsp;<span>${data}</span>&nbsp;` + up;
            }
        },
        {
            data: null, title: "操作", sortable: false, className: "text-center", orderable: false,
            "render": function (data, type, row, meta) {
                let id = data.id;
                return `<button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(${id})'><i class="fa fa-trash"></i>刪除</button>`;
            }
        }];
}

/**
 * 儲存
 */
function save() {
    $('#inventoryForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'companyId': {
                required: true
            },
            'productId': {
                required: true
            },
            'sizeId': {
                required: true
            },
            'cnt': {
                required: true,
                number: true
            },
        }, errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let msg = utils.msg.addSuccess;
            let url = "/inventory/add";
            let param = $("#inventoryForm").serializeObject();
            $.ajax({
                type: "post",
                url: utils.getHandlerPath(url),
                data: JSON.stringify(param),
                cache: false,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
            }).done(function (response) {
                if (response.code === 200) {
                    $("#dataTable").DataTable().ajax.reload(null, true);
                    $('#inventoryModal').modal('hide');
                    utils.msgAlert(msg, "success");
                }
            })
        }
    });
}

function saveCnt() {
    let url = "/inventory/updateCnt";
    let tableData = $('#dataTable').DataTable().rows().data();
    $.ajax({
        type: "post",
        url: utils.getHandlerPath(url),
        data: JSON.stringify(tableData.toArray()),
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    }).done(function (response) {
        if (response.code === 200) {
            utils.msgAlert(utils.msg.editSuccess, "success");
        }
    })
}

function cntUp(index) {
    // 取得要更新數量的行
    let rowData = $('#dataTable').DataTable().row(index).data();
    // 更新數量
    rowData.cnt += 1;
    $('#dataTable').DataTable().row(index).data(rowData);
}

function cntDown(index) {
    // 取得要更新數量的行
    let rowData = $('#dataTable').DataTable().row(index).data();
    if (rowData.cnt > 0) {
        // 更新數量
        rowData.cnt -= 1;
    }
    $('#dataTable').DataTable().row(index).data(rowData);
}

$(function () {
    init();
});