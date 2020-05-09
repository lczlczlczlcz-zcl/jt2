var TT = JT = {
	checkLogin : function(){
		//利用jQuery动态获取cookie记录信息.
		var _ticket = $.cookie("JT_TICKET");
		//JS中if简化判断的写法.
		if(!_ticket){	//如果ticket信息为null则执行teturn
			return ;
		}
		//当dataType类型为jsonp时，jQuery就会自动在请求链接上增加一个callback的参数
		$.ajax({
			url : "http://sso.jt.com/user/query/" + _ticket,
			dataType : "jsonp",	//发起JSONP的请求
			type : "GET",
			success : function(data){
				//返回值SysResult对象
				if(data.status == 200){
					//把json串转化为js对象
					//alert(data.data);
					var _data = JSON.parse(data.data);
					//alert(_data);
					var html =_data.username+"，欢迎来到京淘！<a href=\"http://www.jt.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});