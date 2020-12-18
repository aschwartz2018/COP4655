$(document).ready(function() {
    var strJSON = localStorage.getItem("myJSON");
    var json = JSON.parse(strJSON);

    $('#cityName').html(json.name + ' weather information');

    $('#weather1').html(json.main.temp + " degF");
    $('#weather2').html(json.main.feels_like + " degF");
    $('#weather3').html(json.main.temp_min + " degF");
    $('#weather4').html(json.main.temp_max + " degF");
    $('#weather5').html(json.weather[0].main);
    $('#weather6').html(json.main.pressure + ' hpa');
    $('#weather7').html(json.main.humidity + '%');
    $('#weather8').html(json.wind.speed + ' mph');
    $('#weather9').html(json.wind.deg + ' degrees');
    $('#weather10').html(json.coord.lon + ' lon, ' + json.coord.lat + ' lat')

    //Insert your Google Maps API key (replace API_KEY in the link)
    $('#map').html('<p style="text-align:center"><iframe width="300" height="225" frameborder="0" style="border:0" src="https://www.google.com/maps/embed/v1/view?key=API_KEY&center=' + json.coord.lat + ',' + json.coord.lon + '&zoom=15"></iframe></p>')
})
