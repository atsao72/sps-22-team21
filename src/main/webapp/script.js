// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function addText(msg){
    let e = document.createElement('p');
    e.innerText = msg;
    document.getElementById("feed-container").appendChild(e);
}

// delete every text on chat wall and rewrite
async function loadPosts(){
    let response = await fetch('/record')
    const textResponse = await response.json();
    for(let i = textResponse.length - 1; i >= 0; i--) {
        let username = textResponse[i]["username"]
        let distance = textResponse[i]["distance"]
        let time = textResponse[i]["time"]
        let description = textResponse[i]["description"]
        let formattedText = `${username} ran ${distance} in ${time} with message: ${description}`
        addText(formattedText);
    }
    
}
async function submitPost(msg) {
    console.log(msg);
    await fetch('/chat?' + new URLSearchParams({
        text: msg,
    }), {method : "POST"})
    //add recent text to the wall
    addText(msg);
    
}
loadPosts()