import 'vanilla-fade/dist/esm/fadeOut';

(function() {
    const template = document.querySelector('#bibliography-item-template');
    const list = document.querySelector('#bibliography-list');

    function daysBetweenDates(date1, date2) {
        const oneDayInMiliseconds = 24 * 60 * 60 * 1000;
        return Math.round(Math.abs((date1 - date2) / oneDayInMiliseconds));
    }

    function createIcon(reviewText) {
        const infoIcon = document.createElement('span');
        infoIcon.textContent = '\u2139';
        infoIcon.title = reviewText;
        return infoIcon;
    }

    function populatePageWithBibliography(response) {
        let newEntry;
        let newDateDividor;
        let currentFinishedDate;
        let previousFinishedDate = new Date();

        if (response && response.bibliography) {
            response.bibliography.forEach(function (item){
                newEntry = template.cloneNode(true);
                currentFinishedDate = new Date(item.dateFinished);
                newEntry.querySelector('cite').textContent = item.title;
                newEntry.querySelector('.author').textContent = item.author;
                newEntry.querySelector('.synopsis').textContent = item.synopsis;
                newEntry.querySelector('.synopsis').appendChild(createIcon(item.review));

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

    fetch('/data/bibliography.json', { headers: { 'Content-Type': 'application/json'} })
        .then(data => data.json())
        .then(json => populatePageWithBibliography(json));

    fetch('/data/quotes.json')
        .then(data => data.json())
        .then(json => {
            const quote = json.quotes[Math.floor(Math.random() * json.quotes.length)];
            document.querySelector('#quote-block .quote-text').textContent = '"' + quote.text + '"';
            document.querySelector('#quote-block .quote-attribution').innerHTML = '- ' + quote.author + ', <cite>' + quote.source + '</cite>';
            document.querySelector('#quote-block').style.display = null;
        });

    const curtain = document.querySelector('div.faded-background-cover');
    curtain.fadeOut(1600, 'linear', () => curtain.style.display = 'none');
})();
