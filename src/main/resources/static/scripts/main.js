import 'vanilla-fade/dist/esm/fadeOut';

(() => {
    fetch('/latest-reddit-post')
        .then(data => {
            if (data.ok) {
                return data.text();
            } else {
                throw new Error(data.statusText);
            }
        }).then(text => {
            document.querySelector('#latest-reddit-post').innerHTML = text;
        }).catch(error => {
            console.error('The Latest Reddit Post web service failed to retrieve a post.', error);
        });

    fetch('/data/bibliography.json')
        .then(data => data.json())
        .then(json => {
            const latestRead = json.bibliography[0];
            document.querySelector('#latest-read .title').textContent = latestRead.title;
            document.querySelector('#latest-read .author').textContent = latestRead.author;
            document.querySelector('#latest-read .synopsis').textContent = latestRead.synopsis;
            document.querySelector('#latest-read .review').textContent = latestRead.review;
            document.querySelector('#latest-read').style.display = null;
        });

    fetch('/data/quotes.json')
        .then(data => data.json())
        .then(json => {
            const quote = json.quotes[Math.floor(Math.random() * json.quotes.length)];
            document.querySelector('#quote-block .quote-text').textContent = '“' + quote.text + '”';
            document.querySelector('#quote-block .quote-attribution').innerHTML = '- ' + quote.author + ', <cite>' + quote.source + '</cite>';
            document.querySelector('#quote-block').style.display = null;
        });

    const curtain = document.querySelector('div.faded-background-cover');
    curtain.fadeOut(1600, 'linear', () => curtain.style.display = 'none');
})();
