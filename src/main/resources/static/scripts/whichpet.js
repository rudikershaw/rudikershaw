// Whichpet version v1.0.1
// Defining the Whichpet object.
function Whichpet() {
    // Internet explorer 9 or later required, or any other popular browser.

    // Map (using object notation) of description types.
    // Each type containing a map of words and counts.
    // The tcount represents the total number of those labels.
    // The word total represents the total number of words added against that label.
    var typesMap = {
        // Total must exist and be incremented for probability calculations.
        "total": { "tcount": 0, "wordTotal": 0 }
    };

    // Add a label or list of labels to the classifier
    this.addLabels = function(labels) {
        var i = 0;
        if (typeof labels === "string" && labels.length > 0 && !(labels.toLowerCase() in typesMap)) {
            typesMap[labels.toLowerCase()] = { "tcount": 0, "wordTotal": 0 };
        } else if (labels instanceof Array) {
            for (i; i < labels.length; i++) {
                if (typeof labels[i] === "string" && labels[i].length > 0 && !(labels[i].toLowerCase() in typesMap)) {
                    typesMap[labels[i].toLowerCase()] = { "tcount": 0, "wordTotal": 0 };
                } else {
                    throw new Error("Invalid label");
                }
            }
        } else {
            throw new Error("Invalid label");
        }
    };

    // Add word data from a description to a specified label.
    this.addData = function(label, description) {
        var type, wordArray, i, word;
        var total = typesMap.total;

        if (label in typesMap && typeof description === "string") {
            type = typesMap[label];
            type.tcount = type.tcount + 1;
            total.tcount = total.tcount + 1;
            wordArray = processToArray(description);
            // Check whether each word exists against that label and the total.
            // If it does increment the tcount, otherwise add the word.
            for (i = 0; i < wordArray.length; i++) {
                word = wordArray[i];
                // Add/Increment word to specific label.
                if (word in type) {
                    type[word] = type[word] + 1;
                } else {
                    type[word] = 1;
                }
                // Add/Increment word to total
                if (word in total) {
                    total[word] = total[word] + 1;
                } else {
                    total[word] = 1;
                }
                type.wordTotal = type.wordTotal + 1;
                total.wordTotal = total.wordTotal + 1;
            }
        } else {
            throw new Error("Invalid label or description");
        }
    };

    // Take a description and find the most likely label for it.
    this.classify = function(description) {
        var wordArray, bestChance, bestLabel, typeName,
            type, typeChance;

        if (typeof description === "string" && description.length > 0) {
            wordArray = processToArray(description);
            bestChance = 0;
            bestLabel = "pet";

            // Loop through types working out the chance of the description being
            // for this type. If better than bestChance then bestChange <- chance.
            for (typeName in typesMap) {
                if (typesMap.hasOwnProperty(typeName)) {
                    type = typesMap[typeName];
                    typeChance = getTypeChance(type, wordArray);
                    if (typeChance > bestChance) {
                        bestChance = typeChance;
                        bestLabel = typeName;
                    }
                }
            }
            return bestLabel;
        } else {
            throw new Error("Invalid description");
        }
    };

    // Loop through words and work out probability of type given each word.
    // Multiply each word's probability by total probability to determine type probability.
    function getTypeChance(type, words) {
        var i, typeWordCount, totalWordCount, p1, p2, wordChance;
        var typeChance = 0;
        var total = typesMap.total;

        for (i = 0; i < words.length; i++) {
            typeWordCount = (typeof type[words[i]] !== "undefined" ? type[words[i]] : mEstimate());
            totalWordCount = (typeof total[words[i]] !== "undefined" ? total[words[i]] : mEstimate());
            // Bayes' theorem calculation.
            p1 = (typeWordCount / type.wordTotal) * (type.tcount / total.tcount);
            p2 = ((totalWordCount - typeWordCount / (total.wordTotal - type.wordTotal)) * ((total.tcount - type.tcount) / total.tcount));
            wordChance = p1 / (p1 + p2);
            if (typeChance <= 0) {
                typeChance = wordChance;
            } else {
                typeChance = typeChance * wordChance;
            }
        }
        // Multiply final probability by overall probability that it is of this type to weight by most popular types.
        return typeChance * (type.tcount / total.tcount);
    }

    // A non-zero prior estimate to prevent 0 based probability calculations.
    function mEstimate() {
        var total = typesMap.total;
        return 1 / (total.wordTotal * 100);
    }

    // Stop words including tcount & wordtotal (because they are key words in the maps used to store the data).
    var STOPWORDS = ["a", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because",
        "been", "being", "but", "by", "count", "could", "did", "do", "does", "doing", "during",
        "each", "few", "for", "had", "has", "have", "having", "he", "hed", "hes",
        "her", "here", "heres", "hers", "herself", "him", "himself", "his", "how",
        "hows", "i", "id", "im", "ive", "if", "in", "into", "is", "it", "its", "itself",
        "lets", "me", "more", "most", "my", "myself", "of", "off", "on", "once",
        "only", "or", "other", "ought", "our", "ours", "ourselves", "over", "own",
        "same", "she", "shes", "should", "so", "some", "such", "than", "that",
        "thats", "the", "their", "theirs", "them", "themselves", "then", "there",
        "theres", "these", "they", "theyd", "theyll", "theyre", "theyve", "this",
        "those", "through", "to", "too", "until", "was", "we", "wed", "well", "were",
        "weve", "what", "whats", "when", "whens", "where", "wheres", "which",
        "while", "who", "whos", "whom", "why", "whys", "with", "wordtotal", "would", "you", "youd",
        "youll", "youre", "your", "youve", "yours", "yourself", "yourselves"];

    // Process the description into an array of pertinent standardized lower case words.
    function processToArray(description) {
        var i = 0;
        if (typeof description === "string") {
            // Remove special characters.
            description = description.replace(/[^a-zA-Z ]/g, "");
            // Lower case.
            description = description.toLowerCase();
            // Remove all stop words
            for (i; i < STOPWORDS.length; i++) {
                description = description.replace(new RegExp(" " + STOPWORDS[i] + " ", "g"), " ");
            }
            // Remove extra spaces.
            description = description.replace(/\s+/g, " ");
            // Return array of processed words.
            return description.split(" ");
        } else {
            throw new Error("Invalid description");
        }
    }
}

// UI Setup for the Whichpet article.
$(document).ready(function(){
    // Set up toggle for reading how it works.
    $('#click-for-more a').click(function(){
        $('#they-clicked-for-more').slideToggle();
        var caret = $('#click-for-more span');
        if(caret.text() == String.fromCharCode(9658)){
            caret.html('&#9660;');
        } else {
            caret.html('&#9658;');
        }
    });
    // Retrieve the Whichcat UI with an AJAX request.
    $.ajax({ url: '/fragments/whichpet' }).done(function(data){
        $('#whichpet-target').html(data);
        // Create the Whichpet object.
        var whichpet = new Whichpet();
        // Define details of popup. If classifier guesses right, re-inforce data,
        // otherwise ask while label to add description to.
        $('#input input[type="button"]').click(function(){
            var label = $('#input').find('select option:selected').val();
            var description = $('#which-pet').find('textarea').val();
            whichpet.addData(label, description);
            $.ajax({ url: '/fragments/whichpet/add?label='+encodeURIComponent(label)+'&description='+encodeURIComponent(description) }).done(function(data){
                console.log(data);
            });
            $('.whichpet div.popup').fadeOut(function(){
                $('.whichpet div.buttons').show();
                $('#input').hide();
            });
        });
        $('.whichpet input[name="yes"]').click(function(){
            $('.whichpet div.popup').fadeOut();
            var description = $('#which-pet').find('textarea').val();
            var label = whichpet.classify(description);
            whichpet.addData(label, description);
            $.ajax({ url: '/fragments/whichpet/add?label='+encodeURIComponent(label)+'&description='+encodeURIComponent(description) }).done(function(data){
                console.log(data);
            });
        });
        $('.whichpet input[name="no"]').click(function(){
            $('.whichpet div.buttons').hide();
            $('#input').show();
        });
        // Set up Whichpet to try and guess animal label.
        $('#which-pet input[type="button"]').click(function(){
            var description = $('#which-pet').find('textarea').val();
            $('.whichpet div.popup h3').text('Are you describing a ' + whichpet.classify(description) + '?');
            $('.whichpet div.popup').fadeIn();
        });
        $('a.close-link').click(function(){
            $('.whichpet div.popup').fadeOut(function(){
                $('.whichpet div.buttons').show();
                $('#input').hide();
            });
        });
        // Load labels into data set.
        var labels = ["cat","dog","fish","horse","bird","reptile"];
        whichpet.addLabels(labels);
        // Load training set which should be pulled in with the fragment.
        for(var i = 0; i < pets.length; i++){
            whichpet.addData(pets[i].label, pets[i].description);
        }
    });
});
