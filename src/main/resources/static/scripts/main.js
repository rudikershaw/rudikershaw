$(document).ready(function(){

    // Setup share article dialog.
    var dialogBackground = $('div.faded-background-cover');

    $('a.home.share').click(function(event){
        event.preventDefault();
        $("body").css({"overflow":"hidden"});
        dialogBackground.fadeIn(200);
    });

    dialogBackground.click(function(){
        $("body").css({"overflow":"visible"});
        dialogBackground.fadeOut(200);
    });

    $('div.share-options').click(function(event){
        event.stopPropagation();
    });

    $(document).keyup(function(e) {
         if (e.keyCode === 27) { // Esc key
            $("body").css({"overflow":"visible"});
            dialogBackground.fadeOut(200);
        }
    });

    var address = $('a.home.social-share.twitter').attr("href");
    $('a.home.social-share.twitter').attr("href", address + '%0A' + encodeURI(document.location.href));
    address = $('a.home.social-share.facebook').attr("href");
    $('a.home.social-share.facebook').attr("href", address + encodeURIComponent(document.location.href));
    address = $('a.home.social-share.google-plus').attr("href");
    $('a.home.social-share.google-plus').attr("href", address + encodeURIComponent(document.location.href));
    address = $('a.home.social-share.reddit').attr("href");
    $('a.home.social-share.reddit').attr("href", address + encodeURIComponent(document.location.href));
});
