var choiceButtons = $('.choices li');

$(document).ready(function(){

    $('div.choices').each(function(){
        var tallest = 0;
        $(this).find('pre.code.choice').each(function(){
              var height = $(this).height();
              if (height > tallest) {
                tallest = height;
              }
        });
        $(this).find('pre.code.choice').height(tallest);
    });

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