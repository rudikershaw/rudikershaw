const choiceButtons = $('.choices li');

$(document).ready(function(){

    $('div.choices').each(function(){
        let tallest = 0;
        $(this).find('pre.code.choice').each(function(){
              var height = $(this).height();
              if (height > tallest) {
                tallest = height;
              }
        });
        $(this).find('pre.code.choice').height(tallest);
    });

    choiceButtons.click(function(){
        const thisButton = $(this);
        const choices = thisButton.parents('.choices').children('.choice');

        thisButton.parent().children().removeClass('selected');
        thisButton.addClass('selected');

        choices.hide();
        choices.each(function(){
            const thisChoice = $(this);
            if(thisChoice.attr('choice') === thisButton.text()){
                thisChoice.show();
            }
        });
    });
});
