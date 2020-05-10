import 'vanilla-fade/dist/esm/fadeOut';

(() => {
    fetch('/latest-tweet')
        .then((data) => data.text())
        .then((text) => {
            document.querySelector('#latest-tweet').outerHTML = text;
        });

    fetch('/data/bibliography.json')
        .then((data) => data.json())
        .then((json) => {
            const latestRead = json.bibliography[0];
            document.querySelector('#latest-read .title').textContent = latestRead.title;
            document.querySelector('#latest-read .author').textContent = 'by ' + latestRead.author;
            document.querySelector('#latest-read .synopsis').textContent = latestRead.synopsis;
            document.querySelector('#latest-read .review').textContent = latestRead.review;
            document.querySelector('#latest-read').style.display = null;
        });

    const curtain = document.querySelector('div.faded-background-cover');
    curtain.fadeOut(1600, 'linear', () => curtain.style.display = 'none');
})();
