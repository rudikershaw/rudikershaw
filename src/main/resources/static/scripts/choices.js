var choiceButtons = $('.choices li');

$(document).ready(function(){
    choiceButtons.click(function(){
        var thisButton = $(this);
        thisButton.parent().children().removeClass('selected');
        thisButton.addClass('selected');

        var choices = thisButton.parents('.choices').children('.choice');
        choices.hide();
        choices.each(function(){
            thisChoice = $(this);
            if(thisChoice.attr('choice') == thisButton.text()){
                thisChoice.show();
            }
        });
    });
});