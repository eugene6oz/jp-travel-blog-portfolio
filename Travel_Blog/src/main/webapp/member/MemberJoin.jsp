<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>新規登録 | japan_travel_diary</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/basic.css">
</head>

<body>
  <main class="auth-page">
    <section class="auth-card">

      <!-- 로고 영역 -->
      <div class="auth-brand">
        <h1 class="auth-logo">JPTRIPGRAM</h1>
        <p class="auth-subtitle">日本旅行の記録を共有してみましょう。</p>
      </div>

      <!-- 회원가입 폼 -->
      <form class="auth-form" action="<%=request.getContextPath()%>/jpmem/joinpro.do" method="post">
        <!-- 서버에서 에러 메시지 내려주면 여기 출력 -->
        <%-- 
        <div class="auth-alert">
          すでに使用されているユーザーIDです。
        </div>
        --%>

        <div class="auth-field">
          <label class="auth-label" for="userid">ユーザーID</label>
          <input class="auth-input" type="text" id="userid" name="userid" placeholder="ユーザーID" required />
        </div>

        <div class="auth-field">
          <label class="auth-label" for="password">パスワード</label>
          <input class="auth-input" type="password" id="password" name="password" placeholder="パスワード" required />
        </div>

        <div class="auth-field">
          <label class="auth-label" for="password2">パスワード（確認）</label>
          <input class="auth-input" type="password" id="password2" name="password2" placeholder="パスワード（確認）" required />
        </div>

        <div class="auth-field">
          <label class="auth-label" for="writer">名前（ニックネーム）</label>
          <input class="auth-input" type="text" id="writer" name="writer" placeholder="名前（ニックネーム）" required />
        </div>

        <div class="auth-field">
          <label class="auth-label" for="email">メールアドレス</label>
          <input class="auth-input" type="email" id="email" name="email" placeholder="メールアドレス" required />
        </div>

        <div class="auth-field">
          <label class="auth-label" for="phone">電話番号</label>
          <input class="auth-input" type="text" id="phone" name="phone" placeholder="電話番号（任意）" />
        </div>

        <button class="auth-btn" type="submit">登録する</button>

        <p class="auth-policy">
          登録すると、<span class="auth-link-text">利用規約</span>および
          <span class="auth-link-text">プライバシーポリシー</span>に同意したものとみなされます。
        </p>
      </form>

    </section>

    <!-- 하단: 로그인 이동 -->
    <section class="auth-card auth-card--sub">
      <p class="auth-switch">
        すでにアカウントをお持ちですか？
        <a class="auth-link" href="<%=request.getContextPath()%>/jpmem/login.do">ログイン</a>
      </p>
    </section>

    <footer class="auth-footer">
      <p class="auth-footer-text">© japan_travel_diary</p>
    </footer>
  </main>
  <script>
  const err = "<%= request.getParameter("err") == null ? "" : request.getParameter("err") %>";

  if (err === "dup") {
    alert("すでに使用されているユーザーIDです。");
    document.getElementById("userid")?.focus();
  } else if (err === "pw") {
    alert("パスワードが一致しません。");
    document.getElementById("password")?.focus();
  } else if (err === "fail") {
    alert("登録に失敗しました。もう一度お試しください。");
  }
</script>
</body>
</html>


