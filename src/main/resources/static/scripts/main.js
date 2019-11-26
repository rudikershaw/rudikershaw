$(document).ready(function() {
    $('div.faded-background-cover').fadeOut(1600);

    $.ajax({ url: '/latest-tweet' }).done(function(data){
        $('#latest-tweet').replaceWith(data);
    });
});
