// Page number.
var page = 0;
// Whether waiting on an AJAX response.
var waiting = false;
// Template for new entry cards.
var entryCard = null;
// Page container to populate.
var pageContainer = null;

$(document).ready(function(){
    // On click for next page button.
    $('.next').click(function(){
        if(waiting || $('.next').hasClass('next-off')) return;
        ++page;
        $('.previous').removeClass('previous-off');
        pageContainer.fadeTo('fast', 0, function(){
            getPage(page);
        });
    });
    // On click for previous page button.
    $('.previous').click(function(){
        if(page == 0 || waiting) return;
        if(--page == 0) $('.previous').addClass('previous-off');
        $('.next').removeClass('next-off');
        pageContainer.fadeTo('fast', 0, function(){
            getPage(page);
        });

    });
    // Get a template entry card.
    entryCard = $('.entry-card').first();
    pageContainer = $('#page');
});

// Get page of articles using AJAX request.
function getPage(page){
    waiting = true;
    pageContainer.empty();
    setTimeout(addImageIfWaiting, 500);
    $.ajax({
        url: '/list/' + page,
        dataType: 'JSON',
    }).done(function(data){
        // If no data go back a page.
        if(data.length == 0){
            if(--page == 0) $('.previous').addClass('previous-off');
            $('.next').addClass('next-off');
            getPage(page);
            return;
        }
        // If less than 4 entries, disable next button.
        if(data.length < 4){
            $('.next').addClass('next-off');
        }
        waiting = false;
        pageContainer.empty();
        pageContainer.fadeTo(0,0);
        $.each(data, function(index, article){
            entryCard.attr('href', article['path']);
            entryCard.find('h4').text(article['name']);
            entryCard.find('p').text(article['description']);
            entryCard.find('img').attr('src', article['imagePath']);
            pageContainer.append(entryCard.clone());
        });
        pageContainer.fadeTo('fast', 100);
    });
}

function addImageIfWaiting(){
    if(waiting && $('#waiting') != null){
        var img = $('<img width="200" height="200" id="waiting">');
        img.attr('src', '/images/coolloading.gif');
        pageContainer.append(img);
        pageContainer.fadeTo('fast',1);
    }
}