    //url별 사이드바 메뉴 active 하기
    var pageUrl = location.href; //창의 url을 가져온다.
    $(window).on('load', function(){ //load가 되었을때 실행
        $('#nav > nav > ul > li').siblings('li').removeClass('active'); //다른 active가 있으면 지워준다.

        if (pageUrl.indexOf('schedule') > -1) { //url에 schedule이라는 글자가 있으면 실행
            $('#nav > nav > ul > li').eq(1).addClass('active');
        } else if (pageUrl.indexOf('board') > -1) { //url에 board라는 글자가 있으면 실행
            $('#nav > nav > ul > li').eq(2).addClass('active');
        }else if (pageUrl.indexOf('mypage') > -1) { //url에 board라는 글자가 있으면 실행
            $('#nav > nav > ul > li').eq(3).addClass('active');
        }else {
            $('#nav > nav > ul > li').eq(0).addClass('active');
            //홈 url은 단어가 들어가지 않아서 모든 조건이 아닐때 실행하도록 함
        }
    });


