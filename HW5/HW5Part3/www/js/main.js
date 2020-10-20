document.addEventListener("DOMContentLoaded", function(){

    document.getElementById('home').onclick = dohome;
    document.getElementById('weather').onclick = doweather;
    document.getElementById('map').onclick = domap;
    document.getElementById('currency').onclick = docurrency;

    if(window.sessionStorage.getItem("token") === null) {
      document.getElementById('weather').disabled=true;
      document.getElementById('map').disabled=true;
      document.getElementById('currency').disabled=true;
    }
    
    dohome();
});

function toggleSignIn() {

  if (!firebase.auth().currentUser) {

    var provider = new firebase.auth.OAuthProvider('yahoo.com');

    firebase.auth().signInWithPopup(provider)
      .then(function (result) { 
        var token = result.credential.accessToken;
        window.sessionStorage.setItem("token",token);
        var user = result.user;
        initApp();
        dohome();
      })
      .catch(function (error) {
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
    window.sessionStorage.removeItem("token");
    location.reload();
  }

}


function initApp() {
  
  firebase.auth().onAuthStateChanged(function (user) {
    
    if (user) {
      var displayName = user.displayName;
      var email = user.email;
      var emailVerified = user.emailVerified;
      var photoURL = user.photoURL;
      var isAnonymous = user.isAnonymous;
      var uid = user.uid;
      var providerData = user.providerData;
      document.getElementById('weather').disabled=false;
      document.getElementById('map').disabled=false;
      document.getElementById('currency').disabled=false;

    } else {
        document.getElementById('weather').disabled=true;
        document.getElementById('map').disabled=true;
        document.getElementById('currency').disabled=true;
    }

  });

}