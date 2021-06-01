import 'vanilla-fade/dist/esm/fadeOut';

(() => {
    fetch('/latest-tweet')
        .then(data => {
            if (data.ok) {
                return data.text();
            } else {
                throw new Error(data.statusText);
            }
        }).then(text => {
            document.querySelector('#latest-tweet').outerHTML = text;
        }).catch(error => {
            console.error('The Latest Tweet web service failed to retrieve a tweet.', error);
        });

    fetch('/data/bibliography.json')
        .then(data => data.json())
        .then(json => {
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
