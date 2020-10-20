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
		getWeatherInfo(0,0,0);
	}
}

var zipButton = function() {
	let workspace = document.getElementById("content");
	let location = document.getElementById("location").value;
	if(location.length == 0 || isNaN(location)) {
		let noText = document.getElementById('weathersection');
		noText.innerHTML = "Input a zip code";
	} else {
		getWeatherInfo(0,0,0);
	}
}

var gpsButton = function() {
	let workspace = document.getElementById("content");
	if (navigator.geolocation){
		navigator.geolocation.getCurrentPosition(function(position){ 
			getWeatherInfo(position.coords.latitude,position.coords.longitude,1);
		}, function(error) {}, {
			enableHighAccuracy: true
		});
	} else {
		workspace.innerHTML="<h1>Can't get GPS</h1>";
	}
}

var getWeatherInfo = function(lat,long,gpsToggle){
	var workspace = document.getElementById('content');
    var token = window.sessionStorage.getItem("token");
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
			var json = JSON.parse(this.responseText);
			workspace.innerHTML="<h2>Weather data for "+json.location.city+"</h2>";
			workspace.innerHTML += '<table class="mytable">'
			+ '<tr><td class="mytd">Temperature</td> <td class="mytd" id=\'weather1\'></td></tr>'
			+ '<tr><td class="mytd">Cloudiness</td> <td class="mytd" id=\'weather2\'></td></tr>'
			+ '<tr><td class="mytd">Pressure</td><td class="mytd" id=\'weather3\'></td></tr><tr>'
			+ '<td class="mytd">Humidity</td>'
			+ '<td class="mytd" id=\'weather4\'></td> </tr> <tr> <td class="mytd">Wind Speed</td>'
			+ '<td class="mytd"id=\'weather5\'></td>'
			+ '</tr><tr><td class="mytd">Wind Direction</td><td class="mytd" id=\'weather6\'>'
			+ '</td></tr><tr><td class="mytd">Geolocation</td>'
			+ '<td class="mytd" id=\'weather7\'></td></tr></table>';
			$('#weather1').html(json.current_observation.condition.temperature + " degF");
			$('#weather2').html(json.current_observation.condition.text);
			$('#weather3').html(json.current_observation.atmosphere.pressure + ' inHg');
			$('#weather4').html(json.current_observation.atmosphere.humidity + '%');
			$('#weather5').html(json.current_observation.wind.speed + ' mph');
			$('#weather6').html(json.current_observation.wind.direction + ' degrees');
			$('#weather7').html(json.location.long + ' lon, ' + json.location.lat + ' lat');
        }
	};
	if(gpsToggle==0) {
		xhr.open('GET', 'https://weather-ydn-yql.media.yahoo.com/forecastrss?location='
		+ document.getElementById("location").value + '&format=json');
	} else {
		xhr.open('GET', 'https://weather-ydn-yql.media.yahoo.com/forecastrss?lat='
		+ lat + '&lon='+long+'&format=json');
	}
	
    xhr.setRequestHeader("Authorization", "Bearer " + token);
    xhr.send();
}