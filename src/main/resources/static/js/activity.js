

/**
 * 套件初始化
 * select2、datepicker、dataTable
 */
function init() {
    $('.select2').select2({theme: 'bootstrap-5'});
}

/**
 * 儲存
 */
function save() {
    $('#activityForm').validate({
        errorClass: "text-danger text-valid",
        rules: {
            'bonusMoney': {
                required: true,
                number: true
            },
            'bonusType': {
                required: true
            },
            'bonusConvert': {
                required: true,
                number: true
            },
            'discountMoney': {
                required: true,
                number: true
            },
            'discountType': {
                required: true
            },
            'discountConvert': {
                required: true,
                number: true
            },
        }, errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let msg = utils.msg.editSuccess;
            let url = "/activity/update";
            let param = $("#activityForm").serializeObject();
            $.ajax({
                type: "post",
                url: utils.getHandlerPath(url),
                data: JSON.stringify(param),
                cache: false,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
            }).done(function (response) {
                if (response.code === 200) {
                    utils.msgAlert(msg, "success");
                }
            })
        }
    });
}

$(function () {
    init();
});