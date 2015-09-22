$(document).ready(function(){
    $('.container input[name="e-search-go"]').click(function(){
        var button = $(this);
        var searchFor = $('.container input[name="e-search-for"]').val();
        button.parent().find('li').removeClass('checked found');
        setTimeout(function(){
            button.parent().find('li').each(function(index, element){
                if($(element).text() == searchFor){
                    setTimeout(function(){
                        $(element).addClass('found');
                    }, 500*index);
                    throw "Found";
                } else {
                    setTimeout(function(){
                        $(element).addClass('checked');
                    }, 500*index);
                }

            });
        },500);
    });

    $('.container input[name="b-search-go"]').click(function(){
            var button = $(this);
            var find = $('.container input[name="b-search-for"]').val();
            button.parent().find('li').removeClass('checked found');
            setTimeout(function(){
                var bottom = 0;
                var top = 13;
                var lis = button.parent().find('li');
                var time = 0;
                while(bottom <= top)
                {
                    var middle = (bottom + top)/2 | 0;
                    if($(lis.get(middle)).text() == find) {
                        setTimeout(function(){
                            $(lis.get(middle)).addClass('found');
                        },500*time);
                        throw "Found";
                    } else if(parseInt($(lis.get(middle)).text()) > find) {
                        (function(middle){
                        setTimeout(function(){
                            $(lis.get(middle)).addClass('checked');
                        },500*time)}(middle));
                        top = middle - 1;
                    } else if(parseInt($(lis.get(middle)).text()) < find) {
                        (function(middle){
                        setTimeout(function(){
                            $(lis.get(middle)).addClass('checked');
                        },500*time)}(middle));
                        bottom = middle + 1;
                    }
                    time++;
                }
            },500);
        });
});

