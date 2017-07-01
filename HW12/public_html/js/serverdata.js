
function readDataFromServer() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === XMLHttpRequest.DONE && this.status == 200) {
            document.getElementById('hit_count').innerHTML = JSON.parse(this.responseText).hit_count;
            document.getElementById('miss_count').innerHTML = JSON.parse(this.responseText).miss_count;
            document.getElementById('element_count').innerHTML = JSON.parse(this.responseText).element_count;
            document.getElementById('ServerTime').innerHTML = JSON.parse(this.responseText).time;
        }
    };

    xhttp.open("GET", "/cache_info", true);
    xhttp.send();
}

function refresh() {
    readDataFromServer();
}