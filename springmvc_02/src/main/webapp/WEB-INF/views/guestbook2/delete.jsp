<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 방 명 록 </title>
<style type="text/css">
	a { text-decoration: none;}
	table{width: 600px; border-collapse:collapse; text-align: center;}
	table,th,td{border: 1px solid black; padding: 3px}
	div{width: 600px; margin:auto; text-align: center;}
</style>
<script type="text/javascript">
	<c:if test="${pwdchk == 'fail'}">
		alert("비밀번호 틀림");
	</c:if>
</script>
</head>
<body>
	<div>
		<h2>방 명 록 2 : 삭제화면</h2>
		<hr>
		<p>[<a href="/gb2_list">목록으로 이동]</a></p>
		<form method="post">
			<table>
				<tr align="center">
					<td bgcolor="#99ccff">비밀번호</td>
					<td><input type="password" name="gb2_pw" size ="20"></td>
				</tr>
				
				<tfoot>
					<tr align="center">
						<td colspan="2">
						    <input type="hidden" name="gb2_idx" value="${gb2_idx }">
							<input type="button" value="삭제" onclick="delete_ok(this.form)">
							<input type="reset" value="취소">
						</td>
					</tr>
				</tfoot>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		function delete_ok(f) {
			f.action = "/gb2_delete_ok";
			f.submit();
		}
	</script>
</body>
</html>