
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
    let url = utils.getHandlerPath("/company/query");
    let customObj = {
        order: [0, 'desc'],
        columns: getColumns('')
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
        {data: "brand", title: "品牌名稱", className: "text-center"},
        {data: "salesName1", title: "業務姓名", className: "text-center"},
        {data: "mobilePhone1", title: "行動電話", className: "text-center"},
        {data: "phone1", title: "市話", className: "text-center"},
        {data: "address1", title: "地址", className: "text-center"},
        {
            data: null, title: "操作", sortable: false, className: "text-center", orderable: false,
            "render": function (data, type, row, meta) {
                let id = data.id;
                let edit = `<button type='button' class='btn btn-sm btn-primary' onclick='dialogOpen(${id})'><i class="fa fa-pencil-alt"></i>編輯</button>`;
                let del = `<button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(${id})'><i class="fa fa-trash"></i>刪除</button>`;
                return edit + "&nbsp&nbsp" + del;
            }
        }];
}

/**
 * 儲存
 */
function save() {
    $('#companyForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'brand': {
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