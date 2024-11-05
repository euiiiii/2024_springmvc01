<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 화면</title>
<script type="text/javascript">
	<c:if test="${result == '1'}">
		alert("회원가입 성공");
	</c:if>
	<c:if test="${result == '0'}">
		alert("회원가입 실패");
	</c:if>
	<c:if test="${loginchk == '0'}">
		alert("로그인 실패");
	</c:if>
</script>
</head>
<body>
<h2>로그인</h2>
	<form method="post" action="/login_login">
		<table>
			<thead>
				<tr><th>아이디</th><th>패스워드</th></tr>
			</thead>
			<tbody>
				<tr>
					<td><input type="text" size="14" name="m_id" required></td>
					<td><input type="password" size="14" name="m_pw" required></td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="2" align="center">
						<input type="submit" value="로그인">
					</td>
				</tr>
			</tfoot>
		</table>
	</form>
	<h2><a href="/login_join_form">회원가입</a></h2>
</body>
</html>