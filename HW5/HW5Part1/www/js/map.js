var domap = function() { //This function should be used to build the map in the content element
    let workspace = document.getElementById("content");
	if (navigator.geolocation){
		navigator.geolocation.getCurrentPosition(function(position){ 
			//Replace API_KEY in link with your own Google Maps API key
			workspace.innerHTML = '<p style="text-align:center"><iframe width="500" height="500"'
			+' frameborder="0" style="border:0"'
			+' src="https://www.google.com/maps/embed/v1/view?key=API_KEY&center='
			+ position.coords.latitude + ',' + position.coords.longitude + '&zoom=15"></iframe></p>';
		}, function(error) {}, {
			enableHighAccuracy: true
		});
	} else {
		workspace.innerHTML="<h1>Can't get GPS</h1>";
	}
}
