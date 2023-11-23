/**
 * 套件初始化
 * select2、datepicker、dataTable
 */
function init() {
    $('.select2').select2({theme: 'bootstrap-5'});
}

function cleanMember() {
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath("/member/phone/clean"),
        cache: false,
    }).done(function (response) {
        if (response.code === 200) {
            $("#phone").val("");
            $("#cname").val("");
            $("#points").val("");
            $("#birthday").val("");
            $("#memberId").val("");
        }
    })
}

function queryMember() {
    if (utils.isEmpty($("#phone").val())) {
        $("#phone").focus();
        return;
    }
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath("/member/phone/" + $("#phone").val()),
        cache: false,
    }).done(function (response) {
        if (response.code === 200) {
            let member = response.data;
            if (utils.isEmpty(member)) {
                utils.msgAlert("查無會員紀錄！", "warning");
            } else {
                let month = utils.isEmpty(member.month) ? '' : member.month;
                let day = utils.isEmpty(member.day) ? '' : '/' + member.day;
                $("#cname").val(member.cname);
                $("#points").val(member.points);
                $("#birthday").val(month + day);
                $("#memberId").val(member.id);
            }
        }
    })
}

function changeSize(obj) {
    let selectOption = $(obj).find('option:selected');
    let cnt = $(selectOption).attr("data-cnt");
    let tr = $(obj).parent().parent();
    let td = $(tr).find('td').eq(8);
    $(td).find("span").text("1");
    $(td).find("input").val(cnt)

    //計算單向商品總價
    let price = $(tr).find('td').eq(7);
    let colSum = 1 * parseInt(price.text());
    $(tr).find('td').eq(9).text(colSum);
    calculateSum();
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
                let color = utils.isEmpty(product.color) ? '' : '-' + product.color;
                html += `<option value="${product.id}">${product.prodNo + '-' + product.name + design + color}</option>`
            }
            $("#productId").empty().append(html);
        }
    })
}

function addProd() {
    if (utils.isEmpty($("#productId").val())) {
        utils.msgAlert("尚未選擇商品型號！", "warning");
        return;
    }
    let id = $("#productId").val();

    let url = "/sale/get/product/" + id;
    $.ajax({
        type: "GET",
        url: utils.getHandlerPath(url),
        cache: false,
    }).done(function (response) {
        let data = response.data;
        if (data.length === 0) {
            utils.msgAlert("此產品上尚無庫存！", "warning");
            return;
        }
        let sizeOption = "";
        for (let inventory of data) {
            sizeOption += `<option value="${inventory.sizeChart.id}" data-cnt="${inventory.cnt}">${inventory.sizeChart.size}</option>`;
        }

        let product = data[0].product;
        let company = data[0].company;
        let html = `<tr>
                        <td class="d-none">${data[0].id}</td>
                        <td>${company.brand}</td>
                        <td>${product.prodNo}</td>
                        <td>${utils.isEmpty(product.name) ? '' : product.name}</td>
                        <td>${utils.isEmpty(product.design) ? '' : product.design}</td>
                        <td>${utils.isEmpty(product.color) ? '' : product.color}</td>
                        <td>
                            <select class="form-select select2" name="sizeId" onchange="changeSize(this)">
                              ${sizeOption}
                             </select>
                         </td>
                        <td>${product.price}</td>
                        <td>
                            <button type="button" class="btn btn-sm btn-secondary" onclick="cntDown(this)"><i class="fa fa-minus"></i></button>
                            <span>1</span>
                            <button type="button" class="btn btn-sm btn-secondary" onclick="cntUp(this)"><i class="fa fa-plus"></i></button>
                            <input type="hidden" value="${data[0].cnt}">
                        </td>
                         <td>${product.price}</td>
                        <td><button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(this)'><i class="fa fa-trash"></i>刪除</button></td>
                    </tr>`
        $("#sale").append(html);
        calculateSum();
    })

}

function cntDown(obj) {
    let td = $(obj).parent();
    let tr = $(obj).parent().parent();
    // 取得要更新數量的行
    let val = parseInt($(td).find("span").text());
    if (val > 0) {
        // 更新數量
        val -= 1;
    }
    $(td).find("span").text(val);
    //計算單向商品總價
    let price = $(tr).find('td').eq(7);
    let colSum = val * parseInt(price.text());
    $(tr).find('td').eq(9).text(colSum);
    calculateSum();
}

function cntUp(obj) {
    let td = $(obj).parent();
    let tr = $(obj).parent().parent();
    // 取得要更新數量的行
    let val = parseInt($(td).find("span").text());
    let max = $(td).find("input").val();
    // 更新數量
    if ((val + 1) > max) {
        return;
    } else {
        val += 1;
    }
    $(td).find("span").text(val);

    //計算單向商品總價
    let price = $(tr).find('td').eq(7);
    let colSum = val * parseInt(price.text());
    $(tr).find('td').eq(9).text(colSum);
    calculateSum();
}

function calculateSum() {
    let rows = $("#table").find("tr");
    let sum = 0;
    for (let i = 0; i < rows.length; i++) {
        let tr = rows[i];
        //有這個class代表示標頭和結尾
        if ($(tr).hasClass("text-dark")) {
            continue;
        }
        let val = $(tr).find('td').eq(9).text();
        sum += parseInt(val);
    }
    $("#pay").text(sum.toLocaleString());
    $("#total").text(sum.toLocaleString());
    calculateDiscount(sum);
    blurMoney();
}

function blurMoney() {
    let val = utils.isEmpty($("#bonus").val()) ? '0' : $("#bonus").val();
    let rows = $("#table").find("tr");
    let sum = 0;
    for (let i = 0; i < rows.length; i++) {
        let tr = rows[i];
        //有這個class代表示標頭和結尾
        if ($(tr).hasClass("text-dark")) {
            continue;
        }
        let val = $(tr).find('td').eq(9).text();
        sum += parseInt(val);
    }
    let promotion = utils.isEmpty($("#promotion").val()) ? '0' : $("#promotion").val();
    //折扣情況 重新計算 總金額
    if ($("#discountType").val() === '1') {
        let discount = utils.isEmpty($("#discount").text()) ? '0' : $("#discount").text();
        sum = sum - (parseInt(discount) + parseInt(val) + parseInt(promotion));
    } else {
        sum = sum - (parseInt(val) + parseInt(promotion));
    }
    $("#pay").text(sum.toLocaleString());
}

function calculateDiscount(sum) {
    let type = $("#discountType").val();
    let money = $("#discountMoney").val();
    let convert = $("#discountConvert").val();
    if (type === '1') {
        if (parseInt(sum) >= parseInt(money)) {
            let result = Math.floor(parseInt(sum) * parseFloat('0.' + convert));
            let difference = parseInt(sum) - parseInt(result);
            $("#discount").text(difference.toLocaleString());
            $("#pay").text(result.toLocaleString());
        } else {
            $("#discount").text(0);
        }
    }
}

function calculateMoney() {
    let sum = $("#pay").text();
    let money = $("#money").val();
    if (utils.isEmpty(sum)) {
        sum = '0';
    }
    if (utils.isEmpty(money)) {
        money = '0';
    }
    let give = parseInt(money) - parseInt(sum.replace(/,/g, ''));
    $("#give").text(give.toLocaleString());
}

function billPay() {
    let rows = $("#table").find("tr");
    let params = [];
    for (let i = 0; i < rows.length; i++) {
        let tr = rows[i];
        //有這個class代表示標頭和結尾
        if ($(tr).hasClass("text-dark")) {
            continue;
        }
        let inventoryId = $(tr).find('td').eq(0).text();
        let cnt = $(tr).find('td').eq(8).find("span").text();
        let total = $("#total").text().replace(/,/g, '');
        let discount = $("#discount").text().replace(/,/g, '');
        let bonus = $("#bonus").val();
        let pay = $("#pay").text().replace(/,/g, '');
        let promotion = $("#promotion").val();
        let payMethod = $('input[name="payMethod"]:checked').val();
        let param = {
            inventoryId: inventoryId,
            cnt: cnt,
            total: utils.isEmpty(total) ? 0 : total,
            discount: utils.isEmpty(discount) ? 0 : discount,
            bonus: utils.isEmpty(bonus) ? 0 : bonus,
            pay: utils.isEmpty(pay) ? 0 : pay,
            promotion: utils.isEmpty(promotion) ? 0 : promotion,
            payMethod: payMethod,
        }
        params.push(param);
    }
    let url = "/sale/billPay"
    $.ajax({
        type: "post",
        url: utils.getHandlerPath(url),
        data: JSON.stringify(params),
        cache: false,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    }).done(function (response) {
        if (response.code === 200) {
            utils.msgAlert(utils.msg.addSuccess, "success", function () {
                window.location.reload();
            });
        }
    })
}


function deleteRow(obj) {
    let tr = $(obj).parent().parent();
    $(tr).remove();
    calculateSum();
}

$(function () {
    init();
});