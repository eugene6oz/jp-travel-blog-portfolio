<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="../header.jsp"%>

<main class="mp-wrap">

	<!-- 프로필 헤더 -->
	<section class="mp-profile">

		<div class="mp-avatar">
			<c:choose>
				<c:when test="${not empty sessionScope.profileImg}">
					<img
						src="<%=request.getContextPath()%>/image/profile/${sessionScope.profileImg}"
						alt="プロフィール"
						style="width: 80px; height: 80px; border-radius: 50%; object-fit: cover;">
				</c:when>
				<c:otherwise>
					<span class="mp-initial">
						<c:choose>
							<c:when test="${not empty sessionScope.userid}">
								${fn:substring(sessionScope.userid,0,1)}
							</c:when>
							<c:otherwise>U</c:otherwise>
						</c:choose>
					</span>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="mp-meta">
			<div class="mp-topline">
				<div class="mp-userid">@${sessionScope.userid}</div>

				<div class="mp-actions">
					<a class="mp-btn ghost"
						href="<%=request.getContextPath()%>/jpmem/edit.do">プロフィール編集</a>
					<button class="mp-btn" type="button" onclick="openWithdraw()">退会</button>
				</div>
			</div>

			<!-- 통계 -->
			<div class="mp-stats">
				<div class="stat">
					<div class="num">${postCount}</div>
					<div class="label">投稿</div>
				</div>
				<div class="stat">
					<div class="num">${commentCount}</div>
					<div class="label">コメント</div>
				</div>
			</div>

			<div class="mp-bio">
				<div class="name">
					<c:choose>
						<c:when test="${not empty sessionScope.writer}">${sessionScope.writer}</c:when>
						<c:otherwise>私の旅の記録</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</section>

	<!-- 탭 -->
	<section class="mp-tabs">
		<button class="tab active" type="button" data-tab="posts">投稿</button>
		<button class="tab" type="button" data-tab="comments">コメント</button>
		<button class="tab" type="button" data-tab="likes">いいね</button>
	</section>

	<!-- 내 글 -->
	<section class="mp-panel show" id="tab-posts">
		<div class="mp-grid">

			<c:forEach var="p" items="${myPosts}">
				<div class="grid-item">

					<a class="grid-link"
						href="<%=request.getContextPath()%>/jpblog/view.do?bno=${p.bno}">
						<c:choose>
							<c:when test="${not empty p.imgfile}">
								<img
									src="<%=request.getContextPath()%>/jpblog/image?name=${p.imgfile}"
									alt="${p.title}">
							</c:when>
							<c:otherwise>
								<div class="grid-empty">
									<span>NO IMAGE</span>
								</div>
							</c:otherwise>
						</c:choose>

						<div class="grid-overlay">
							<div class="ov-title">${p.title}</div>
							<div class="ov-meta">
								<span>${p.type}</span>
								<c:if test="${not empty p.city}">
									<span>· ${p.city}</span>
								</c:if>
							</div>
						</div>
					</a>

					<div class="grid-actions">
						<a class="btn-mini edit"
							href="<%=request.getContextPath()%>/jpblog/update.do?bno=${p.bno}">
							編集
						</a>

						<form action="<%=request.getContextPath()%>/jpblog/delete.do"
							method="post" onsubmit="return confirm('本当に削除しますか？');">
							<input type="hidden" name="bno" value="${p.bno}">
							<button type="submit" class="btn-mini danger">削除</button>
						</form>
					</div>

				</div>
			</c:forEach>

			<c:if test="${empty myPosts}">
				<div class="mp-empty">
					まだ投稿がありません。<br />
					<a class="mp-link"
						href="<%=request.getContextPath()%>/jpblog/write.do">最初の投稿を作成</a>
				</div>
			</c:if>

		</div>
	</section>

	<!-- 내 댓글 -->
	<section class="mp-panel" id="tab-comments">
		<div class="mp-list">

			<c:if test="${empty myComments}">
				<div class="mp-empty">まだコメントがありません。</div>
			</c:if>

			<c:forEach var="cmt" items="${myComments}">
				<a class="list-row"
					href="<%=request.getContextPath()%>/jpblog/view.do?bno=${cmt.bno}">
					<div class="row-top">
						<div class="row-title">${cmt.blogTitle}</div>
						<div class="row-date">${cmt.regdate}</div>
					</div>
					<div class="row-body">${cmt.content}</div>
				</a>
			</c:forEach>

		</div>
	</section>

	<!-- 좋아요 -->
	<section class="mp-panel" id="tab-likes">
		<div class="mp-grid">

			<c:forEach var="l" items="${myLikes}">
				<a class="grid-item"
					href="<%=request.getContextPath()%>/jpblog/view.do?bno=${l.bno}">
					<c:choose>
						<c:when test="${not empty l.imgfile}">
							<img
								src="<%=request.getContextPath()%>/jpblog/image?name=${l.imgfile}"
								alt="${l.title}">
						</c:when>
						<c:otherwise>
							<div class="grid-empty">
								<span>NO IMAGE</span>
							</div>
						</c:otherwise>
					</c:choose>

					<div class="grid-overlay">
						<div class="ov-title">${l.title}</div>
					</div>
				</a>
			</c:forEach>

			<c:if test="${empty myLikes}">
				<div class="mp-empty">いいねした投稿はまだありません。</div>
			</c:if>

		</div>
	</section>

</main>

<!-- 회원탈퇴 모달 -->
<div class="mp-modal" id="withdrawModal" aria-hidden="true">
	<div class="mp-modal-backdrop" onclick="closeWithdraw()"></div>
	<div class="mp-modal-card">
		<div class="mm-title">退会</div>
		<div class="mm-desc">
			本当に退会しますか？<br />退会後は復元できない場合があります。
		</div>

		<form method="post"
			action="<%=request.getContextPath()%>/jpmem/withdraw.do"
			class="mm-form">
			<label class="mm-label">パスワード確認</label>
			<input class="mm-input"
				type="password" name="password" placeholder="パスワードを入力" required />
			<div class="mm-actions">
				<button class="mp-btn ghost" type="button" onclick="closeWithdraw()">キャンセル</button>
				<button class="mp-btn danger" type="submit">退会する</button>
			</div>
		</form>
	</div>
</div>

<%@ include file="../footer.jsp"%>

<script>
  document.addEventListener('DOMContentLoaded', () => {
    const tabs = document.querySelectorAll('.mp-tabs .tab');
    tabs.forEach(btn => {
      btn.addEventListener('click', () => {
        tabs.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        const key = btn.dataset.tab;
        document.querySelectorAll('.mp-panel').forEach(p => p.classList.remove('show'));
        const target = document.getElementById('tab-' + key);
        if (target) target.classList.add('show');
      });
    });
  });

  function openWithdraw() {
    const modal = document.getElementById('withdrawModal');
    modal.style.display = 'block';
    modal.setAttribute('aria-hidden', 'false');
  }

  function closeWithdraw() {
    const modal = document.getElementById('withdrawModal');
    modal.style.display = 'none';
    modal.setAttribute('aria-hidden', 'true');
  }
</script>
