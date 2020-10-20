document.addEventListener("DOMContentLoaded", function(){
    //Set event listeners/handlers for buttons
    document.getElementById('home').onclick = dohome;
    document.getElementById('weather').onclick = doweather;
    document.getElementById('map').onclick = domap;
    document.getElementById('currency').onclick = docurrency;
    
    dohome();
});
