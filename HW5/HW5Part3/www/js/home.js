var dohome = function(){

    let workspace = document.getElementById("content");
    workspace.innerHTML = "<img src='./header.png' alt='WeatherIcon' class='responsive'>";
    let h2 = document.createElement('h2');
    let text = document.createTextNode("Welcome to my HW5 APP");
    h2.appendChild(text);
    workspace.append(h2);
    workspace.innerHTML += "<p>This is an application where you can access a map of your current location"
    +", get weather data from anywhere, and see currency info (compared to USD).</p>";

    if(window.sessionStorage.getItem("token") === null) {
        workspace.innerHTML += '<button id="login" style="text-align: center;">Sign in with Yahoo</button>';
    }
    else {
        workspace.innerHTML += '<button id="logout" style="text-align: center;">Sign out of Yahoo</button>';
    }

    let h4 = document.createElement('h4');
    text = document.createTextNode("Alex Schwartz : Z23482130");
    h4.appendChild(text);
    workspace.append(h4);

    if(document.getElementById('login'))
        document.getElementById('login').addEventListener('click', toggleSignIn, false);
    if(document.getElementById('logout'))
        document.getElementById('logout').addEventListener('click', toggleSignIn, false);
}