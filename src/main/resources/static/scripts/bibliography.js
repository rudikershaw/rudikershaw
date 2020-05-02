(function() {
    var template = document.querySelector('#bibliography-item-template');
    var list = document.querySelector('#bibliography-list');
    var request = new XMLHttpRequest();

    function populatePageWithBibliography(response) {
        if (response && response.bibliography) {
            response.bibliography.forEach(function (item){
                var newEntry = template.cloneNode(true);
                newEntry.querySelector('cite').textContent = item.title;
                newEntry.querySelector('.author').textContent = item.author;
                newEntry.removeAttribute('style');
                list.appendChild(newEntry);
            });
        }
    }

    request.open("GET", "/data/bibliography.json", true);
    request.setRequestHeader('Accept', 'application/json');
    request.onreadystatechange = function() {
        if (request.readyState === XMLHttpRequest.DONE && request.status === 200) {
            populatePageWithBibliography(JSON.parse(request.responseText));
        }
    };
    request.send();
})();
