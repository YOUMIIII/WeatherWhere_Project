/*//비번확인도 실시간 체크하고싶은데 잘 안되서 일단 멈춤
function samePassword(pwCheck){
    var pas = pwCheck.value;
    var passCheck = 'dkssud';
    var message = "";

    if(pas.length){
        if(pas !=== passCheck){
            message = "비밀번호가 일치하지 않습니다";
        }else{
            message = "비밀번호가 일치합니다";
        }
    }
    document.getElementById("pwChT").innerHTML = message;
}*/

function mailPattern(str){
    var mail = str.value;
    var message = "";
    var color = "";

    if(mail.length){
        var pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
        if(pattern.test(mail) == false){
            message = "이메일 형식이 맞지 않습니다";
            color = "#FF8C42";
        }else{
            message = "비밀번호 찾기 시 발송될 이메일 입니다";
            color = "#0000CD";
        }
    } else{
        message = "이메일을 작성해주세요";
        color = "red";

    }
    document.getElementById("mailT").innerHTML = message;
    document.getElementById("mailT").style.color = color;
}


//비번 유효성 체크
function safetyPasswordPattern(str) {
    var pass = str.value;
    var message = "";
    var color = "";
    var checkPoint = 0;

    // 입력값이 있을경우에만 실행
    if(pass.length) {

        // 최대 입력 글자수를 제한한다.
        if(pass.length < 8 || pass.length > 16) {
            message = "비밀번호는 최소 8글자, 최대 16글자 이하로 입력해주세요";
            color = "red";
        }

        // 문자열의 길이가 8 ~ 16 인경우
        else {

        // 비밀번호 문자열에 숫자 존재 여부 검사
              var pattern1 = /[0-9]/;  // 숫자
              if(pattern1.test(pass) == false) {
                  checkPoint = checkPoint + 1;
              }

              // 비밀번호 문자열에 영문 소문자 존재 여부 검사
              var pattern2 = /[a-zA-Z]/;
              if(pattern2.test(pass) == false) {
                  checkPoint = checkPoint + 1;
              }
              // 비밀번호 문자열에 특수문자 존재 여부 검사
              var pattern4 = /[~!@#$%^&*()_+|<>?:{}]/;  // 특수문자
              if(pattern4.test(pass) == false) {
                  checkPoint = checkPoint + 1;
              }

              if(checkPoint >= 2) {
                  message = "🔒보안성이 낮은 비밀번호";
                  color = "#A23E48";
              } else if(checkPoint == 1) {
                  message = "🔒보안성이 보통인 비밀번호";
                  color = "#FF8C42";
              } else {
                  message = "🔒보안성이 강력한 비밀번호";
                  color = "#0000CD";
              }
        }
    }

    else {
        message = "비밀번호는 최소 8글자, 최대 16글자 이하로 입력해주세요";
        color = "red";
    }
    document.getElementById("pwT").innerHTML = message;
    document.getElementById("pwT").style.color = color;
}

//필수항목체크해야 가입 버튼 활성화 되는거(다시 수정하기)
 function agreeCheck(frm){
    if (frm.submitBtn.disabled==true)
        frm.submitBtn.disabled=false
    else
        frm.submitBtn.disabled=true
}

//체크박스 전체 선택/해제
function allCheckboxes(boxNames, chkMode){
    const el = document.getElementsByName(boxNames);
    for(let i = 0; i < el.length; i++){
        if(!el[i].disabled){
            el[i].checked = chkMode;
        }
    }
}

//전체 체크 여부
function isAllCheck(boxNames, allChkName){
    const el = document.getElementsByName(boxNames);
    let checkboxCnt = 0;
    let checkedCnt = 0;
    for(let i = 0; i < el.length; i++){
        if(!el[i].disabled){
            checkboxCnt += 1;
            if(el[i].checked){
                checkedCnt += 1;
            }
        }
    }
    let chkMode = false;
    //체크박스 개수와 체크 된 체크박스 개수와 일치할 경우
    if(checkboxCnt == checkedCnt){
        chkMode = true;
    } else {
        chkMode = false;
    }
    //일치할 경우 천제 체크 박스는 체크, 일치하지 않을 경우 해제
    document.getElementById(allChkName).checked = chkMode;
}