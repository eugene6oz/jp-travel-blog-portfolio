<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>投稿の詳細</title>

  <!-- Leaflet CSS -->
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
</head>

<body>

<main class="main-content">
  <div class="view-wrap">

    <!-- 상단 바 -->
    <div class="view-topbar">
      <a class="icon-btn" href="<%=request.getContextPath()%>/jpblog/list.do">←</a>
      <div class="view-topbar-title">投稿</div>
      <button class="icon-btn" type="button">⋯</button>
    </div>

    <!-- 게시글 없을 때 -->
    <c:if test="${empty viewdto}">
      <div style="padding:20px; text-align:center; color:#777;">
        投稿が見つかりません。
      </div>
    </c:if>

    <!-- 게시글 있을 때 -->
    <c:if test="${not empty viewdto}">
      <div class="view-card">

        <!-- 왼쪽 이미지 -->
        <section class="view-media">
          <c:choose>
            <c:when test="${not empty viewdto.imgfile}">
              <img
                src="<%=request.getContextPath()%>/jpblog/image?name=${viewdto.imgfile}"
                alt="${viewdto.title}"
                class="view-media-img"
              />
            </c:when>
            <c:otherwise>
              <img
                src="https://images.unsplash.com/photo-1526481280693-3bfa7568e0f3"
                alt="post"
                class="view-media-img"
              />
            </c:otherwise>
          </c:choose>
        </section>

        <!-- 오른쪽 내용 -->
        <section class="view-side">

          <!-- 헤더 -->
          <div class="view-header">
            <div class="author">
              <c:choose>
                <c:when test="${not empty writerProfileImg}">
                  <img class="author-avatar"
                       src="<%=request.getContextPath()%>/image/profile/${writerProfileImg}"
                       alt="writer avatar" />
                </c:when>
                <c:otherwise>
                  <img class="author-avatar"
                       src="<%=request.getContextPath()%>/image/LOGO.png"
                       alt="default avatar" />
                </c:otherwise>
              </c:choose>

              <div class="author-meta">
                <div class="author-line">
                  <span class="author-name">${viewdto.userid}</span>
                  <span class="dot">•</span>
                  <span class="author-sub">カテゴリ</span>
                </div>
                <div class="author-sub">${viewdto.regdate} · 閲覧 ${viewdto.views}</div>
              </div>
            </div>

            <!-- 본인 글일 때만 수정/삭제 -->
            <c:if test="${sessionScope.userid eq viewdto.userid}">
              <div class="view-actions">
                <a class="text-btn" href="<%=request.getContextPath()%>/jpblog/update.do?bno=${viewdto.bno}">編集</a>
                <a class="text-btn danger"
                   href="<%=request.getContextPath()%>/jpblog/delete.do?bno=${viewdto.bno}"
                   onclick="return confirm('本当に削除しますか？')">削除</a>
              </div>
            </c:if>
          </div>

          <!-- 본문 + 댓글 -->
          <div class="view-body">

            <!-- 글 내용 -->
            <div class="post-caption">
              <span class="author-name">${viewdto.userid}</span>
              <span class="caption-text">
                <strong>${viewdto.title}</strong><br/>
                <c:out value="${viewdto.content}" />
              </span>
            </div>

            <!-- 댓글 목록 -->
            <div class="comment-list" id="comments">

              <c:if test="${empty commentList}">
                <div style="color:#777; padding:10px 0;">まだコメントがありません。</div>
              </c:if>

              <c:forEach var="c" items="${commentList}">
                <div class="comment-item">
                  <div class="comment-line">
                    <span class="comment-user">${c.userid}</span>
                    <span class="comment-text"><c:out value="${c.content}" /></span>
                  </div>

                  <div class="comment-sub">
                    <span>${c.regdate}</span>

                    <c:if test="${sessionScope.userid eq c.userid}">
                      <a class="link-btn"
                         href="<%=request.getContextPath()%>/jpblog/commentDelete.do?cno=${c.cno}&bno=${viewdto.bno}&cp=${commentPage}"
                         onclick="return confirm('コメントを削除しますか？')">
                        削除
                      </a>
                    </c:if>
                  </div>
                </div>
              </c:forEach>

              <!-- 댓글 페이지네이션 -->
              <c:if test="${commentTotalPages > 1}">
                <div class="comment-paging" style="display:flex; gap:8px; padding:10px 0; flex-wrap:wrap;">
                  <c:forEach var="p" begin="1" end="${commentTotalPages}">
                    <c:choose>
                      <c:when test="${p == commentPage}">
                        <strong style="padding:4px 8px; border:1px solid #ddd; border-radius:8px;">${p}</strong>
                      </c:when>
                      <c:otherwise>
                        <a style="padding:4px 8px; border:1px solid #eee; border-radius:8px; text-decoration:none;"
                           href="<%=request.getContextPath()%>/jpblog/view.do?bno=${viewdto.bno}&cp=${p}#comments">
                          ${p}
                        </a>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </div>
              </c:if>

            </div>

            <!-- 댓글 작성 -->
            <c:choose>
              <c:when test="${empty sessionScope.userid}">
                <div class="comment-login-hint" style="padding:10px 0; color:#777;">
                  コメントするには
                  <a href="<%=request.getContextPath()%>/jpmem/login.do"
                     style="color:#1e88e5; font-weight:600;">ログイン</a>
                  してください。
                </div>
              </c:when>

              <c:otherwise>
                <form class="comment-form"
                      action="<%=request.getContextPath()%>/jpblog/commentWrite.do"
                      method="post">
                  <input type="hidden" name="bno" value="${viewdto.bno}" />
                  <input class="comment-input" type="text" name="content"
                         placeholder="コメントを追加..." maxlength="200" required />
                  <button class="comment-submit" type="submit">投稿</button>
                </form>
              </c:otherwise>
            </c:choose>

          </div><!-- /.view-body -->

          <!-- 하단 액션 -->
          <div class="view-footer">
            <div class="post-actions">
              <div class="left-actions">

                <c:choose>
                  <c:when test="${empty sessionScope.userid}">
                    <a class="icon-action"
                       href="<%=request.getContextPath()%>/jpmem/login.do"
                       style="text-decoration:none;">♡</a>
                  </c:when>

                  <c:otherwise>
                    <form method="post" action="<%=request.getContextPath()%>/jpblog/like.do" style="display:inline;">
                      <input type="hidden" name="bno" value="${viewdto.bno}" />
                      <c:choose>
                        <c:when test="${isLiked}">
                          <input type="hidden" name="mode" value="unlike" />
                          <button class="icon-action" type="submit" title="いいね解除">♥</button>
                        </c:when>
                        <c:otherwise>
                          <input type="hidden" name="mode" value="like" />
                          <button class="icon-action" type="submit" title="いいね">♡</button>
                        </c:otherwise>
                      </c:choose>
                    </form>
                  </c:otherwise>
                </c:choose>

                <button class="icon-action" type="button">💬</button>
                <button class="icon-action" type="button" id="openMapBtn" title="地図">🗺️</button>
              </div>

              <button class="icon-action" type="button">🔖</button>
            </div>

            <div class="post-stats">
              <strong>いいね</strong> <span>${likeCount}</span> 件
            </div>
          </div><!-- /.view-footer -->

        </section>
      </div>
    </c:if>

  </div>

  <!-- ✅ 지도 모달 -->
  <div id="mapModal" class="modal-overlay" aria-hidden="true">
    <div class="modal-card">
      <div class="modal-header">
        <div class="modal-title">地図</div>
        <button type="button" class="modal-close" id="closeMapBtn">✕</button>
      </div>
      <div class="modal-sub">
        <span id="mapKeywordText"></span>
      </div>
      <div id="mapBox" class="map-box"></div>
    </div>
  </div>

</main>

<!-- Leaflet JS -->
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<!-- ✅ JSP 값 → JS로 먼저 전달(맨 위에 있어야 안전) -->
<script>
  var POST_LAT = "<c:out value='${viewdto.lat}'/>";
  var POST_LNG = "<c:out value='${viewdto.lng}'/>";
  var MAP_KEYWORD = "<c:out value='${empty viewdto.city ? viewdto.category : viewdto.city}'/>";
</script>

<script>
  const modal = document.getElementById("mapModal");
  const openBtn = document.getElementById("openMapBtn");
  const closeBtn = document.getElementById("closeMapBtn");
  const keywordText = document.getElementById("mapKeywordText");

  let map = null;
  let marker = null;

  function initLeafletMap(){
    const fallback = [35.681236, 139.767125]; // 도쿄역
    map = L.map("mapBox").setView(fallback, 12);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);
    marker = L.marker(fallback).addTo(map);
  }

  function openModal(){
    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.style.overflow = "hidden";

    if (!map) initLeafletMap();

    // ✅ 모달 열리고 난 뒤 사이즈 재계산(흰 화면 방지)
    setTimeout(() => {
      map.invalidateSize();

      const hasCoord =
        POST_LAT && POST_LNG &&
        !isNaN(parseFloat(POST_LAT)) &&
        !isNaN(parseFloat(POST_LNG));

      if (hasCoord) {
        const pos = [parseFloat(POST_LAT), parseFloat(POST_LNG)];
        map.setView(pos, 14);
        marker.setLatLng(pos);
        keywordText.textContent = "保存された位置を表示中";
      } else {
        const kw = (MAP_KEYWORD || "").trim();
        keywordText.textContent = kw ? ("検索: " + kw) : "検索キーワードがありません。";
        if (kw) searchAndPin(kw);
      }
    }, 120);
  }

  function closeModal(){
    modal.classList.remove("is-open");
    modal.setAttribute("aria-hidden", "true");
    document.body.style.overflow = "";
  }

  openBtn && openBtn.addEventListener("click", openModal);
  closeBtn && closeBtn.addEventListener("click", closeModal);
  modal.addEventListener("click", (e) => { if (e.target === modal) closeModal(); });
  window.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal.classList.contains("is-open")) closeModal();
  });

  async function searchAndPin(q){
    try{
      const url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&q=" + encodeURIComponent(q);
      const res = await fetch(url, { headers: { "Accept": "application/json" }});
      const data = await res.json();
      if (!data || data.length === 0) return;

      const lat = parseFloat(data[0].lat);
      const lon = parseFloat(data[0].lon);

      const pos = [lat, lon];
      map.setView(pos, 14);
      marker.setLatLng(pos);

      if (data[0].display_name) {
        keywordText.textContent = q + " · " + data[0].display_name;
      }
    }catch(e){
      console.log(e);
    }
  }
</script>

<%@ include file="../footer.jsp" %>
</body>
</html>

