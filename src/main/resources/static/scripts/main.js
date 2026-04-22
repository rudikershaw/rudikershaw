import 'vanilla-fade/dist/esm/fadeOut';

(() => {
    fetch('/latest-reddit-post')
        .then(data => {
            if (data.ok) {
                return data.json();
            } else {
                throw new Error(data.statusText);
            }
        }).then(post => {
            if (!post || typeof post.link !== 'string' || !post.link.startsWith('https://www.reddit.com/')) {
                return;
            }
            const row = document.querySelector('#latest-reddit-post-row');
            const subredditEl = row.querySelector('.reddit-subreddit');
            const dateEl = row.querySelector('.reddit-date');
            if (post.subreddit) {
                subredditEl.textContent = 'r/' + post.subreddit;
            }
            if (post.published) {
                dateEl.textContent = (post.subreddit ? ' · ' : '') + post.published;
            }
            row.querySelector('.reddit-title').textContent = post.title || '';
            const body = row.querySelector('.reddit-body');
            if (post.description) {
                body.textContent = post.description;
            } else {
                body.style.display = 'none';
            }
            row.querySelector('.reddit-link').href = post.link;
            row.style.display = null;
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
