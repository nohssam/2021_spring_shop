<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니 보기</title>
<style type="text/css">
table {
	margin: 10px auto;
	width: 800px;
	border-collapse: collapse;
	border-color: navy;
	font-size: 10pt;
}

table, th, td {
	border: 1px solid black;
	padding: 2px;
}

input{
	width: 50px; 
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		$.ajax({
			url : "cartList.do",
			method : "post",
			data : "id="+$("#log_id").val(),
			dataType : "json",
			success : function(data) {
				var total = 0 ;
				$.each(data,function(){
					tbody += "<tr>";
					tbody += "<td>"+this["p_num"]+"</td>"; 
					tbody += "<td>"+this["p_name"]+"</td>"; 
					tbody += "<td>"+this["p_price"]+" 원<br>";
					tbody += "<font style='color: tomato'>(세일가 : "+this["p_saleprice"]+" 원)</font></td>";
					
					tbody += "<input type='hidden' name='id' value='"+this["id"]+"'>"
					tbody += "<input type='hidden' name='p_num' value='"+this["p_num"]+"'>"

					tbody += "<td><input type='number' name='amount' id='amount' value='"+this["amount"]+"'>"
					tbody += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					tbody += "<input type='button' value='수정' id='editcart'></td>"
					tbody += "<td> "+ (this["p_saleprice"]) * parseInt(this["amount"]) +" 원</td>";
					tbody += "<td><input type='button' value='삭제' id='delcart'></td>"; 
					tbody += "</tr>";
					total = total + (this["p_saleprice"]) * parseInt(this["amount"]) ;
				});
					$("#tbody").append(tbody);
				
					tbody += "<tfoot>";
					tbody += "<tr style='text-align: right;'>";
					tbody += "<td colspan='6' style='padding-right: 50px;'>";
					tbody += "<h2> 총 결재액 : "+ total +" 원</h2>";
					tbody += "</tr>";
					tbody += "</tfoot>";
				$("#tbody").append(tbody);
			},
			error : function() {
				alert("읽기실패");
			}
		});
	});
	
</script>
</head>
<body>
	<%-- 현재 페이지에서 다른 페이지 가져오기  --%>
	<%@ include file="top.jsp" %>
	<hr>
	<table>
		<caption><h2> :: 장바구니 내용 :: </h2></caption>
		<thead>
			<tr bgcolor="#dedede">
				<th style="width: 10%">제품번호</th>
				<th style="width: 20%">제품명</th>
				<th style="width: 20%">단가</th>
				<th style="width: 20%">수량</th>
				<th style="width: 15%">금액</th>
				<th style="width: 10%">삭제</th>
			</tr>
		</thead>
		<tbody id="tbody"></tbody>
	</table>
</body>
</html>