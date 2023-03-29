function itemChange(e){
    var cloth_top = ["민소매", "반팔", "긴팔", "외투"];
    	var cloth_bottom = ["반바지", "면바지", "청바지", "치마"];
    	var target = document.getElementById("cKind2");

    	if(e.value == "top") var d = cloth_top;
    	else if(e.value == "bottom") var d = cloth_bottom;

    	target.options.length = 0;

    	for (x in d) {
    		var opt = document.createElement("option");
    		opt.value = d[x];
    		opt.innerHTML = d[x];
    		target.appendChild(opt);
    	}
}

function itemChange2(e){
    var cloth_top_long = ["니트", "맨투맨", "셔츠"];
    	var cloth_outer = ["자켓", "야상", "트렌치코트", "코트", "패딩", "가디건"];
    	var cloth_none = ["-"];
    	var target = document.getElementById("cKind3");

    	if(e.value == "긴팔") var d = cloth_top_long;
    	else if(e.value == "외투") var d = cloth_outer;
    	else var d = cloth_none;

    	target.options.length = 0;

    	for (x in d) {
    		var opt = document.createElement("option");
    		opt.value = d[x];
    		opt.innerHTML = d[x];
    		target.appendChild(opt);
    	}
}



