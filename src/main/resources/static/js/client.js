/**
 * Created by stephan on 20.03.16.
 */

$(function () {
	// VARIABLES =============================================================
	var TOKEN_KEY = "jwtToken"
	var $notLoggedIn = $("#notLoggedIn");
	var $loggedIn = $("#loggedIn").hide();
	var $loggedInBody = $("#loggedInBody");
	var $response = $("#response");
	var $login = $("#login");
	var $userInfo = $("#userInfo").hide();
	//===CREATE BY GARY
	$("#addarticle").hide();
	$("#articleInfo").hide();
	$('#articlePage').hide();

	// FUNCTIONS =============================================================
	function getJwtToken() {
		return localStorage.getItem(TOKEN_KEY);
	}

	function setJwtToken(token) {
		localStorage.setItem(TOKEN_KEY, token);
	}

	function removeJwtToken() {
		localStorage.removeItem(TOKEN_KEY);
	}

	//登录
	function doLogin(loginData) {
		$.ajax({
			url: "/auth",
			type: "POST",
			data: JSON.stringify(loginData),
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function (data, textStatus, jqXHR) {
				setJwtToken(data.token);
				$login.hide();
				$notLoggedIn.hide();
				showValidateToken();//显示token状态
				showTokenInformation();
				showUserInformation();
				$("#addarticle").show();//显示
				$("#articleInfo").show();//显示文章列表
				$('#articlePage').show();
				showarticleDetailsInformation()
			},
			error: function (jqXHR, textStatus, errorThrown) {
				if (jqXHR.status === 401) {
					$('#loginErrorModal')
					.modal("show")
					.find(".modal-body")
					.empty()
					.html("<p>Spring exception:<br>" + jqXHR.responseJSON.exception + "</p>");
				} else {
					throw new Error("an unexpected error occured: " + errorThrown);
				}
			}
		});
	}

	//退出
	function doLogout() {
		removeJwtToken();//移除token相当于退出，类似于移除session
		$login.show();
		$userInfo.hide().find("#userInfoBody").empty();
		$loggedIn.hide();
		$loggedInBody.empty();
		$notLoggedIn.show();
		$("#addarticle").hide();
		$("#articleInfo").hide();
		$("#articlePage").hide();
	}

	//生成Token请求头
	function createAuthorizationTokenHeader() {
		var token = getJwtToken();//获取jwtToken
		if (token) {
			return {"Authorization": "Bearer " + token};//返回Token请求头
		} else {
			return {};
		}
	}
	
	//展示token是否过期
	function showValidateToken() {
		$.ajax({
			url: "/jwtuser/validata",//声明请求地址
			type: "GET",//声明请求方法
			contentType: "application/json; charset=utf-8",//声明内容格式
			dataType: "json",//声明数据类型
			headers: createAuthorizationTokenHeader(),
			success: function (data, textStatus, jqXHR) {
				//alert(data.status);
				if(data.status != 200){//如果过期则执行退出函数，移除token
					alert(data.status + "登录过期");
					doLogout();
				}
			}
		});	
	}

	//展示用户信息
	function showUserInformation() {
		$.ajax({
			url: "/jwtuser/info",//声明请求地址
			type: "GET",//声明请求方法
			contentType: "application/json; charset=utf-8",//声明内容格式
			dataType: "json",//声明数据类型
			headers: createAuthorizationTokenHeader(),//声明Token请求头，发送给服务器请求数据
			success: function (data, textStatus, jqXHR) {//请求成功，执行该函数
				
				/*if(jqXHR.status != 200){
					doLogout();
				}*/
				
				showResponse(jqXHR.status, JSON.stringify(data));

				var $userInfoBody = $userInfo.find("#userInfoBody");

				$userInfoBody.append($("<div>").text("ID: " + data.id));
				$userInfoBody.append($("<div>").text("Username: " + data.username));
				$userInfoBody.append($("<div>").text("Email: " + data.email));
				$userInfoBody.append($("<div>").text("FirstName: " + data.firstname));
				$userInfoBody.append($("<div>").text("imgurl: " + data.imgurl));
				$userInfoBody.append($("<div>").text("jqXHR.status: " + jqXHR.status));

				var $authorityList = $("<ul>");
				data.authorities.forEach(function (authorityItem) {
					$authorityList.append($("<li>").text(authorityItem.authority));
				});
				var $authorities = $("<div>").text("Authorities:");
				$authorities.append($authorityList);

				$userInfoBody.append($authorities);
				$userInfo.show();
			},
			error:function(){
				doLogout()
			}
		});
	}

	//展示token信息
	function showTokenInformation() {
		var jwtToken = getJwtToken();
		var decodedToken = jwt_decode(jwtToken);

		$loggedInBody.append($("<h4>").text("Token"));
		$loggedInBody.append($("<div>").text(jwtToken).css("word-break", "break-all"));
		$loggedInBody.append($("<h4>").text("Token claims"));

		var $table = $("<table>")
		.addClass("table table-striped");
		appendKeyValue($table, "sub", decodedToken.sub);
		appendKeyValue($table, "aud", decodedToken.aud);
		appendKeyValue($table, "iat", decodedToken.iat);
		appendKeyValue($table, "exp", decodedToken.exp);

		$loggedInBody.append($table);

		$loggedIn.show();
	}

	function appendKeyValue($table, key, value) {
		var $row = $("<tr>")
		.append($("<td>").text(key))
		.append($("<td>").text(value));
		$table.append($row);
	}

	//展示response
	function showResponse(statusCode, message) {
		$response
		.empty()
		.text("status code: " + statusCode + "\n-------------------------\n" + message);
	}

	// REGISTER EVENT LISTENERS =============================================================
	//点击按钮提交数据
	$("#loginForm").submit(function (event) {
		event.preventDefault();

		var $form = $(this);
		var formData = {
				username: $form.find('input[name="username"]').val(),
				password: $form.find('input[name="password"]').val()
		};

		doLogin(formData);//跳到登录函数
	});

	//点击退出
	$("#logoutButton").click(doLogout);
	
	//移除token
	$("#removetokenButton").click(function () {
		removeJwtToken();//移除token
		window.location.reload();//刷新当前页面.
	});

	//persons
	$("#exampleServiceBtn").click(function () {
		$.ajax({
			url: "/persons",
			type: "GET",
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			headers: createAuthorizationTokenHeader(),
			success: function (data, textStatus, jqXHR) {
				showResponse(jqXHR.status, JSON.stringify(data));
			},
			error: function (jqXHR, textStatus, errorThrown) {
				showResponse(jqXHR.status, errorThrown);
			}
		});
	});

	//admin
	$("#adminServiceBtn").click(function () {
		$.ajax({
			url: "/protected",
			type: "GET",
			contentType: "application/json; charset=utf-8",
			headers: createAuthorizationTokenHeader(),
			success: function (data, textStatus, jqXHR) {
				showResponse(jqXHR.status, data);
			},
			error: function (jqXHR, textStatus, errorThrown) {
				showResponse(jqXHR.status, errorThrown);
			}
		});
	});

	$loggedIn.click(function () {
		$loggedIn
		.toggleClass("text-hidden")
		.toggleClass("text-shown");
	});

	// INITIAL CALLS 如果token为true执行下列函数，避免页面刷新后默认隐藏的函数============
	if (getJwtToken()) {
		$login.hide();
		$notLoggedIn.hide();
		showTokenInformation();
		showUserInformation();
		showArticleInformation();
		addArticleInformation();
		showArticleInformationPage();
		showarticleDetailsInformation();
		showValidateToken();//检查token是否过期
	}
	
	
	// WRITE BY GARY 2018.02.09====================================================
	//所有文章列表
	function showArticleInformation() {
		$.ajax({
			url: "/article/list",//声明请求地址
			type: "GET",//声明请求方法
			contentType: "application/json; charset=utf-8",//声明内容格式
			dataType: "json",//声明数据类型
			//headers: createAuthorizationTokenHeader(),//声明Token请求头，发送给服务器请求数据
			success: function (data, textStatus, jqXHR){
				var article = "<ul>";
				//i表示在data中的索引位置，n表示包含的信息的对象，data表示（json类型）参数
				$.each(data, 
						function(i, n) {
					//获取对象中属性为title的值
					article += "<li>" 
						+ n["id"] + ". "
						+ "<b>" + n["title"] +"<b>" + "     "
						+ "#" + n["lable"] + "#" + "     "
						+ "Time:" + "<time>" + n["createDate"] + "</time>"
						+ "<br>" + n["content"]
					+ "</li>";
					//alert(i);//0
					//alert(n);//[object Object]
					//alert(data);//[object Object],[object Object],[object Object],[object Object],[object Object],[object Object]
				});
				"</ul>";
				//append() 方法在被选元素的结尾（仍然在内部）插入指定内容
				$('#articleInfoBody').append(article);
				$("#articleInfo").show();//显示文章列表
			}
		});
	}
	
	//文章分页
	function showArticleInformationPage() {
		var params = {
				page:1,
				size:1
		};
		$.ajax({
			url: "/article/page_str",//声明请求地址
			type: "GET",//声明请求方法
			contentType: "application/json;charset=gb2312",//声明内容格式
			dataType: "json",//声明数据类型
			data: params,
			//headers: createAuthorizationTokenHeader(),//声明Token请求头，发送给服务器请求数据
			success: function (data, textStatus, jqXHR){
				var article = "<ul>";
				//i表示在data中的索引位置，n表示包含的信息的对象，data表示（json类型）参数
				$.each(data, 
						function(i, n) {
					//获取对象中属性为title的值
					article += "<li>" 
						+ n["id"] + ". "
						+ "<b>" + n["title"] +"<b>" + "     "
						+ "#" + n["lable"] + "#" + "     "
						+ "Time:" + "<time>" + n["createDate"] + "</time>"
						+ "<br>" + n["content"]
					+ "</li>";
					//alert(i);//0
					//alert(n);//[object Object]
					//alert(data);//[object Object],[object Object],[object Object],[object Object],[object Object],[object Object]
				});
				"</ul>";
				//append() 方法在被选元素的结尾（仍然在内部）插入指定内容
				$('#articleInfoBodyPage').append(article);
				$("#articlePage").show();//显示文章列表
			}
		});
	}
	
	//显示添加文章组件(添加文章组件默认隐藏)
	function addArticleInformation() {
		$("#addarticle").show();
	}
	
	//添加文章
	$("#articleForm").submit(function doaddArticleInformation() {
		var params = {
				title : $('#title').val(),
				content : $('#content').val(),
				lable : $('#lable').val()
		};
		$.ajax({
			url: "/userarticle/add",//声明请求地址
			type: "POST",//声明请求方法
			//contentType: "application/x-www-form-urlencoded; charset=utf-8",//声明内容格式
			//dataType: "json",//声明数据类型,提交数据不需要json格式
			data: params,//声明变量
			headers: createAuthorizationTokenHeader(),
			success: function(data) {
				alert(data.data);//弹框
				window.location.reload();//刷新页面
			},
			error: function() {
				alert("晕...发帖成功！");
			}
		});
	}
	);
	
	//展示专辑信息
	function showarticleDetailsInformation() {
		$.ajax({
			url: "/article/bean",//声明请求地址
			type: "GET",//声明请求方法
			contentType: "application/json;charset=utf-8",//声明内容格式
			data:{"id":3},
			dataType: "json",//声明数据类型
			//headers: createAuthorizationTokenHeader(),//声明Token请求头，发送给服务器请求数据
			success: function (data) {//请求成功，执行该函数

				var $articleDetailsInfoBody = $("#articleDetailsInfo").find("#articleDetailsInfoBody");

				$articleDetailsInfoBody.append($("<div>").text("ID: " + data.id));
				$articleDetailsInfoBody.append($("<div>").text("Title: " + data.title));
				$articleDetailsInfoBody.append($("<div>").text("Content: " + data.content));

				//$("#articleDetails").show();
			}
		});
	}
	
	//===============END by Gary================
	
});

