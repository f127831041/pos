const msgEnum = Object.freeze(
    {
        addSuccess: '新增成功',
        editSuccess: '修改成功',
        deleteSuccess: '删除成功',
        uploadSuccess: '上傳成功',
        downloadSuccess: '下載成功',
        reviewSuccess: '審核成功',
        addFail: '新增失败',
        editFail: '修改失败',
        deleteFail: '删除失败',
        logOutFail: '登出失敗',
        loginFail: '登入失敗',
        authFail: '無權進行此項操作',
        reviewFail: '審核失敗',
        systemFail: '執行此操作時發生錯誤！請測試相關環境或洽詢服務廠商。',
        reloadFail: '系統發生錯誤，請重新登入！',
        fieldEmptyFail: '尚未添加欄位！',
        fileSizeFail: '超出檔案最大限制！',
        confirmDelete: '確定要刪除資料嗎？',
        fileNameFail: '系統檢測到重覆的檔名，是否確定要覆蓋？',
        confirmDeleteMenu: '删除會連同子選單一起刪除，確定要刪除資料嗎？',
        userInfoFail: '無法取得登入者資訊，請重新登入！',
    }
)


jQuery.ajaxSetup({
    error: function (jqXHR, textStatus, errorThrown) {
        if (jqXHR.status === 403) {
            utils.msgAlert(msgEnum.userInfoFail, "error", function () {
                window.location.href = utils.getHandlerPath('/login');
            })
        } else {
            let msg = utils.msg.systemFail;
            if (!utils.isEmpty(jqXHR.responseText)) {
                let json = JSON.parse(jqXHR.responseText);
                msg = json.message;
            }
            utils.msgAlert(msg, "error");
        }
    },
    //完成請求後觸發。即在success或error觸發後觸發
    complete: function (XMLHttpRequest, status) {
        $.unblockUI();
    },
    beforeSend: function (XMLHttpRequest) {
        //傳送請求前觸發
        $.blockUI({
            message: `<div class="d-flex justify-content-center">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div> 
                      </div>`,
            //borderWidth:'0px' 和透明背景
            css: {borderWidth: '0px', backgroundColor: 'transparent'},
            baseZ: 2000
        });
    }
});

jQuery.fn.serializeObject = function () {
    let formData = {};
    let formArray = this.serializeArray();
    for (let i = 0, n = formArray.length; i < n; ++i) {
        formData[formArray[i].name] = formArray[i].value;
    }
    return formData;
};

$(document).on('hidden.bs.modal', '.modal', function () {
    $('.modal:visible').length && $(document.body).addClass('modal-open');
});

const utils = (function () {
    return {
        dataTableOption: () => {
            let option = {};
            $.extend(option, {
                colReorder: false,
                destroy: true,
                autoWidth: false,
                serverSide: true,
                lengthMenu: [10, 25, 50, 75, 100],
                pageLength: 10,
                searching: false,
                language: {
                    emptyTable: "查無資料",
                    processing: "處理中...",
                    loadingRecords: "載入中...",
                    lengthMenu: "顯示 _MENU_ 項結果",
                    zeroRecords: "沒有符合的結果",
                    // info: "顯示第 _START_ 至 _END_ 項結果，共 _TOTAL_ 項",
                    // infoEmpty: "顯示第 0 至 0 項結果，共 0 項",
                    // infoFiltered: "(從 _MAX_ 項結果中過濾)",
                    info: "共 _TOTAL_ 項",
                    infoEmpty: "共 0 項",
                    infoFiltered: "",
                    infoPostFix: "",
                    search: "搜尋:",
                    paginate: {"first": "首頁", "previous": "上一頁", "next": "下一頁", "last": "尾頁"},
                    aria: {"sortAscending": ": 升冪排列", "sortDescending": ": 降冪排列"}
                }
            });
            return option;
        },
        msgConfirm: (content, func1, func2) => {
            Swal.fire({
                text: content,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3c8dbc',
                cancelButtonColor: '#d33',
                confirmButtonText: '是',
                cancelButtonText: '否'
            }).then((result) => {
                if (result.isConfirmed) {
                    func1();
                } else {
                    func2();
                }
            })
        },
        msgRemind: (content, type) => {
            Swal.fire({
                icon: type,
                text: content,
                showConfirmButton: false,
                showCancelButton: false,
                timer: 1500
            })
        },
        msgAlert: (content, type, func) => {
            Swal.fire({
                icon: type,
                text: content,
                confirmButtonText: '確定',
                confirmButtonColor: '#3c8dbc',
            }).then(() => {
                if (typeof func === 'function') {
                    func();
                }
            })
        },
        copyText: (content) => {
            if (window.location.protocol.indexOf("https")) {
                navigator.clipboard.writeText(content).then(() => {
                    utils.msgRemind('複製鏈結成功','success');
                });
            } else {
                let aux = document.createElement("textarea");
                aux.setAttribute("text", content);
                aux.innerHTML = content;
                document.body.appendChild(aux);
                aux.select();
                document.execCommand("copy");
                document.body.removeChild(aux);
                utils.msgRemind('複製鏈結成功', 'success');
            }
        },
        getHandlerPath: (functionPath) => {
            return '/' + window.location.pathname.split("/")[1] + functionPath;
        },
        isEmpty(str) {
            if (!str || 0 === str.length) {
                return true;
            } else if (typeof str === "undefined") {
                return true;
            } else if (!str || /^\s*$/.test(str)) {
                return true;
            }
            return false;
        },
        getNameSpace: () => {
            return window.location.pathname.split("/")[1];
        },
        msg: msgEnum
    };
})();



