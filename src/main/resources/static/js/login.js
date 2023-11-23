
/**
 * 表單送出(登入)
 * @returns {boolean}
 */
function login() {
    $('#form').validate({
        errorClass: "text-danger text-valid",
        errorElement: "p",
        rules: {
            'account': {
                required: true
            }, 'password': {
                required: true
            }
        },
        errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        }, highlight: function (element, errorClass) {
            $(element).removeClass(errorClass);
        }, submitHandler: function () {
            let params = $("#form").serializeObject();
            $.ajax({
                type: 'POST',
                url: utils.getHandlerPath("/doLogin"),
                data: JSON.stringify(params),
                cache: false,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
            }).done(function (response) {
                if (response.code === 200) {
                    window.location.href = utils.getHandlerPath("/home");
                }
            })
        }
    });
}

$(function () {

});