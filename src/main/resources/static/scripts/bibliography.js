import 'vanilla-fade/dist/esm/fadeOut';
import WhichX from 'whichx';

(function() {
    const LABELS = ['fiction', 'science', 'history', 'philosophy', 'technical'];
    const TOTAL_BUDGET_MS = 6000;
    const MIN_DELAY_MS = 15;
    const MAX_DELAY_MS = 500;
    const CURVE_EXPONENT = 3;

    const template = document.querySelector('#bibliography-item-template');
    const list = document.querySelector('#bibliography-list');

    function daysBetweenDates(date1, date2) {
        const oneDayInMiliseconds = 24 * 60 * 60 * 1000;
        return Math.round(Math.abs((date1 - date2) / oneDayInMiliseconds));
    }

    function buildDoc(item) {
        return [item.title, item.author, item.synopsis, item.review].join(' ');
    }

    function sleep(ms) {
        return new Promise(r => setTimeout(r, ms));
    }

    // Delay follows (1 - i/(n-1))^p — slow start, sharp acceleration near the
    // end. Total bounded by TOTAL_BUDGET_MS so it never drags past that.
    function delayForIndex(i, n) {
        if (n <= 1) return MIN_DELAY_MS;
        let totalUnscaled = 0;
        for (let k = 0; k < n; k++) {
            const w = Math.pow(1 - k / (n - 1), CURVE_EXPONENT);
            totalUnscaled += w * (MAX_DELAY_MS - MIN_DELAY_MS) + MIN_DELAY_MS;
        }
        const weightI = Math.pow(1 - i / (n - 1), CURVE_EXPONENT);
        const unscaled = weightI * (MAX_DELAY_MS - MIN_DELAY_MS) + MIN_DELAY_MS;
        const scale = totalUnscaled > TOTAL_BUDGET_MS ? TOTAL_BUDGET_MS / totalUnscaled : 1;
        return Math.max(MIN_DELAY_MS, unscaled * scale);
    }

    function makeGenreTag(label, extraClass) {
        const tag = document.createElement('span');
        tag.className = 'genre-tag' + (extraClass ? ' ' + extraClass : '');
        const dot = document.createElement('span');
        dot.className = 'genre-dot';
        dot.dataset.genre = label;
        const labelEl = document.createElement('span');
        labelEl.className = 'genre-label';
        labelEl.textContent = label;
        tag.appendChild(dot);
        tag.appendChild(labelEl);
        return tag;
    }

    function renderEntries(bibliography) {
        const nodes = [];
        let newDateDividor;
        let currentFinishedDate;
        let previousFinishedDate = new Date();

        bibliography.forEach(function (item) {
            const newEntry = template.cloneNode(true);
            currentFinishedDate = new Date(item.dateFinished);
            newEntry.querySelector('cite').textContent = item.title;
            newEntry.querySelector('.author').textContent = item.author;
            newEntry.querySelector('.synopsis').textContent = item.synopsis;

            const review = newEntry.querySelector('.review');
            const wrapper = newEntry.querySelector('.review-wrapper');
            const toggle = newEntry.querySelector('.review-toggle');
            if (item.review) {
                review.textContent = item.review;
                toggle.addEventListener('click', () => {
                    const expanded = toggle.getAttribute('aria-expanded') === 'true';
                    toggle.setAttribute('aria-expanded', String(!expanded));
                    toggle.querySelector('.review-toggle-label').textContent =
                        expanded ? 'Read review' : 'Hide review';
                    wrapper.classList.toggle('open', !expanded);
                });
            } else {
                toggle.remove();
                wrapper.remove();
            }

            newEntry.removeAttribute('style');
            newEntry.removeAttribute('id');

            if (previousFinishedDate.getFullYear() !== currentFinishedDate.getFullYear()) {
                newDateDividor = list.querySelector('.date-divider').cloneNode(true);
                newDateDividor.querySelector('h2').textContent = currentFinishedDate.getFullYear();
                list.appendChild(newDateDividor);
            }
            if (previousFinishedDate) {
                newEntry.style.marginTop = daysBetweenDates(previousFinishedDate, currentFinishedDate) + 'px';
            }

            list.appendChild(newEntry);
            nodes.push({ item: item, node: newEntry });
            previousFinishedDate = currentFinishedDate;
        });

        newDateDividor = list.querySelector('.date-divider').cloneNode(true);
        newDateDividor.querySelector('h2').textContent = 'Etc';
        list.appendChild(newDateDividor);

        return nodes;
    }

    function setupGenreKey(total) {
        const key = document.querySelector('#genre-key');
        const tbody = key.querySelector('.genre-key-table tbody');
        const rowRefs = {};
        LABELS.forEach(label => {
            const tr = document.createElement('tr');
            tr.innerHTML =
                '<td><span class="genre-dot" data-genre="' + label + '"></span>' + label + '</td>' +
                '<td class="cell-precision">–</td>' +
                '<td class="cell-recall">–</td>' +
                '<td class="cell-f1">–</td>' +
                '<td class="cell-seen">0</td>';
            tbody.appendChild(tr);
            rowRefs[label] = tr;
        });
        key.style.display = null;
        return {
            rows: rowRefs,
            summary: key.querySelector('.genre-key-summary')
        };
    }

    function recomputeStats(matrix) {
        const perClass = {};
        for (const label of LABELS) {
            const tp = matrix[label][label];
            const fn = LABELS.reduce((s, p) => s + (p === label ? 0 : matrix[label][p]), 0);
            const fp = LABELS.reduce((s, a) => s + (a === label ? 0 : matrix[a][label]), 0);
            const precision = tp + fp === 0 ? null : tp / (tp + fp);
            const recall = tp + fn === 0 ? null : tp / (tp + fn);
            const f1 = (precision === null || recall === null || precision + recall === 0)
                ? null
                : (2 * precision * recall) / (precision + recall);
            perClass[label] = {
                precision: precision,
                recall: recall,
                f1: f1,
                support: LABELS.reduce((s, p) => s + matrix[label][p], 0)
            };
        }
        return perClass;
    }

    function renderStats(rowRefs, perClass) {
        const fmt = v => v === null ? '–' : (v * 100).toFixed(0) + '%';
        for (const label of LABELS) {
            const row = rowRefs[label];
            const c = perClass[label];
            row.querySelector('.cell-precision').textContent = fmt(c.precision);
            row.querySelector('.cell-recall').textContent = fmt(c.recall);
            row.querySelector('.cell-f1').textContent = fmt(c.f1);
            row.querySelector('.cell-seen').textContent = String(c.support);
        }
    }

    function renderSummary(summaryEl, matrix, perClass) {
        const fmt = v => v === null ? '–' : (v * 100).toFixed(0) + '%';
        let total = 0;
        let correct = 0;
        const supportByClass = {};
        for (const actual of LABELS) {
            for (const predicted of LABELS) {
                total += matrix[actual][predicted];
                if (actual === predicted) correct += matrix[actual][predicted];
            }
            supportByClass[actual] = LABELS.reduce((s, p) => s + matrix[actual][p], 0);
        }
        if (total === 0) {
            summaryEl.textContent = '';
            return;
        }
        const accuracy = correct / total;
        const f1s = LABELS.map(l => perClass[l].f1).filter(v => v !== null);
        const macroF1 = f1s.length ? f1s.reduce((s, v) => s + v, 0) / f1s.length : null;
        const biggestClassSupport = Math.max(...LABELS.map(l => supportByClass[l]));
        const baseline = biggestClassSupport / total;
        const num = v => '<span class="num">' + fmt(v) + '</span>';
        summaryEl.innerHTML =
            'Overall accuracy: ' + num(accuracy) +
            ' (macro-F1: ' + num(macroF1) + ')' +
            ' vs ' + num(baseline) + ' majority-class baseline.';
    }

    function classifyCascade(nodes, classifier, refs) {
        const matrix = Object.fromEntries(LABELS.map(a => [a, Object.fromEntries(LABELS.map(p => [p, 0]))]));
        const predictions = [];
        const n = nodes.length;

        const step = i => {
            if (i >= n) return Promise.resolve(predictions);
            const { item, node } = nodes[i];
            const predicted = classifier.classify(buildDoc(item));
            predictions.push(predicted);

            const meta = node.querySelector('.entry-meta');
            const tag = makeGenreTag(predicted, 'is-prediction');
            meta.appendChild(tag);
            requestAnimationFrame(() => tag.classList.add('is-visible'));

            if (LABELS.includes(item.genre)) {
                matrix[item.genre][predicted]++;
                const perClass = recomputeStats(matrix);
                renderStats(refs.rows, perClass);
                renderSummary(refs.summary, matrix, perClass);
            }

            return sleep(delayForIndex(i, n)).then(() => step(i + 1));
        };

        return step(0);
    }

    function correctnessCascade(nodes, predictions) {
        const n = nodes.length;

        const step = i => {
            if (i >= n) return Promise.resolve();
            const { item, node } = nodes[i];
            const predicted = predictions[i];
            const actual = item.genre;
            const meta = node.querySelector('.entry-meta');
            const predictionTag = meta.querySelector('.genre-tag.is-prediction');

            if (LABELS.includes(actual)) {
                if (predicted === actual) {
                    const check = document.createElement('span');
                    check.className = 'genre-correct';
                    check.setAttribute('aria-label', 'correct');
                    check.innerHTML = '<i class="fa fa-check" aria-hidden="true"></i>';
                    meta.appendChild(check);
                    requestAnimationFrame(() => check.classList.add('is-visible'));
                } else {
                    predictionTag.classList.add('is-wrong');
                    const correctTag = makeGenreTag(actual, 'is-truth');
                    meta.appendChild(correctTag);
                    requestAnimationFrame(() => correctTag.classList.add('is-visible'));
                }
            }

            return sleep(delayForIndex(i, n)).then(() => step(i + 1));
        };

        return step(0);
    }

    Promise.all([
        fetch('/data/bibliography.json', { headers: { 'Content-Type': 'application/json' } }).then(r => r.json()),
        fetch('/data/genre-model.json').then(r => r.ok ? r.json() : null).catch(() => null)
    ]).then(([bibliography, model]) => {
        const entries = bibliography && bibliography.bibliography ? bibliography.bibliography : [];
        const nodes = renderEntries(entries);

        if (!model) return;

        const classifier = new WhichX();
        classifier.import(model);

        const refs = setupGenreKey(entries.length);
        // Wait for the fade-in curtain to clear before the cascade starts, so
        // none of the cascade is burned up behind the fade.
        return sleep(1600)
            .then(() => classifyCascade(nodes, classifier, refs))
            .then(predictions => correctnessCascade(nodes, predictions));
    });

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
