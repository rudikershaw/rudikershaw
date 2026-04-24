const fs = require("fs");
const path = require("path");
const WhichX = require("whichx");

const LABELS = ["fiction", "science", "history", "philosophy", "technical"];

const PATHS = {
    training: path.join(__dirname, "data/training-examples.jsonl"),
    bibliography: path.join(__dirname, "..", "src/main/resources/static/data/bibliography.json"),
    model: path.join(__dirname, "..", "src/main/resources/static/data/genre-model.json")
};

// Stopwords limited to words with no genre signal in a book-review context.
const EXTRA_STOPWORDS = [
    "book", "books", "read", "reads", "reading", "reader", "readers",
    "recommend", "recommended", "recommendation", "recommendations", "recommending",
    "worth",
    "really", "very", "quite", "rather", "bit", "lot", "lots", "many", "much",
    "good", "great", "excellent", "nice", "fine",
    "chapter", "chapters", "page", "pages", "paragraph", "paragraphs",
    "author", "authors", "written", "writes", "wrote", "writer", "writers",
    "cover", "covers", "covered", "covering",
    "particular", "particularly", "especially", "specifically",
    "probably", "perhaps", "definitely", "certainly", "maybe",
    "like", "liked", "likes", "liking",
    "best", "better", "favourite", "favorite", "top",
    "first", "second", "third",
    "overall", "generally", "broadly", "usually", "often", "sometimes",
    "point", "points", "part", "parts", "section", "sections",
    "also", "just", "even", "still", "yet",
    "say", "says", "said", "saying",
    "something", "someone", "anything", "anyone", "everything", "everyone", "nothing",
    "thing", "things",
    "way", "ways",
    "get", "got", "gets", "getting",
    "make", "made", "makes", "making",
    "see", "saw", "seen", "sees", "seeing",
    "take", "took", "taken", "takes", "taking",
    "give", "gave", "given", "gives", "giving",
    "come", "came", "comes", "coming",
    "find", "found", "finds", "finding"
];

const buildDoc = e => [e.title, e.author, e.synopsis, e.review].join(" ");

function loadJsonl(filePath) {
    return fs.readFileSync(filePath, "utf8")
        .split("\n")
        .filter(l => l.trim().length > 0)
        .map(l => JSON.parse(l));
}

function countBy(items, key) {
    const counts = Object.fromEntries(LABELS.map(l => [l, 0]));
    for (const item of items) counts[item[key]]++;
    return counts;
}

function validateLabels(items, source) {
    for (const item of items) {
        if (!LABELS.includes(item.genre)) {
            throw new Error(`${source} entry "${item.title}" has invalid genre "${item.genre}"`);
        }
    }
}

function trainClassifier(examples) {
    const classifier = new WhichX({ stopwords: WhichX.getDefaultStopwords().concat(EXTRA_STOPWORDS) });
    classifier.addLabels(LABELS);
    for (const e of examples) classifier.addData(e.genre, buildDoc(e));
    return classifier;
}

function evaluate(classifier, entries) {
    const matrix = Object.fromEntries(LABELS.map(a => [a, Object.fromEntries(LABELS.map(p => [p, 0]))]));
    let correct = 0;
    for (const e of entries) {
        const predicted = classifier.classify(buildDoc(e));
        matrix[e.genre][predicted]++;
        if (predicted === e.genre) correct++;
    }
    return { matrix, correct, total: entries.length };
}

function perClassMetrics(matrix, support) {
    const result = {};
    for (const label of LABELS) {
        const tp = matrix[label][label];
        const fn = LABELS.reduce((s, p) => s + (p === label ? 0 : matrix[label][p]), 0);
        const fp = LABELS.reduce((s, a) => s + (a === label ? 0 : matrix[a][label]), 0);
        const precision = tp + fp === 0 ? 0 : tp / (tp + fp);
        const recall = tp + fn === 0 ? 0 : tp / (tp + fn);
        const f1 = precision + recall === 0 ? 0 : (2 * precision * recall) / (precision + recall);
        result[label] = {
            support: support[label],
            precision: round3(precision),
            recall: round3(recall),
            f1: round3(f1)
        };
    }
    return result;
}

function macroF1(perClass) {
    const f1s = LABELS.map(l => perClass[l].f1);
    return f1s.reduce((a, b) => a + b, 0) / f1s.length;
}

// The majority-class baseline always predicts the largest class. Accuracy
// equals the proportion of the evaluation set belonging to that class.
function majorityClassAccuracy(evalCounts, total) {
    return Math.max(...Object.values(evalCounts)) / total;
}

const round3 = n => Number(n.toFixed(3));
const pct = n => `${(100 * n).toFixed(1)}%`;

function printReport(report) {
    const { trainCounts, evalCounts, accuracy, macroF1: mF1, baseline, perClass, matrix, trainingSize, evaluationSize } = report;

    console.log(`Training set: ${trainingSize} synthetic examples`);
    console.log(`Training counts: ${LABELS.map(l => `${l}=${trainCounts[l]}`).join(", ")}`);
    console.log(`\nEvaluation set: ${evaluationSize} bibliography entries`);
    console.log(`Eval counts: ${LABELS.map(l => `${l}=${evalCounts[l]}`).join(", ")}`);

    console.log(`\nOverall accuracy: ${pct(accuracy)} (macro-F1: ${pct(mF1)}) vs ${pct(baseline)} majority-class baseline.`);

    console.log("\nPer-class precision / recall / F1:");
    for (const label of LABELS) {
        const m = perClass[label];
        console.log(`  ${label.padEnd(11)} precision=${pct(m.precision)}  recall=${pct(m.recall)}  f1=${pct(m.f1)}  (support=${m.support})`);
    }

    console.log("\nConfusion matrix (rows=actual, cols=predicted):");
    const header = "actual\\pred".padEnd(14) + LABELS.map(l => l.slice(0, 5).padStart(6)).join(" ");
    console.log(header);
    for (const actual of LABELS) {
        const row = actual.padEnd(14) + LABELS.map(p => String(matrix[actual][p]).padStart(6)).join(" ");
        console.log(row);
    }
}

const trainingExamples = loadJsonl(PATHS.training);
validateLabels(trainingExamples, "Training");

const bibliography = JSON.parse(fs.readFileSync(PATHS.bibliography, "utf8")).bibliography;
validateLabels(bibliography, "Bibliography");

const classifier = trainClassifier(trainingExamples);
fs.writeFileSync(PATHS.model, JSON.stringify(classifier.export()));

const trainCounts = countBy(trainingExamples, "genre");
const evalCounts = countBy(bibliography, "genre");
const { matrix, correct, total } = evaluate(classifier, bibliography);
const accuracy = correct / total;
const perClass = perClassMetrics(matrix, evalCounts);
const mF1 = macroF1(perClass);
const baseline = majorityClassAccuracy(evalCounts, total);

printReport({
    trainingSize: trainingExamples.length,
    evaluationSize: total,
    trainCounts,
    evalCounts,
    accuracy,
    macroF1: mF1,
    baseline,
    perClass,
    matrix
});
