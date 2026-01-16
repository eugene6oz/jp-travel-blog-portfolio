<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>

<!-- Main Content -->
<main class="main-content">

  <!-- Profile Header -->
  <section class="profile-header">
    <!-- Desktop Layout -->
    <div class="profile-desktop">

      <!-- 왼쪽 영역 -->
      <div class="profile-left">
		  <div class="profile-image-wrapper">
		
		    <c:choose>
		      <c:when test="${not empty sessionScope.profileImg}">
		        <img src="<%=request.getContextPath()%>/image/profile/${sessionScope.profileImg}"
		             alt="프로필" class="profile-image">
		      </c:when>
		      <c:otherwise>
		        <img src="<%=request.getContextPath()%>/image/LOGO.png"
		             alt="기본 프로필" class="profile-image">
		      </c:otherwise>
		    </c:choose>
		
		  </div>

        <div class="profile-info">
          <div class="profile-top">
            <h2 class="username">JPTRIPGRAM</h2>
          </div>

          <div class="profile-bio">
            <div class="bio-name">日本旅行ダイアリー</div>
            <div class="bio-text">
             🇯🇵 日本旅行を記録し、共有する旅行アーカイブ<br>
   			 📍 都市·グルメ·お祭り中心の実際の旅行情報<br>
   			 ✈️ 直接行ってきた経験をもとにまとめました
            </div>
          </div>
        </div>
      </div>

      <!-- 오른쪽 영역 -->
      <div class="profile-right">
        <div class="side-box">
          <h4 class="side-title">🔥 人気記事</h4>
          <ul class="side-list">
            <c:if test="${empty popularList}">
              <li><span style="color:#777;">まだ人気記事がありません。</span></li>
            </c:if>

            <c:forEach var="p" items="${popularList}">
              <li>
                <a href="<%=request.getContextPath()%>/jpblog/view.do?bno=${p.bno}">
                  ${p.title}
                </a>
              </li>
            </c:forEach>
          </ul>
        </div>

        <div class="side-box">
          <h4 class="side-title">⭐ おすすめ</h4>
          <ul class="side-list">
            <c:if test="${empty recommendList}">
              <li><span style="color:#777;">おすすめ記事がありません。</span></li>
            </c:if>

            <c:forEach var="r" items="${recommendList}">
              <li>
                <a href="<%=request.getContextPath()%>/jpblog/view.do?bno=${r.bno}">
                  ${r.title}
                </a>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>

    </div>

    <!-- Tabs -->
    <ul class="nav nav-tabs custom-tabs" id="storyTabs" role="tablist">
      <li class="nav-item">
        <a class="nav-link active" data-toggle="tab" href="#city" role="tab">都市</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#festival" role="tab">祭り</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" data-toggle="tab" href="#food" role="tab">グルメ</a>
      </li>

      <li class="nav-item ml-auto">
        <c:if test="${empty sessionScope.userid}">
          <a href="<%=request.getContextPath()%>/jpmem/login.do" class="btn btn-ghost">
             ログインして投稿する
          </a>
        </c:if>

        <c:if test="${not empty sessionScope.userid}">
          <a class="nav-link" style="border:0;" href="<%=request.getContextPath()%>/jpblog/write.do">
            ＋ 投稿する
          </a>
        </c:if>
      </li>
    </ul>

    <div class="tab-content mt-3">

      <!-- 도시 -->
      <div class="tab-pane fade show active" id="city" role="tabpanel">
        <div class="stories-container">

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="https://images.unsplash.com/photo-1526481280693-3bfa7568e0f3"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">すべて</span>
            </div>
          </a>

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?city=도쿄">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="https://images.unsplash.com/photo-1640871426525-a19540c45a39"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">東京</span>
            </div>
          </a>

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?city=교토">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="https://images.unsplash.com/photo-1729864881494-d96345092845"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">京都</span>
            </div>
          </a>

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?city=오사카">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="https://images.unsplash.com/photo-1655747708896-c6c71b7c5585"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">大阪</span>
            </div>
          </a>

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?city=후쿠오카">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/Fukuoka.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">福岡</span>
            </div>
          </a>

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?city=삿포로">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/sapporo.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">札幌</span>
            </div>
          </a>

          <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?city=소도시">
            <div class="story">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/smallcity.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">地方都市</span>
            </div>
          </a>

        </div>
      </div>

      <!-- 축제 -->
      <div class="tab-pane fade" id="festival" role="tabpanel">
        <div class="stories-container">

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?festival=벚꽃축제">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="https://images.unsplash.com/photo-1557409518-691ebcd96038"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">桜祭り</span>
            </a>
          </div>

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?festival=불꽃축제">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/firefestival.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">花火大会</span>
            </a>
          </div>

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?festival=지역축제">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/smallfestival.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">地域祭り</span>
            </a>
          </div>
          
          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?festival=겨울축제">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/winter.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">冬祭り</span>
            </a>
          </div>

        </div>
      </div>

      <!-- 맛집 -->
      <div class="tab-pane fade" id="food" role="tabpanel">
        <div class="stories-container">

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?food=라멘">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/ramen.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">ラーメン</span>
            </a>
          </div>

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?food=카페">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/cafe.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">カフェ</span>
            </a>
          </div>

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?food=야끼니쿠">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="<%=request.getContextPath()%>/image/niku.jpg"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">焼肉</span>
            </a>
          </div>

          <div class="story">
            <a class="story-link" href="<%=request.getContextPath()%>/jpblog/list.do?food=스시">
              <div class="story-ring">
                <div class="story-image-wrapper">
                  <img src="https://images.unsplash.com/photo-1700324822763-956100f79b0d"
                       class="story-image">
                </div>
              </div>
              <span class="story-label">寿司</span>
            </a>
          </div>

        </div>
      </div>

    </div>
  </section> <!-- ✅ profile-header 끝 -->

  <!-- ✅ Posts Header -->
  <div class="posts-header">
    全 <strong>${totalCount}</strong>件の投稿
  </div>

  <!-- Posts Grid -->
  <section class="posts-grid">
    <c:if test="${empty list}">
      <div style="padding:20px; text-align:center; color:#777;">
        まだ投稿がありません。
      </div>
    </c:if>

    <c:forEach var="item" items="${list}">
      <a href="<%=request.getContextPath()%>/jpblog/view.do?bno=${item.bno}"
         class="post-link">

        <div class="post-item">
          <!-- ✅ DB 이미지 출력 (없으면 기본 이미지) -->
          <c:choose>
            <c:when test="${not empty item.imgfile}">
              <img src="<%=request.getContextPath()%>/jpblog/image?name=${item.imgfile}"
                   alt="${item.title}" class="post-image">
            </c:when>
            <c:otherwise>
              <img src="https://images.unsplash.com/photo-1526481280693-3bfa7568e0f3"
                   alt="기본이미지" class="post-image">
            </c:otherwise>
          </c:choose>

          <div class="post-overlay">
           <div class="overlay-stat">
              <span>${item.title}</span>
            </div>
            <div class="overlay-stat">
              <span class="icon">閲覧数</span>
              <span>${item.views}</span>
              
            </div>
            <div class="overlay-stat">
              <span class="icon">💬</span>
              <span>${item.commentCount}</span>
            </div>
          </div>
        </div>

      </a>
    </c:forEach>
  </section>

  <!-- ✅ Pagination -->
  <c:if test="${totalPage > 1}">
    <div style="display:flex; justify-content:center; gap:6px; margin:18px 0; flex-wrap:wrap;">

      <!-- 이전 블록 -->
      <c:if test="${startPage > 1}">
        <a href="<%=request.getContextPath()%>/jpblog/list.do?page=${startPage-1}
          <c:if test='${not empty city}'>&city=${city}</c:if>
          <c:if test='${not empty festival}'>&festival=${festival}</c:if>
          <c:if test='${not empty food}'>&food=${food}</c:if>
          <c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
          style="padding:8px 12px; border:1px solid #ddd; border-radius:10px; text-decoration:none;">
          «
        </a>
      </c:if>

      <!-- 페이지 번호 -->
      <c:forEach var="p" begin="${startPage}" end="${endPage}">
        <a href="<%=request.getContextPath()%>/jpblog/list.do?page=${p}
          <c:if test='${not empty city}'>&city=${city}</c:if>
          <c:if test='${not empty festival}'>&festival=${festival}</c:if>
          <c:if test='${not empty food}'>&food=${food}</c:if>
          <c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
          style="padding:8px 12px; border:1px solid #ddd; border-radius:10px; text-decoration:none;
                 <c:if test='${p == page}'>font-weight:700; background:#111; color:#fff;</c:if>">
          ${p}
        </a>
      </c:forEach>

      <!-- 다음 블록 -->
      <c:if test="${endPage < totalPage}">
        <a href="<%=request.getContextPath()%>/jpblog/list.do?page=${endPage+1}
          <c:if test='${not empty city}'>&city=${city}</c:if>
          <c:if test='${not empty festival}'>&festival=${festival}</c:if>
          <c:if test='${not empty food}'>&food=${food}</c:if>
          <c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
          style="padding:8px 12px; border:1px solid #ddd; border-radius:10px; text-decoration:none;">
          »
        </a>
      </c:if>

    </div>
  </c:if>

</main>

<%@ include file="footer.jsp" %>
