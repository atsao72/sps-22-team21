document.addEventListener('DOMContentLoaded', function (){
    var checkbox = document.querySelector('input[type="checkbox"]');

    checkbox.addEventListener('change', function (){
        if (checkbox.checked){
            console.log('Checked');
            BionicReader();
        }
        else{
            console.log('Not Checked');
            normalText();
        }

    });
});
/*
  Removes 'b', and 'span' element from 'p'
*/
function normalText(){
    var paragraphList = document.getElementsByTagName("p"); // Made changes here from getElementById to getElementByTagName
    for (var i = 0; i < paragraphList.length; i ++){
        paragraphList[i].innerHTML = paragraphList[i].innerHTML.replace(/<span>/g, "");
        paragraphList[i].innerHTML = paragraphList[i].innerHTML.replace(/<\/span>/g, "");
        paragraphList[i].innerHTML = paragraphList[i].innerHTML.replace(/<b>/g, "");
        paragraphList[i].innerHTML = paragraphList[i].innerHTML.replace(/<\/b>/g, "");
    }
}

/*
Bionic Reader converter that will bolden the the first three chars of a word string
*/
function BionicReader(){
    var paragraphList = document.getElementsByTagName("p"); 
    
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