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


// Create a p tag that will be used
function addCard(username, distance, time){
    let div_col = document.createElement('div');
    let div_card = document.createElement('div');

    div_col.setAttribute("class", "col-md");

    div_card.setAttribute("class", "card mx-auto");
    div_card.setAttribute("style", "width: 18rem;");
    
    div_card.innerHTML = "<img class='card-img-top' src='resources/post_picture_3.jpeg' alt='Fitness Image' style='width: 286px; height: 180px;'>";
    div_card.innerHTML += `<div class='card-body'> <h5 class='card-title'>${username}</h5> <p class='card-text-2' id='running'> Lorem ipsum dolor sit amet consectetur adipisicing elit. Provident odio ut perspiciatis aut, soluta obcaecati nesciunt odit vel nam commodi ducimus alias, inventore dolores, ipsum exercitationem minima voluptatem incidunt sit. </p></div>`;
    div_card.innerHTML += "<ul class='list-group list-group-flush'>";
    div_card.innerHTML +=  `<li class='list-group-item'>Distance: ${distance}</li>`;
    div_card.innerHTML +=  `<li class='list-group-item'>Average BPM:</li>`;
    div_card.innerHTML +=  `<li class='list-group-item'>Time: ${time}</li>`;
    div_card.innerHTML += "</ul>";
    div_card.innerHTML += "<div class='card-body'> <a href='#' class='btn btn-primary' onclick='BionicReader('card-text-2')'>Bionic Reading</a> </div>";


    div_col.appendChild(div_card);
    document.getElementById("test-feed-0").appendChild(div_col);
}

// This function gets the data from the database
async function loadPosts(){
    let response = await fetch('/record')
    const textResponse = await response.json();
    for(let i = textResponse.length - 1; i >= 0; i--) {
        let username = textResponse[i]["username"]
        let distance = textResponse[i]["distance"]
        let time = textResponse[i]["time"]
        addCard(username, distance, time);
    }
    
}

// Bionic Reader Function
function BionicReader(className){
    var paragraphList = document.getElementsByClassName(className); 
    
    for (var i = 0; i <paragraphList.length; i++){
        var p=paragraphList[i];
      /*
        if (p._bionic_processed){
            continue;
        }*/
        p._bionic_processed = true;
        var nodes= p.childNodes; 
        for(var j = 0; j < nodes.length; j++){
            var node = nodes[j];
            if (node.nodeType == 3){
                console.log('works');
                var span = document.createElement("span");
                var text = node.nodeValue;
                var html = "";
                var words = text.split(" ");
                for (var k = 0; k < words.length; k++){
                    var word = words[k];
                    var boldStr = "";
                    var normalStr = word;
                    if (word.trim().length > 0){
                        if(word.length < 3){
                            boldStr = word;
                            normalStr = ""; 
                        } 
                        else {
                            var boldLen = Math.floor(word.length * 0.5);
                            if (boldLen >= word.length){
                            boldLen = word.length;
                        }
                            boldStr = word.substring(0, boldLen);
                            normalStr = word.substring(boldLen);
                        }   
                    }
                    if (k < words.length - 1) {
                        normalStr += " ";
                    }
                    if (boldStr.length > 0) {
                        var b = document.createElement("b");
                        b.innerText = boldStr;
                        span.appendChild(b);
                    }
                    if (normalStr.length > 0) {
                        var textNode = document.createTextNode(normalStr);
                        span.appendChild(textNode);
                    }
                }
                p.replaceChild(span, node);
                
            }
        }
    }
}

function testFunction(){
    element = document.getElementsByClassName("card-text-1");
    console.log(element);
    console.log(element[0])
}

// loadPosts()