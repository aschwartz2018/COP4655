var openWeatherLink='http://api.openweathermap.org/data/2.5/weather?appid='
+'eccc1b4ec1c7dcffee6558bbbf3bf87c&units=imperial&';

var doweather = function() {
	let workspace = document.getElementById("content");
	workspace.innerHTML="<h1 id='weathersection'>Weather Section</h1>";

	workspace.innerHTML += '<form id="locationInfo" style="display: block; text-align: center;">'
	+ '<input type="text" id="location" placeholder="Enter City, State or ZIP">'
	+ '<button type="button" onclick="cityButton()">City</button>'
	+ '<button type="button" onclick="zipButton()">ZIP</button>'
	+ '<button type="button" id="gpsBtn" onclick="gpsButton()">'
	+ '<i class="fa fa-location-arrow" aria-hidden="true"></i></button>'
	+ '</form>';

	$(window).keydown(function(event){
        if(event.keyCode == 13) {
          event.preventDefault();
          return false;
        }
    });
}

var cityButton = function() {
	let workspace = document.getElementById("content");
	let location = document.getElementById("location").value;
	if(location.length == 0 || !isNaN(location)) {
		let noText = document.getElementById('weathersection');
		noText.innerHTML = "Input a city";
	} else {
		weatherAPI(location,0,0);
	}
}

var zipButton = function() {
	let workspace = document.getElementById("content");
	let location = document.getElementById("location").value;
	if(location.length == 0 || isNaN(location)) {
		let noText = document.getElementById('weathersection');
		noText.innerHTML = "Input a zip code";
	} else {
		weatherAPI(location,0,0);
	}
}

var gpsButton = function() {
	let workspace = document.getElementById("content");
	if (navigator.geolocation){
		navigator.geolocation.getCurrentPosition(function(position){ 
			weatherAPI(position.coords.latitude,position.coords.longitude,1);
		}, function(error) {}, {
			enableHighAccuracy: true
		});
	} else {
		workspace.innerHTML="<h1>Can't get GPS</h1>";
	}
}

var weatherAPI = function(location,location2,gpsToggle) {
	let workspace=document.getElementById("content");
	let keyword;
	if(gpsToggle==1) {
		keyword='lat=';
		location=location+'&lon='+location2;
	} else if(isNaN(location)) {
		keyword='q=';
	} else {
		keyword='zip=';
	}
	$.getJSON(openWeatherLink+keyword+location, function(json) {
		workspace.innerHTML="<h2>Weather data for "+json.name+"</h2>";
		workspace.innerHTML += '<table class="mytable">'
		+ '<tr><td class="mytd">Temperature</td> <td class="mytd" id=\'weather1\'></td>'
		+ '</tr><tr> <td class="mytd">Feels like</td> <td class="mytd" id=\'weather2\'></td>'
		+ '</tr> <tr> <td class="mytd">Min Temperature</td> <td class="mytd" id=\'weather3\'>'
		+ '</td> </tr> <tr> <td class="mytd">Max Temperature</td> <td class="mytd" id=\'weather4\'></td>'
		+ '</tr> <tr> <td class="mytd">Cloudiness</td> <td class="mytd" id=\'weather5\'></td></tr><tr>'
		+ '<td class="mytd">Pressure</td><td class="mytd" id=\'weather6\'></td></tr><tr>'
		+ '<td class="mytd">Humidity</td>'
		+ '<td class="mytd" id=\'weather7\'></td> </tr> <tr> <td class="mytd">Wind Speed</td>'
		+ '<td class="mytd"id=\'weather8\'></td>'
		+ '</tr><tr><td class="mytd">Wind Direction</td><td class="mytd" id=\'weather9\'>'
		+ '</td></tr><tr><td class="mytd">Geolocation</td>'
		+ '<td class="mytd" id=\'weather10\'></td></tr></table>';
		$('#weather1').html(json.main.temp + " degF");
		$('#weather2').html(json.main.feels_like + " degF");
		$('#weather3').html(json.main.temp_min + " degF");
		$('#weather4').html(json.main.temp_max + " degF");
		$('#weather5').html(json.weather[0].main);
		$('#weather6').html(json.main.pressure + ' hpa');
		$('#weather7').html(json.main.humidity + '%');
		$('#weather8').html(json.wind.speed + ' mph');
		$('#weather9').html(json.wind.deg + ' degrees');
		$('#weather10').html(json.coord.lon + ' lon, ' + json.coord.lat + ' lat');		
	})
	.fail(function() { 
		workspace.innerHTML="<h2>Couldn't find weather information for '"+location+"' inputted</h2>";
	})
}