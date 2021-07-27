<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
table {
	margin: 10px auto;
	width: 800px;
	border-collapse: collapse;
	font-size: 13pt;
	border-color: navy;
}

table, th, td {
	border: 1px solid black;
	padding: 8px;
}

.bg {
	background-color: #dedede;
	width: 30%;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		// 장바구니 담기
		$("#btn1").click(function() {
			// 로그인 유무
			if("${log_in}"=='1'){
				$.ajax({
					url : "addCart.do",
					method : "post",
					data : "idx=${vo.idx}",
					dataType : "text",
					success : function(data) {
						if (data == "1") {
							alert("카트에 담기 성공");
						} 
					},
					error : function() {
						alert("읽기실패");
					}
				});
			}else{
				alert("로그인 하세요");
				location.href = "login.do";
			}
		});
		$("#btn2").click(function() {
			if("${log_in}" == '1') {
				location.href="cartList_go.do";
			}else{
				alert("로그인 하세요");
				location.href = "login.do";
			}
		});
	});
</script>
</head>
<body>
	<%-- 현재 페이지에서 다른 페이지 가져오기  --%>
	<%@ include file="top.jsp"%>
	<hr>
	<table>
		<tr>
			<th class="bg">제품Category</th>
			<td>${vo.category }</td>
		</tr>
		<tr>
			<th class="bg">제품번호</th>
			<td>${vo.p_num }</td>
		</tr>
		<tr>
			<th class="bg">제품이름</th>
			<td>${vo.p_name }</td>
		</tr>
		<tr>
			<th class="bg">제품판매사</th>
			<td>${vo.p_company}</td>
		</tr>
		<tr>
			<th class="bg">제품가격</th>
			<td>시중가 : <fmt:formatNumber value="${vo.p_price }"
					pattern="#,###" />원 <font style="color: tomato">(할인가:<fmt:formatNumber
						value="${vo.p_saleprice}" pattern="#,###" />원 )
			</font></td>
		</tr>
		<tr>
			<th class="bg">제품설명</th>
			<td><pre>${vo.p_content}</pre></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><img
				src="resources/images/${vo.p_image_l}" style="width: 350px;"></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
			<input type="button" value="장바구니에 담기" id="btn1" /> 
			<input type="button" value="장바구니 보기"	id="btn2" />
		</tr>
	</table>
</body>
</html>