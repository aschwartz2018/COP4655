$(document).ready(function() {
    localStorage.setItem('myJSON',null);
    localStorage.setItem('latitude',null);
    localStorage.setItem('longitude',null);

    $(window).keydown(function(event){
        if(event.keyCode == 13) {
          event.preventDefault();
          return false;
        }
    });

    $('#cityBtn').on('click',function(event) {
        event.preventDefault();
        var city = $('#location').val();

        if(city=='') {
            $('#notFound').html('No city indicated');
        } else {
            $.getJSON('http://api.openweathermap.org/data/2.5/weather?appid=eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&q='+city, function(json) {
                location.href='./second.html';
                strJSON = JSON.stringify(json);
                localStorage.setItem("myJSON",strJSON);
            })
            .fail(function() { 
                $('#notFound').html('City not found'); 
            })
        }
    })

    $('#zipBtn').on('click',function(event) {
        event.preventDefault();
        var city = $('#location').val();

        if(city=='') {
            $('#notFound').html('No ZIP indicated');
        } else {
            $.getJSON('http://api.openweathermap.org/data/2.5/weather?appid=eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&zip='+city, function(json) {
                location.href='./second.html';
                strJSON = JSON.stringify(json);
                localStorage.setItem("myJSON",strJSON);
            })
            .fail(function() { 
                $('#notFound').html('ZIP not found'); 
            })
        }
    })

    $('#gpsBtn').on('click',function(event) {
        if (navigator.geolocation){
            navigator.geolocation.getCurrentPosition(function(position){ 
                localStorage.setItem('latitude',position.coords.latitude);
                localStorage.setItem('longitude',position.coords.longitude);
            }, function(error) {}, {
                enableHighAccuracy: true
            });
        }
        
        $.getJSON('http://api.openweathermap.org/data/2.5/weather?appid=eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&lat='+localStorage.getItem('latitude')+'&lon='+localStorage.getItem('longitude'), function(json) {
            location.href='./second.html';
            strJSON = JSON.stringify(json);
            localStorage.setItem("myJSON",strJSON);
        })
        .fail(function() { 
            $('#notFound').html('GPS not found'); 
        })
    })
})