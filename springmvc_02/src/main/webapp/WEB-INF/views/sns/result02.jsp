<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>네이버 로그인</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
	<!-- 네이버 결과 -->
	<h2>네이버 로그인 성공</h2>
	<div id="result"></div>
	<!-- 네이버는 별도의 로그아웃이 없다 -->
	<a href="/naverLogout">로그아웃</a>
	
	<script type="text/javascript">
		$(function() {
			$("#result").empty();
			$.ajax({
				url : "/naverUserInfo",
				method : "post",
				dataType : "json",
				success : function(data) {
					let name = data.response.name;
					let nickname = data.response.nickname;
					let email = data.response.email;
					let profile_image = data.response.profile_image;
					
					let str = "<li>" + name + "</li>";
						str += "<li>" + nickname + "</li>";
						str += "이미지: <img src=" + profile_image + ">";
				$('#result').append(str);
				},
				error : function() {
					alert("읽기 실패");
				}
			});
		});
	</script>
</body>
</html>