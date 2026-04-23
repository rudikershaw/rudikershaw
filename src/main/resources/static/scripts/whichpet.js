import $ from 'jquery';
import Whichpet from 'whichx';

// UI Setup for the Whichpet article.
(() => {
    const LABELS = ['cat', 'dog', 'fish', 'horse', 'bird', 'reptile'];
    const HOLDOUT_FRACTION = 0.1;

    const TOTAL_BUDGET_MS = 6000;
    const MIN_DELAY_MS = 15;
    const MAX_DELAY_MS = 200;
    const CURVE_EXPONENT = 3;

    const sleep = ms => new Promise(r => setTimeout(r, ms));

    function shuffle(arr) {
        const a = arr.slice();
        for (let i = a.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [a[i], a[j]] = [a[j], a[i]];
        }
        return a;
    }

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

    function setupPetKey() {
        const key = document.querySelector('#pet-key');
        const tbody = key.querySelector('.pet-key-table tbody');
        const rows = {};
        LABELS.forEach(label => {
            const tr = document.createElement('tr');
            tr.innerHTML =
                '<td><span class="pet-dot" data-pet="' + label + '"></span>' + label + '</td>' +
                '<td class="cell-precision">–</td>' +
                '<td class="cell-recall">–</td>' +
                '<td class="cell-f1">–</td>' +
                '<td class="cell-seen">0</td>';
            tbody.appendChild(tr);
            rows[label] = tr;
        });
        key.style.display = null;
        return { rows: rows, summary: key.querySelector('.pet-key-summary') };
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

    function evaluate(classifier, holdout, refs) {
        const matrix = Object.fromEntries(LABELS.map(a =>
            [a, Object.fromEntries(LABELS.map(p => [p, 0]))]));

        return cascade(holdout, item => {
            if (!LABELS.includes(item.label)) return;
            const predicted = classifier.classify(item.description);
            if (!LABELS.includes(predicted)) return;
            matrix[item.label][predicted]++;
            const perClass = computeStats(matrix);
            renderStats(refs.rows, perClass);
            renderSummary(refs.summary, matrix, perClass);
        });
    }

    // Set up toggle for reading how it works.
    document.querySelector('#click-for-more a').onclick = () => {
        $('#they-clicked-for-more').slideToggle();
        const caret = $('#click-for-more span');
        if (caret.text() === String.fromCharCode(9658)) {
            caret.html('&#9660;');
        } else {
            caret.html('&#9658;');
        }
    };

    // Retrieve the Whichpet UI with an AJAX request.
    fetch('/fragments/whichpet')
        .then(data => data.text())
        .then(text => {
            $('#whichpet-target').html(text);
            // Create the Whichpet object.
            const whichpet = new Whichpet();
            whichpet.addLabels(LABELS);

            // Shuffle pets and hold out 10% for evaluation.
            const shuffled = shuffle(window.pets || []);
            const holdoutSize = Math.round(shuffled.length * HOLDOUT_FRACTION);
            const holdout = shuffled.slice(0, holdoutSize);
            const training = shuffled.slice(holdoutSize);
            for (let i = 0; i < training.length; i++) {
                whichpet.addData(training[i].label, training[i].description);
            }

            // Interactive classifier: on "no" the user corrects us, on "yes" we re-enforce.
            document.querySelector('#input input[type="button"]').onclick = () => {
                const label = $('#input').find('select option:selected').val();
                const description = $('#which-pet').find('textarea').val();
                whichpet.addData(label, description);
                $.ajax({ url: '/fragments/whichpet/add?label=' + encodeURIComponent(label) + '&description=' + encodeURIComponent(description) }).done(function (data) {
                    console.log(data);
                });
                $('.whichpet div.popup').fadeOut(function () {
                    $('.whichpet div.buttons').show();
                    $('#input').hide();
                });
            };
            $('.whichpet input[name="yes"]').click(function () {
                $('.whichpet div.popup').fadeOut();
                const description = $('#which-pet').find('textarea').val();
                const label = whichpet.classify(description);
                whichpet.addData(label, description);
                $.ajax({ url: '/fragments/whichpet/add?label=' + encodeURIComponent(label) + '&description=' + encodeURIComponent(description) }).done(function (data) {
                    console.log(data);
                });
            });
            $('.whichpet input[name="no"]').click(function () {
                $('.whichpet div.buttons').hide();
                $('#input').show();
            });
            $('#which-pet input[type="button"]').click(function () {
                const description = $('#which-pet').find('textarea').val();
                $('.whichpet div.popup h3').text('Are you describing a ' + whichpet.classify(description) + '?');
                $('.whichpet div.popup').fadeIn();
            });
            $('a.close-link').click(function () {
                $('.whichpet div.popup').fadeOut(function () {
                    $('.whichpet div.buttons').show();
                    $('#input').hide();
                });
            });

            // Run evaluation cascade over the holdout, then fold the holdout
            // back into the model so no training data is wasted.
            if (holdout.length > 0) {
                const refs = setupPetKey();
                evaluate(whichpet, holdout, refs).then(() => {
                    for (let i = 0; i < holdout.length; i++) {
                        whichpet.addData(holdout[i].label, holdout[i].description);
                    }
                });
            }
        });
})();
