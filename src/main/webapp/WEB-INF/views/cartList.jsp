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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		function cartList() {
			$("#tbody").empty();
			$.ajax({
				url : "cartList.do",
				method : "post",
				dataType : "json",
				success : function(data) {
					var tbody="";
					var total = 0 ;
					$.each(data, function() {
						tbody += "<tr>";
						tbody += "<td><input type='checkbox' name='chk_id' value='"+this["id"]+"'></td>";
						tbody += "<td>"+this["p_num"]+"</td>";
						tbody += "<td>"+this["p_name"]+"</td>";
						tbody += "<td>"+this["p_price"]+" 원<br>";
						tbody += "<font style='color:red'>(세일가:"+this["p_saleprice"]+" 원)</font></td>";
						
						// 수정시  변경된 amount,  id , p_num 이 필요하다.
						tbody += "<td><input type='number' name='amount' class='amount' value='"+this["amount"]	+"' id='"+this["id"]+"' p_num='"+this["p_num"]+"'</td>";
						tbody += "<td>"+parseInt(this["p_saleprice"]) * parseInt(this["amount"])+" 원</td>";
						
						// 삭제시 id 와 p_num 이 필요하다.
						tbody += "<td><input type='button' value='삭제' class='delcart' id='"+this["id"]+"' p_num='"+this["p_num"]+"'></td>";
						tbody += "</tr>";
						total = total + (parseInt(this["p_saleprice"]) * parseInt(this["amount"]));
					});
					tbody += "<tr style='text-align: right;'>";
					tbody += "<td colspan='7' style='padding-right: 50px'>";
					tbody += "<h2> 총 결재액 : "  + total + " 원 </h2>";
					tbody += "</tr>";
					$("#tbody").append(tbody);
				},
				error : function() { alert("읽기실패");	}
			});
		}
		// 삭제
		$("table").on("click", ".delcart", function() {
			$.ajax({
				url : "deleteCart.do",
				method : "post",
				data : "id="+$(this).attr("id")+"&p_num="+$(this).attr("p_num"),
				dataType : "text",
				success : function(data) {
					if(data == '1'){
					   cartList();
					}
				},
				error: function() {
					alert("읽기실패");
				}
			});
		});
		
		// 수정
		$("table").on("change", ".amount", function() {
			$.ajax({
				url : "editCart.do",
				method : "post",
				data : "id="+$(this).attr("id")+"&p_num="+$(this).attr("p_num")
				        +"&amount="+$(this).val(),
				dataType : "text",
				success : function(data) {
					if(data == '1'){
					   cartList();
					}
				},
				error: function() {
					alert("읽기실패");
				}
			});
		});
		cartList();
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
				<th style="width: 5%">체크</th>
				<th style="width: 10%">제품번호</th>
				<th style="width: 20%">제품명</th>
				<th style="width: 20%">단가</th>
				<th style="width: 15%">수량</th>
				<th style="width: 15%">금액</th>
				<th style="width: 15%">삭제</th>
			</tr>
		</thead>
		<tbody id="tbody"></tbody>
	</table>
</body>
</html>


