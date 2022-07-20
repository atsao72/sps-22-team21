let token = 0
function handleCredentialResponse(response) {
    console.log("Encoded JWT ID token: " + response.credential);
    token = response.credential
}
window.onload = function () {
google.accounts.id.initialize({
    client_id: "126364799474-iuabk1c066p90m76da18q2fc2jjkikij.apps.googleusercontent.com",
    callback: handleCredentialResponse
});
google.accounts.id.renderButton(
    document.getElementById("buttonDiv"),
    { theme: "outline", size: "large" }  // customization attributes
);
google.accounts.id.prompt(); // also display the One Tap dialog
}

async function post() {
    if (token == 0) {
        console.log("User need to log in first.")
    } else {
        
        let distance = document.getElementById('distance').value
        let time = document.getElementById('time').value
        let avgBPM = document.getElementById('avgBPM').value
        let description = document.getElementById('description').value
        res = await fetch('/record?' + new URLSearchParams({
            token: token,
            distance: distance,
            time: time,
            avgBPM: avgBPM,
            description: description,
        }), {method : "POST"})
        // await fetch('/chat?' + new URLSearchParams({
        //     text: msg,
        // }), {method : "POST"})
    }
}