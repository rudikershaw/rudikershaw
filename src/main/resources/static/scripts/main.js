import $ from 'jquery';

$(document).ready(function() {
    $('div.faded-background-cover').fadeOut(1600);

    $.ajax({ url: '/latest-tweet' }).done(function(data){
        $('#latest-tweet').replaceWith(data);
    });

    $.ajax({ url: '/data/bibliography.json' }).done(function(data){
        const latestRead = data.bibliography[0];
        $('#latest-read .title').text(latestRead.title);
        $('#latest-read .author').text('by ' + latestRead.author);
        $('#latest-read .synopsis').text(latestRead.synopsis);
        $('#latest-read .review').text(latestRead.review);
        $('#latest-read').show();
    });
});
