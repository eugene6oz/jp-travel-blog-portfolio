<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="../header.jsp"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>로그인 • japan_travel_diary</title>
</head>

<body class="auth-body">
	<main class="auth-wrap">
		<section class="auth-card">
			<div class="auth-brand">
				<div class="auth-logo">JPTRIPGRAM</div>
				<p class="auth-sub">旅の記録を共有してみましょう。</p>
			</div>

			<!-- UI만: 나중에 action을 loginPro.do 같은 걸로 바꾸면 됨 -->
			<form class="auth-form" method="post"
				action="<%=request.getContextPath()%>/jpmem/loginpro.do">
				<div class="auth-field">
					<label class="auth-label" for="userid">ユーザーID</label> <input
						class="auth-input" id="userid" name="userid" type="text"
						placeholder="ユーザーID" autocomplete="username" required />
				</div>




				<div class="auth-field">
					<label class="auth-label" for="password">パスワード</label> <input
						class="auth-input" id="password" name="password" type="password"
						placeholder="パスワード" autocomplete="current-password" required>
				</div>

				<c:if test="${not empty msg}">
					<div class="auth-alert">${msg}</div>
				</c:if>

				<button class="auth-btn" type="submit">ログイン</button>


				<div class="auth-divider">
					<span>または</span>
				</div>
			</form>
		</section>

		<section class="auth-card auth-card--small">
			<div class="auth-bottom">
				アカウントをお持ちでない方は <a class="auth-link auth-link--strong"
					href="<%=request.getContextPath()%>/jpmem/join.do">新規登録</a>
			</div>
		</section>

	</main>
	<%@ include file="../footer.jsp"%>

	<c:if test="${param.withdraw eq 'success'}">
	  <script>
	    alert("退会が完了しました。\nご利用ありがとうございました。");
	    history.replaceState({}, "", location.pathname);
	  </script>
	</c:if>
	
	<c:if test="${param.success eq '1'}">
	  <script>
	    alert("会員登録が完了しました。ログインしてください。");
	    history.replaceState({}, "", location.pathname);
	  </script>
	</c:if>
	
	<script>
	  const yEl = document.getElementById("y");
	  if (yEl) yEl.textContent = new Date().getFullYear();
	</script>
</body>
</html>
