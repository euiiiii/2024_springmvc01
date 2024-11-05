<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카카오 로그인</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
	<!-- 카카오 결과 -->
	<h2>카카오 로그인 성공</h2>
	<div id="result"></div>
	<!-- 계정과 함께 로그아웃 -->
	<a href="https://kauth.kakao.com/oauth/logout?client_id=f51e94df215c297f090d59e3af4159dc&logout_redirect_uri=http://localhost:8080/kakaologout">로그아웃</a>
	
	<script type="text/javascript">
		// $(document).ready(function() { }); // 정석
		$(function() {
			$("#result").empty();
			$.ajax({
				url : "/kakaoUserInfo",
				method : "post",
				dataType : "json",
				success : function(data) {
					const nicakname = data.properties.nickname;
					const profile_image = data.properties.profile_image;
					const email = data.kakao_account.email;
					
					let str = "<li> 닉네임 : " + nicakname + "</li>"
						str+= "<li> 이메일 : " + email + "</li>";
						str+= "이미지 : <img src=" + profile_image + ">";
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