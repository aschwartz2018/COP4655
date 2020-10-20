var dohome = function(){
    
    let workspace = document.getElementById("content");
    workspace.innerHTML = "<img src='./header.png' alt='WeatherIcon' class='responsive'>";
    let h2 = document.createElement('h2');
    let text = document.createTextNode("Welcome to my HW5 APP");
    h2.appendChild(text);
    workspace.append(h2);
    workspace.innerHTML += "<p>This is an application where you can access a map of your current location"
    +", get weather data from anywhere, and see currency info (compared to USD).</p>";
    let h4 = document.createElement('h4');
    text = document.createTextNode("Alex Schwartz : Z23482130");
    h4.appendChild(text);
    workspace.append(h4);
    
}