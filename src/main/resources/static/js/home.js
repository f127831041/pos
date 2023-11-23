let rgbArray = [
    'rgba(207,0,3)',
    'rgba(45,186,30)',
    'rgba(160,0,102)',
    'rgba(204,78,1)',
    'rgba(0,128,157)',
    'rgba(1,13,133)',
    'rgba(0, 156, 255)',
    'rgba(107,115,213)',
    'rgba(255,255,129)',
    'rgba(230,113,186)',
    'rgba(31,30,28)',
    'rgba(198,184,3)',
    'rgba(81,1,128)',
    'rgba(0,128,157)',
    'rgba(253,223,213)'
]

function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

function getRandomBgColor(){
    return shuffleArray(rgbArray).shift();
}

function getCompanyData() {
    let result = [];
    for (let company of companyList) {
        let dataList = companyDataObj[company.id];
        let array = new Array(companyDateList.length);
        array.fill(0);
        if (!utils.isEmpty(dataList)) {
            for (let data of dataList) {
                let index = companyDateList.indexOf(data.date);
                if (index !== -1) {
                    array[index] = data.cnt;
                }
            }
        }
        let obj = {
            label: company.brand,
            data: array,
            backgroundColor: getRandomBgColor(),
        }
        result.push(obj);
    }
    return result;
}

function getRevenueData() {
    let result = [];
    let array = new Array(revenueChartDateList.length);
    array.fill(0);

    for (let data of revenueChartDataList) {
        let index = revenueChartDateList.indexOf(data.date);
        if (index !== -1) {
            array[index] = data.pay;
        }
    }

    let obj = {
        label: '營收',
        data: array,
        backgroundColor: "rgba(0, 156, 255, .3)",
        fill: true
    }
    result.push(obj);
    return result;
}

/**
 * 套件初始化
 * select2、datepicker、dataTable
 */
function init() {
    let ctx1 = $("#company-sales").get(0).getContext("2d");
    let myChart1 = new Chart(ctx1, {
        type: "bar",
        data: {
            labels: companyDateList,
            datasets: getCompanyData()
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1
                    }
                }
            }
        }
    });

    let ctx2 = $("#revenue-sales").get(0).getContext("2d");
    let myChart2 = new Chart(ctx2, {
        type: "line",
        data: {
            labels: revenueChartDateList,
            datasets: getRevenueData()
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize: 1000,
                        callback: function (value, index, values) {
                            if (value % 1000 === 0) {
                                return '$' + value.toLocaleString();
                            } else {
                                return null; // 不显示小数刻度
                            }
                        }
                    }
                }
            }
        }
    });
}

$(function () {
    init();
});