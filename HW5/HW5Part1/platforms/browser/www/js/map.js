var domap = function() { //This function should be used to build the map in the content element
    let workspace = document.getElementById("content");
	if (navigator.geolocation){
		navigator.geolocation.getCurrentPosition(function(position){ 
			workspace.innerHTML = '<p style="text-align:center"><iframe width="500" height="500"'
			+' frameborder="0" style="border:0"'
			+' src="https://www.google.com/maps/embed/v1/view?key=AIzaSyC0FDwy4JSx7rRULalVcSaz7K_9pOGJ8Yc&center='
			+ position.coords.latitude + ',' + position.coords.longitude + '&zoom=15"></iframe></p>';
		}, function(error) {}, {
			enableHighAccuracy: true
		});
	} else {
		workspace.innerHTML="<h1>Can't get GPS</h1>";
	}
}