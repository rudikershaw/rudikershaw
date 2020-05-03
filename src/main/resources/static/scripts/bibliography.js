(function() {
    var template = document.querySelector('#bibliography-item-template');
    var list = document.querySelector('#bibliography-list');
    var request = new XMLHttpRequest();

    function daysBetweenDates(date1, date2) {
        const oneDayInMiliseconds = 24 * 60 * 60 * 1000;
        return Math.round(Math.abs((date1 - date2) / oneDayInMiliseconds));
    }

    function populatePageWithBibliography(response) {
        var newEntry;
        var newDateDividor;
        var currentFinishedDate;
        var previousFinishedDate = new Date();

        if (response && response.bibliography) {
            response.bibliography.forEach(function (item){
                newEntry = template.cloneNode(true);
                currentFinishedDate = new Date(item.dateFinished);
                newEntry.querySelector('cite').textContent = item.title;
                newEntry.querySelector('.author').textContent = item.author;
                newEntry.querySelector('.synopsis').textContent = item.synopsis;
                newEntry.removeAttribute('style');
                newEntry.removeAttribute('id');

                if (previousFinishedDate.getFullYear() !== currentFinishedDate.getFullYear()) {
                    newDateDividor = list.querySelector('.date-divider').cloneNode(true);
                    newDateDividor.textContent = currentFinishedDate.getFullYear();
                    list.appendChild(newDateDividor);
                }
                if (previousFinishedDate) {
                    newEntry.style.marginTop = daysBetweenDates(previousFinishedDate, currentFinishedDate) + 'px';
                }

                list.appendChild(newEntry);
                previousFinishedDate  = currentFinishedDate;
            });

            newDateDividor = list.querySelector('.date-divider').cloneNode(true);
            newDateDividor.textContent = 'Etc';
            list.appendChild(newDateDividor);
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
    $('div.faded-background-cover').fadeOut(1600);
})();
