import 'vanilla-fade/dist/esm/fadeOut';

(function() {
    const template = document.querySelector('#bibliography-item-template');
    const list = document.querySelector('#bibliography-list');

    function daysBetweenDates(date1, date2) {
        const oneDayInMiliseconds = 24 * 60 * 60 * 1000;
        return Math.round(Math.abs((date1 - date2) / oneDayInMiliseconds));
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
        .then((data) => data.json())
        .then((json) => populatePageWithBibliography(json));

    const curtain = document.querySelector('div.faded-background-cover');
    curtain.fadeOut(1600, 'linear', () => curtain.style.display = 'none');
})();
