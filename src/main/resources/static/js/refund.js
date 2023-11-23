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
                $("#cname").val(member.cname);
                $("#points").val(member.points);
                $("#birthday").val(member.month + "/" + member.day);
                $("#memberId").val(member.id);
            }
        }
    })
}

function changeSize(obj) {
    let selectOption = $(obj).find('option:selected');
    let tr = $(obj).parent().parent();
    let td = $(tr).find('td').eq(8);
    $(td).find("span").text("1");

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
    let product = productObj[id];
    let company = companyObj[product.companyId];
    let sizeOption = "";
    for (let sizeChart of sizeChartList) {
        sizeOption += `<option value="${sizeChart.id}">${sizeChart.size}</option>`;
    }

    let html = `<tr>
                        <td class="d-none">${product.id}</td>
                        <td>${company.brand}</td>
                        <td>${product.prodNo}</td>
                        <td>${product.name}</td>
                        <td>${product.design}</td>
                        <td>${product.color}</td>
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
                        </td>
                         <td>${product.price}</td>
                        <td><button type='button' class='btn btn-sm btn-danger' onclick='deleteRow(this)'><i class="fa fa-trash"></i>刪除</button></td>
                    </tr>`
    $("#data").append(html);
    calculateSum();

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
    val += 1;
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
}

function save() {
    let rows = $("#table").find("tr");
    let params = [];
    for (let i = 0; i < rows.length; i++) {
        let tr = rows[i];
        //有這個class代表示標頭和結尾
        if ($(tr).hasClass("text-dark")) {
            continue;
        }
        let productId = $(tr).find('td').eq(0).text();
        let sizeId = $(tr).find('td').eq(6).find('select').val();
        let cnt = $(tr).find('td').eq(8).find("span").text();
        let total = $("#total").text().replace(/,/g, '');
        let pay = $("#pay").text().replace(/,/g, '');
        let bonus = $("#bonus").val();
        let payMethod = $('input[name="payMethod"]:checked').val();
        let param = {
            productId: productId,
            sizeId: sizeId,
            cnt: cnt,
            total: utils.isEmpty(total) ? 0 : total,
            pay: utils.isEmpty(pay) ? 0 : pay,
            bonus: utils.isEmpty(bonus) ? 0 : bonus,
            payMethod: payMethod
        }
        params.push(param);
    }
    let url = "/refund/save"
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
    sum -= parseInt(val);
    $("#pay").text(sum.toLocaleString());
}

function deleteRow(obj) {
    let tr = $(obj).parent().parent();
    $(tr).remove();
    calculateSum();
}

$(function () {
    init();
});