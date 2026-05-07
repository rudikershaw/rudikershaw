(() => {
    fetch('/data/quotes.json')
        .then(data => data.json())
        .then(json => {
            const quote = json.quotes[Math.floor(Math.random() * json.quotes.length)];
            document.querySelector('#quote-block .quote-text').textContent = '“' + quote.text + '”';
            document.querySelector('#quote-block .quote-attribution').innerHTML = '- ' + quote.author + ', <cite>' + quote.source + '</cite>';
            document.querySelector('#quote-block').style.display = null;
        });
})();