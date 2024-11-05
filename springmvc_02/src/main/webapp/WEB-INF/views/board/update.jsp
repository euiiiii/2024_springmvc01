<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
table {
		width:700px;
}
tr {
	    text-align:center;
	    padding:4px 10px;
	    background-color: #F6F6F6;
	}
	
th {
		width:120px;
	    text-align:center;
	    padding:4px 10px;
	    background-color: #B2CCFF;
	}
input{
	margin: 5px;
	padding: 5px;
}
</style>

<script type="text/javascript">
  <c:if test="${pwdchk == 'fail'}">
 	 alert("비밀번호 틀림");
  </c:if>
</script>

</head>
<body>
	<div  align="center">
	<form method="post" enctype="multipart/form-data">
		<table summary="게시판 수정">
			<caption><h2>Board 게시판 수정</h2></caption>
		<tbody>
			<tr>
				<th>작성자</th>
				<td align="left"><input type="text" name="writer" value="${bovo.writer}"></td>
			</tr>
			<tr>
				<th>제목</th>
				<td align="left"> <input type="text" name="title" value="${bovo.title}"></td>
			</tr>
			<tr>
				<th>내용</th>
				<td align="left"><textarea rows="10" cols="60" name="content">${bovo.content}</textarea>
				</td>
			</tr>
			<tr>
				<th>첨부파일</th>
				<c:choose>
					<c:when test="${empty bovo.f_name}">
					   <td>
					   		<input type="file" name="file_name"><b>이전 파일 없음</b>
					   		<input type="hidden" name="old_f_name" value="">
					   	</td>
					</c:when>
					<c:otherwise>
						<td>
					   		<input type="file" name="file_name"><br><b>${bovo.f_name}</b>
					   		<input type="hidden" name="old_f_name" value="${bovo.f_name }">
					   	</td>
					</c:otherwise>
				</c:choose>
				
			</tr>
			<tr>
				<th>비밀번호</th>
				<td align="left"><input type="password" name="pwd"></td>
			</tr>
			<tr>
				<td colspan="2">
				    <input type="hidden" name="cPage" value="${cPage }" >
				    <input type="hidden" name="idx" value="${idx }" >
				    
					<input type="button" value="수정완료" onclick="board_update_ok(this.form)" > 
					<input type="reset" value="취소" />
					<input type="button" value="목록" onclick="bord_list_go(this.form)" > 
				</td>
			</tr>
            </tbody>
		</table>
	</form>
	</div>
	<script type="text/javascript">
		function board_update_ok(f) {
			for (let i = 0; i < f.elements.length; i++) {
				if(f.elements[i].value == ""){
					if(i==3 || i==4)  continue ;
					alert(f.elements[i].name + "를 입력하세요");
					f.elements[i].focus();
					return;
				}
			}
			f.action="/board_update_ok" ;
			f.submit();
		}
		
		function bord_list_go(f) {
			f.action="/board_list" ;
			f.submit();
		}
	</script>
</body>
</html>