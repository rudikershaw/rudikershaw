import 'vanilla-fade/dist/esm/fadeOut';
import WhichX from 'whichx';

(function() {
    const LABELS = ['fiction', 'science', 'history', 'philosophy', 'technical'];

    // Classification cascade: delay per step follows (1 - i/(n-1))^p, scaled so
    // the whole cascade fits inside TOTAL_BUDGET_MS — slow start, rapid finish.
    const TOTAL_BUDGET_MS = 6000;
    const MIN_DELAY_MS = 15;
    const MAX_DELAY_MS = 500;
    const CURVE_EXPONENT = 3;

    const MS_PER_DAY = 24 * 60 * 60 * 1000;
    const CURTAIN_FADE_MS = 1600;

    const template = document.querySelector('#bibliography-item-template');
    const list = document.querySelector('#bibliography-list');
    const dividerTemplate = list.querySelector('.date-divider');

    const sleep = ms => new Promise(r => setTimeout(r, ms));
    const daysBetween = (a, b) => Math.round(Math.abs((a - b) / MS_PER_DAY));
    const buildDoc = item => [item.title, item.author, item.synopsis, item.review].join(' ');

    function computeDelays(n) {
        if (n <= 1) return [MIN_DELAY_MS];
        const span = MAX_DELAY_MS - MIN_DELAY_MS;
        const raw = [];
        let total = 0;
        for (let i = 0; i < n; i++) {
            const w = Math.pow(1 - i / (n - 1), CURVE_EXPONENT);
            const d = w * span + MIN_DELAY_MS;
            raw.push(d);
            total += d;
        }
        const scale = total > TOTAL_BUDGET_MS ? TOTAL_BUDGET_MS / total : 1;
        return raw.map(d => Math.max(MIN_DELAY_MS, d * scale));
    }

    function cascade(items, handler) {
        const delays = computeDelays(items.length);
        const step = i => {
            if (i >= items.length) return Promise.resolve();
            handler(items[i], i);
            return sleep(delays[i]).then(() => step(i + 1));
        };
        return step(0);
    }

    function fadeInto(parent, el) {
        parent.appendChild(el);
        requestAnimationFrame(() => el.classList.add('is-visible'));
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
        tag.append(dot, labelEl);
        return tag;
    }

    function appendDateDivider(label) {
        const divider = dividerTemplate.cloneNode(true);
        divider.querySelector('h2').textContent = label;
        list.appendChild(divider);
    }

    function renderEntries(bibliography) {
        const nodes = [];
        let previousDate = new Date();

        bibliography.forEach(item => {
            const entry = template.cloneNode(true);
            const currentDate = new Date(item.dateFinished);
            entry.querySelector('cite').textContent = item.title;
            entry.querySelector('.author').textContent = item.author;
            entry.querySelector('.synopsis').textContent = item.synopsis;

            const review = entry.querySelector('.review');
            const wrapper = entry.querySelector('.review-wrapper');
            const toggle = entry.querySelector('.review-toggle');
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

            entry.removeAttribute('style');
            entry.removeAttribute('id');

            if (previousDate.getFullYear() !== currentDate.getFullYear()) {
                appendDateDivider(currentDate.getFullYear());
            }
            entry.style.marginTop = daysBetween(previousDate, currentDate) + 'px';

            list.appendChild(entry);
            nodes.push({ item: item, node: entry });
            previousDate = currentDate;
        });

        appendDateDivider('Etc');
        triggerEnterAnimation();
        return nodes;
    }

    function triggerEnterAnimation() {
        // Stagger each row's enter transition so items trail the rail falling.
        const rows = list.querySelectorAll(':scope > li');
        const last = Math.max(rows.length - 1, 1);
        rows.forEach((li, i) => {
            li.style.transitionDelay = (300 + (i / last) * 900).toFixed(0) + 'ms';
        });
        // Double rAF so the browser paints the starting state before transitioning.
        requestAnimationFrame(() => requestAnimationFrame(() => list.classList.add('is-ready')));
    }

    function setupGenreKey() {
        const key = document.querySelector('#genre-key');
        const tbody = key.querySelector('.genre-key-table tbody');
        const rows = {};
        LABELS.forEach(label => {
            const tr = document.createElement('tr');
            tr.innerHTML =
                '<td><span class="genre-dot" data-genre="' + label + '"></span>' + label + '</td>' +
                '<td class="cell-precision">–</td>' +
                '<td class="cell-recall">–</td>' +
                '<td class="cell-f1">–</td>' +
                '<td class="cell-seen">0</td>';
            tbody.appendChild(tr);
            rows[label] = tr;
        });
        key.style.display = null;
        return { rows: rows, summary: key.querySelector('.genre-key-summary') };
    }

    function computeStats(matrix) {
        const perClass = {};
        for (const label of LABELS) {
            const tp = matrix[label][label];
            const fn = LABELS.reduce((s, p) => s + (p === label ? 0 : matrix[label][p]), 0);
            const fp = LABELS.reduce((s, a) => s + (a === label ? 0 : matrix[a][label]), 0);
            const support = tp + fn;
            const precision = tp + fp === 0 ? null : tp / (tp + fp);
            const recall = support === 0 ? null : tp / support;
            const f1 = (precision === null || recall === null || precision + recall === 0)
                ? null
                : (2 * precision * recall) / (precision + recall);
            perClass[label] = { precision, recall, f1, support };
        }
        return perClass;
    }

    const fmtPct = v => v === null ? '–' : (v * 100).toFixed(0) + '%';

    function renderStats(rows, perClass) {
        for (const label of LABELS) {
            const row = rows[label];
            const c = perClass[label];
            row.querySelector('.cell-precision').textContent = fmtPct(c.precision);
            row.querySelector('.cell-recall').textContent = fmtPct(c.recall);
            row.querySelector('.cell-f1').textContent = fmtPct(c.f1);
            row.querySelector('.cell-seen').textContent = c.support;
        }
    }

    function renderSummary(summaryEl, matrix, perClass) {
        const total = LABELS.reduce((s, l) => s + perClass[l].support, 0);
        if (total === 0) {
            summaryEl.textContent = '';
            return;
        }
        const correct = LABELS.reduce((s, l) => s + matrix[l][l], 0);
        const accuracy = correct / total;
        const f1s = LABELS.map(l => perClass[l].f1).filter(v => v !== null);
        const macroF1 = f1s.length ? f1s.reduce((s, v) => s + v, 0) / f1s.length : null;
        const biggestSupport = Math.max(...LABELS.map(l => perClass[l].support));
        const baseline = biggestSupport / total;
        const num = v => '<span class="num">' + fmtPct(v) + '</span>';
        summaryEl.innerHTML =
            'Overall accuracy: ' + num(accuracy) +
            ' (macro-F1: ' + num(macroF1) + ')' +
            ' vs ' + num(baseline) + ' majority-class baseline.';
    }

    function classifyCascade(nodes, classifier, refs) {
        const matrix = Object.fromEntries(LABELS.map(a =>
            [a, Object.fromEntries(LABELS.map(p => [p, 0]))]));
        const predictions = [];

        return cascade(nodes, ({ item, node }) => {
            const predicted = classifier.classify(buildDoc(item));
            predictions.push(predicted);

            fadeInto(node.querySelector('.entry-meta'), makeGenreTag(predicted, 'is-prediction'));

            matrix[item.genre][predicted]++;
            const perClass = computeStats(matrix);
            renderStats(refs.rows, perClass);
            renderSummary(refs.summary, matrix, perClass);
        }).then(() => predictions);
    }

    function correctnessCascade(nodes, predictions) {
        return cascade(nodes, ({ item, node }, i) => {
            const meta = node.querySelector('.entry-meta');
            if (predictions[i] === item.genre) {
                const check = document.createElement('span');
                check.className = 'genre-correct';
                check.setAttribute('aria-label', 'correct');
                check.innerHTML = '<i class="fa fa-check" aria-hidden="true"></i>';
                fadeInto(meta, check);
            } else {
                meta.querySelector('.genre-tag.is-prediction').classList.add('is-wrong');
                fadeInto(meta, makeGenreTag(item.genre, 'is-truth'));
            }
        });
    }

    Promise.all([
        fetch('/data/bibliography.json').then(r => r.json()),
        fetch('/data/genre-model.json').then(r => r.ok ? r.json() : null).catch(() => null)
    ]).then(([bibliography, model]) => {
        const entries = bibliography && bibliography.bibliography ? bibliography.bibliography : [];
        const nodes = renderEntries(entries);

        if (!model) return;

        const classifier = new WhichX();
        classifier.import(model);
        const refs = setupGenreKey();

        // Let the curtain fade clear so no classifications are burned behind it.
        return sleep(CURTAIN_FADE_MS)
            .then(() => classifyCascade(nodes, classifier, refs))
            .then(predictions => correctnessCascade(nodes, predictions));
    });

    fetch('/data/quotes.json')
        .then(r => r.json())
        .then(json => {
            const quote = json.quotes[Math.floor(Math.random() * json.quotes.length)];
            document.querySelector('#quote-block .quote-text').textContent = '"' + quote.text + '"';
            document.querySelector('#quote-block .quote-attribution').innerHTML =
                '- ' + quote.author + ', <cite>' + quote.source + '</cite>';
            document.querySelector('#quote-block').style.display = null;
        });

    const curtain = document.querySelector('div.faded-background-cover');
    curtain.fadeOut(CURTAIN_FADE_MS, 'linear', () => curtain.style.display = 'none');
})();
