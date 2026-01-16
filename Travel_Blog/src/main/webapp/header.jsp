<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="icon" href="<%=request.getContextPath()%>/image/favicon.png">


<title>JPTRIPGRAM</title>

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/basic.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/login.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/view.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/writer.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/theme.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/MyPage.css">

</head>

<body>

	<header class="top-nav">
		<div class="container">
			<h1 class="logo">
				<a href="<%=request.getContextPath()%>/jpblog/list.do"
					style="display: flex; align-items: center; text-decoration: none;">

					<img
					src="<%=request.getContextPath()%>/image/LOGOPONT.png"
					alt="JPTRIPGRAM" style="height: 80px; width: auto;">

				</a>
			</h1>

			<div class="header-right">

				<!-- ✅ 공용 검색창 (choose 밖에 둬야 함) -->
				<form class="header-search" method="get"
					action="<%=request.getContextPath()%>/jpblog/list.do">

					<!-- 필터 유지(스토리/탭에서 list로 들어온 경우에만 유지됨) -->
					<c:if test="${not empty city}">
						<input type="hidden" name="city" value="${city}">
					</c:if>
					<c:if test="${not empty festival}">
						<input type="hidden" name="festival" value="${festival}">
					</c:if>
					<c:if test="${not empty food}">
						<input type="hidden" name="food" value="${food}">
					</c:if>

					<input type="text" name="keyword" value="${keyword}"
						placeholder="タイトルサーチ"
						style="flex: 1; padding: 8px 12px; border: 1px solid #ddd; border-radius: 20px;">

					<button type="submit"
						style="padding: 8px 14px; border: none; border-radius: 20px; background: #fff; color: #000;">
						🔍</button>
				</form>

				<!-- ✅ 로그인 상태에 따라 버튼만 분기 -->
				<c:choose>
					<c:when test="${not empty sessionScope.userid}">
						<span class="header-user"> ${sessionScope.userid} 님 </span>

						<a href="<%=request.getContextPath()%>/jpmem/mypage.do"
							class="btn btn-ghost"> MYPAGE </a>

						<a href="<%=request.getContextPath()%>/jpmem/logout.do"
							class="btn btn-ghost"> LOGOUT </a>
					</c:when>

					<c:otherwise>
						<a href="<%=request.getContextPath()%>/jpmem/login.do"
							class="btn btn-ghost"> LOGIN </a>

						<a href="<%=request.getContextPath()%>/jpmem/join.do"
							class="btn btn-primary"> JOIN </a>
					</c:otherwise>
				</c:choose>

			</div>
		</div>
	</header>