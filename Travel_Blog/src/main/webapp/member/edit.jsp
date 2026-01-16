<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../header.jsp"%>

<main class="mp-wrap">

  <section class="edit-card">
    <h2 class="edit-title">プロフィール編集</h2>

    <!-- ✅ 1) 프로필 사진 변경 폼 (단독 form) -->
    <form action="<%=request.getContextPath()%>/jpmem/profileImgUpdate.do"
          method="post" enctype="multipart/form-data"
          class="edit-form" style="margin-bottom:16px;">

      <div class="edit-row">
        <label class="edit-label">プロフィール画像</label>

        <div style="display:flex; align-items:center; gap:12px; flex-wrap:wrap;">
          <!-- 미리보기(없으면 기본) -->
          <c:choose>
            <c:when test="${not empty sessionScope.profileImg}">
              <img src="<%=request.getContextPath()%>/image/profile/${sessionScope.profileImg}"
                   style="width:64px;height:64px;border-radius:50%;object-fit:cover;">
            </c:when>
            <c:otherwise>
              <img src="<%=request.getContextPath()%>/image/LOGO.png"
                   style="width:64px;height:64px;border-radius:50%;object-fit:cover;">
            </c:otherwise>
          </c:choose>

          <input type="file" name="profileImg" accept="image/*" required>
          <button type="submit" class="mp-btn">写真を変更</button>
        </div>
      </div>
    </form>

    <!-- ✅ 2) 닉네임/비번 수정 폼 -->
    <form method="post"
          action="<%=request.getContextPath()%>/jpmem/editpro.do"
          class="edit-form">

      <!-- 아이디 -->
      <div class="edit-row">
        <label class="edit-label">ユーザーID</label>
        <input class="edit-input readonly" type="text"
               value="${sessionScope.userid}" readonly>
      </div>

      <!-- 닉네임 -->
      <div class="edit-row">
        <label class="edit-label">ニックネーム</label>
        <input class="edit-input" type="text" name="writer"
               value="${sessionScope.writer}" maxlength="20" required>
        <small class="edit-hint">マイページに表示される名前です</small>
      </div>

      <div class="edit-row">
        <label class="edit-label">現在のパスワード</label>
        <input class="edit-input" type="password" name="currentPassword" placeholder="現在のパスワード">
      </div>

      <div class="edit-row">
        <label class="edit-label">新しいパスワード</label>
        <input class="edit-input" type="password" name="newPassword" placeholder="新しいパスワード">
      </div>

      <div class="edit-row">
        <label class="edit-label">新しいパスワード（確認）</label>
        <input class="edit-input" type="password" name="newPasswordConfirm" placeholder="新しいパスワード（確認）">
      </div>

      <!-- 버튼 -->
      <div class="edit-actions">
        <button type="submit" class="mp-btn">保存</button>
        <a href="<%=request.getContextPath()%>/jpmem/mypage.do" class="mp-btn ghost">キャンセル</a>
      </div>

    </form>
  </section>

</main>

<%@ include file="../footer.jsp"%>

