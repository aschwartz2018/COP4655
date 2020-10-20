var docurrency = function(){

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let currency = JSON.parse(this.responseText);
            buildcurrency(currency);
        }
    };
     url = 'https://api.exchangeratesapi.io/latest?base=USD'
        
    xmlhttp.open("GET", url, true);
    xmlhttp.send();
}


function buildcurrency(currency){
    let workspace = document.getElementById("content");
    workspace.innerHTML = "<h2>Currency Information</h2>";
    //TODO : CURRENCY DATA IS IN JSON OBJECT currency
    //All you need to do is build it into content.
    let rates = currency.rates;
    for(var property in rates) {
        workspace.innerHTML += "<p>" + property + ": " + rates[property] +"</p>";
    }
}

