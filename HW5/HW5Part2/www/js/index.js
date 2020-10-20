/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('DOMContentLoaded', onDeviceReady, false);

function onDeviceReady() {
    // Cordova is now initialized. Have fun!

    console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);

    //Disable the input elements
    document.getElementById('weather').disabled = true;
    document.getElementById('city').disabled = true;

    //Add onclick listeners
    document.getElementById('login').addEventListener('click', toggleSignIn, false);
    document.getElementById('weather').onclick = getWeatherInfo;
    initApp();

    //Make sure user is not signed in on page load.
    firebase.auth().signOut();

}

//Get weather from Yahoo
function getWeatherInfo() {
    var token = window.sessionStorage.getItem("token");
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.response);
            document.getElementById("content").innerHTML = "";
            var p = document.createElement('p');
            p.innerText = this.response;
            document.getElementById("content").appendChild(p);
        }
    };
    xhr.open('GET', 'https://weather-ydn-yql.media.yahoo.com/forecastrss?location=' + document.getElementById("city").value + '&format=json');

    //Set header for OAuth
    xhr.setRequestHeader("Authorization", "Bearer " + token);
    xhr.send();
}



function toggleSignIn() {
  //If the current user object does not exist
  if (!firebase.auth().currentUser) {
    //Set the auth provider to yahoo
    var provider = new firebase.auth.OAuthProvider('yahoo.com');
    //And sign in with a popup
    firebase.auth().signInWithPopup(provider)
      .then(function (result) { //On Success save the token to session storage and output it to console
        var token = result.credential.accessToken;
        window.sessionStorage.setItem("token",token);
        console.log(result);
        var user = result.user;
        var ws = document.getElementById("content");
        ws.innerHTML = '';
        var p = document.createElement('p');
        p.textContent = "User : " + result.user.email;
        ws.appendChild(p);
        p = document.createElement('p');
        p.textContent = "Token : " + token;
        ws.appendChild(p);

      })
      .catch(function (error) { //On failure alert user or report error to console
        var errorCode = error.code;
        var errorMessage = error.message;
        var email = error.email;
        var credential = error.credential;
        if (errorCode === 'auth/account-exists-with-different-credential') {
          alert('You have already signed up with a different auth provider for that email.');
        } else {
          console.error(error);
        }
      });
  } else {
    firebase.auth().signOut();
  }

}


function initApp() {
  //Set listeners for Auth State Changed
  firebase.auth().onAuthStateChanged(function (user) {
    //if there is a user enable app functionality
    if (user) {
      var displayName = user.displayName;
      var email = user.email;
      var emailVerified = user.emailVerified;
      var photoURL = user.photoURL;
      var isAnonymous = user.isAnonymous;
      var uid = user.uid;
      var providerData = user.providerData;
      document.getElementById('login').textContent = 'Sign out';
      document.getElementById('city').disabled = false;
      document.getElementById('weather').disabled = false;

      //else keep the app disabled or re-disabled it
    } else {
      document.getElementById("weather").disabled = true;
      document.getElementById('login').textContent = 'Sign in with Yahoo';
    }

  });

}

