<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../header.jsp" %>

<!-- Leaflet (지도) -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<main class="write-page">
  <div class="write-wrap">

    <!-- 상단 바 -->
    <div class="write-topbar">
      <div class="write-title">
        <span class="badge-dot"></span>
        <strong>投稿の編集</strong>
      </div>

      <div class="write-actions">
        <a class="btn-ghost"
           href="<%=request.getContextPath()%>/jpblog/view.do?bno=${viewdto.bno}">
          キャンセル
        </a>

        <!-- ✅ form 밖에서도 100% submit -->
        <button class="btn-primary" type="button"
                onclick="document.getElementById('updateForm').submit();">
          編集完了
        </button>
      </div>
    </div>

    <!-- 카드 -->
    <div class="write-card">

      <!-- 왼쪽: 이미지 업로드/미리보기 -->
      <section class="media-pane">
        <div class="media-box">

          <!-- ✅ 기존 이미지가 있으면 먼저 보여주기 -->
          <c:choose>
            <c:when test="${not empty viewdto.imgfile}">
              <div class="media-preview" id="mediaPreview" style="display:flex;" aria-hidden="false">
                <img id="previewImg"
                     src="<%=request.getContextPath()%>/jpblog/image?name=${viewdto.imgfile}"
                     alt="プレビュー" />
                <button type="button" class="btn-mini" id="changeBtn">写真を変更</button>
              </div>

              <div class="media-empty" id="mediaEmpty" style="display:none;">
                <div class="media-icon">📷</div>
                <div class="media-text">
                  写真をアップロードしてください<br/>
                  <small>jpg / png 推奨</small>
                </div>
                <label class="btn-outline" for="images">ファイル選択</label>
              </div>
            </c:when>

            <c:otherwise>
              <div class="media-empty" id="mediaEmpty">
                <div class="media-icon">📷</div>
                <div class="media-text">
                  写真をアップロードしてください<br/>
                  <small>jpg / png 推奨</small>
                </div>
                <label class="btn-outline" for="images">ファイル選択</label>
              </div>

              <div class="media-preview" id="mediaPreview" style="display:none;" aria-hidden="true">
                <img id="previewImg" alt="プレビュー" />
                <button type="button" class="btn-mini" id="changeBtn">写真を変更</button>
              </div>
            </c:otherwise>
          </c:choose>

        </div>

        <div class="media-hint">
          <span>Tip:</span> 写真は任意です。新しく選択すると既存の写真が置き換えられます。
        </div>
      </section>

      <!-- 오른쪽: 폼 -->
      <section class="form-pane">

        <form id="updateForm"
              action="<%=request.getContextPath()%>/jpblog/updatepro.do"
              method="post"
              enctype="multipart/form-data">

          <input type="hidden" name="bno" value="${viewdto.bno}" />

          <div class="row">
            <label class="lbl">作成者</label>
            <input class="inp" name="userid" type="text"
                   value="${viewdto.userid}" readonly />
          </div>

          <div class="row">
            <label class="lbl">タイトル</label>
            <input class="inp" name="title" type="text"
                   value="${viewdto.title}" required />
          </div>

          <!-- ✅ (권장) 타입/카테고리 기존 프로젝트 방식 유지하려면 여기 구조를 writer.jsp랑 맞추는게 베스트
               지금은 기존 너 코드(탭) 유지. 단, 서버에서 category 파라미터로 처리하는 구조라면 OK
          -->

          <!-- ✅ 위치 (지도 모달로 선택) -->
          <div class="row">
            <label class="lbl">場所</label>
            <input type="text" id="placeInput" class="inp"
                   placeholder="場所(例: 大濠公園 / 渋谷スクランブル交差点)">
            <button type="button" id="btnOpenPicker" class="btn-small">地図で選ぶ</button>

            <small id="placeStatus" style="color:#777; margin-left:8px;">
              <c:if test="${not empty viewdto.lat && not empty viewdto.lng}">
                OK (${viewdto.lat}, ${viewdto.lng})
              </c:if>
            </small>
          </div>

          <!-- ✅ lat/lng 유지 + 수정 가능 -->
          <input type="hidden" name="lat" id="lat" value="<c:out value='${viewdto.lat}'/>">
          <input type="hidden" name="lng" id="lng" value="<c:out value='${viewdto.lng}'/>">

          <div class="row">
            <label class="lbl">本文</label>
            <textarea class="ta" name="content" rows="8" required><c:out value="${viewdto.content}"/></textarea>

            <div class="counter">
              <span id="count">0</span>/2000
            </div>
          </div>

          <div class="row">
            <label class="lbl">タグ</label>
            <input class="inp" name="tags" type="text"
                   placeholder="例）#東京 #ラーメン #桜（スペース・カンマ可）" />
          </div>

          <div class="row">
            <label class="lbl">メイン画像</label>
            <input class="file" id="images" name="imgfile" type="file" accept="image/*" />
            <div class="help">
              * ファイルを選択すると、左側のプレビューが新しい画像に更新されます。
            </div>
          </div>

          <div class="mobile-actions">
            <button class="btn-primary w100" type="submit">編集完了</button>
          </div>

        </form>

      </section>
    </div>
  </div>

  <!-- ✅ 지도 선택 모달 -->
  <div id="pickModal" class="modal-overlay" aria-hidden="true">
    <div class="modal-card">
      <div class="modal-header">
        <div class="modal-title">地図で場所を選ぶ</div>
        <button type="button" class="modal-close" id="pickCloseBtn">✕</button>
      </div>

      <div class="modal-sub" style="display:flex; gap:8px; align-items:center;">
        <input id="pickQuery" class="inp" type="text" placeholder="地名を検索 (例: 渋谷スクランブル交差点)">
        <button type="button" id="pickSearchBtn" class="btn-small">検索</button>
        <button type="button" id="pickUseBtn" class="btn-small">この位置を使う</button>
      </div>

      <div id="pickMap" class="map-box"></div>

      <div style="padding-top:10px; font-size:12px; color:#777;">
        ※ 지도에서 클릭하면 핀이 이동합니다.
        <span id="pickCoordText"></span>
      </div>
    </div>
  </div>

</main>

<%@ include file="../footer.jsp" %>

<!-- ✅ 모달/지도 CSS (writer.css에 이미 있으면 여기 삭제해도 됨) -->
<style>
  .modal-overlay{ display:none; position:fixed; inset:0; background:rgba(0,0,0,.45); align-items:center; justify-content:center; z-index:9999; }
  .modal-overlay.is-open{ display:flex; }
  .modal-card{ width:min(900px, 92vw); background:#fff; border-radius:16px; padding:14px; box-shadow:0 10px 30px rgba(0,0,0,.2); }
  .modal-header{ display:flex; align-items:center; justify-content:space-between; padding-bottom:10px; border-bottom:1px solid #eee; }
  .modal-title{ font-weight:700; }
  .modal-close{ border:none; background:transparent; cursor:pointer; font-size:18px; }
  .map-box{ width:100%; height:520px; border-radius:12px; overflow:hidden; margin-top:10px; }
  .btn-small{ padding:10px 12px; border:1px solid #eee; background:#fff; border-radius:12px; cursor:pointer; }
</style>

<script>
  // 글자수 카운트 (초기값 포함)
  const ta = document.querySelector('textarea[name="content"]');
  const countEl = document.getElementById('count');
  if (ta && countEl) {
    countEl.textContent = ta.value.length;

    ta.addEventListener('input', () => {
      const len = ta.value.length;
      countEl.textContent = len;
      if (len > 2000) ta.value = ta.value.slice(0, 2000);
    });
  }

  // 이미지 미리보기
  const fileInput = document.getElementById('images');
  const mediaEmpty = document.getElementById('mediaEmpty');
  const mediaPreview = document.getElementById('mediaPreview');
  const previewImg = document.getElementById('previewImg');
  const changeBtn = document.getElementById('changeBtn');

  function showPreview(file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      previewImg.src = e.target.result;
      if (mediaEmpty) mediaEmpty.style.display = 'none';
      if (mediaPreview) {
        mediaPreview.style.display = 'flex';
        mediaPreview.setAttribute('aria-hidden', 'false');
      }
    };
    reader.readAsDataURL(file);
  }

  if (fileInput) {
    fileInput.addEventListener('change', () => {
      const file = fileInput.files && fileInput.files[0];
      if (!file) return;
      if (!file.type.startsWith('image/')) {
        alert('画像ファイルのみアップロードできます。');
        fileInput.value = '';
        return;
      }
      showPreview(file);
    });
  }

  if (changeBtn) {
    changeBtn.addEventListener('click', () => fileInput && fileInput.click());
  }
</script>

<!-- ✅ 지도 모달 + 핀 선택 -->
<script>
  const modal = document.getElementById("pickModal");
  const openBtn = document.getElementById("btnOpenPicker");
  const closeBtn = document.getElementById("pickCloseBtn");

  const pickQuery = document.getElementById("pickQuery");
  const pickSearchBtn = document.getElementById("pickSearchBtn");
  const pickUseBtn = document.getElementById("pickUseBtn");

  const placeInput = document.getElementById("placeInput");
  const placeStatus = document.getElementById("placeStatus");

  const latEl = document.getElementById("lat");
  const lngEl = document.getElementById("lng");
  const coordText = document.getElementById("pickCoordText");

  let map = null;
  let marker = null;
  let picked = null;

  function initMap(){
    const fallback = [35.681236, 139.767125]; // 도쿄역
    map = L.map("pickMap").setView(fallback, 12);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);

    marker = L.marker(fallback, { draggable:true }).addTo(map);

    map.on("click", (e) => setPicked([e.latlng.lat, e.latlng.lng], true));

    marker.on("dragend", () => {
      const p = marker.getLatLng();
      setPicked([p.lat, p.lng], false);
    });

    setPicked(fallback, false);
  }

  function setPicked(pos, move){
    picked = pos;
    if (marker) marker.setLatLng(pos);
    if (move && map) map.setView(pos, 15);
    if (coordText) coordText.textContent = " lat=" + pos[0].toFixed(6) + ", lng=" + pos[1].toFixed(6);
  }

  async function searchAndMove(q){
    if(!q) return;
    try{
      const url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&q=" + encodeURIComponent(q);
      const res = await fetch(url, { headers: { "Accept": "application/json" }});
      const data = await res.json();
      if(!data || data.length === 0){
        alert("見つかりませんでした");
        return;
      }
      setPicked([parseFloat(data[0].lat), parseFloat(data[0].lon)], true);
    }catch(e){
      console.log(e);
      alert("検索エラー");
    }
  }

  function openModal(){
    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.style.overflow = "hidden";

    if (pickQuery) pickQuery.value = (placeInput.value || "").trim();

    if (!map) initMap();

    setTimeout(() => {
      map.invalidateSize();

      // ✅ 기존 좌표가 있으면 그 좌표 우선
      const hasLat = latEl.value && !isNaN(parseFloat(latEl.value));
      const hasLng = lngEl.value && !isNaN(parseFloat(lngEl.value));

      if (hasLat && hasLng) {
        setPicked([parseFloat(latEl.value), parseFloat(lngEl.value)], true);
      } else if (pickQuery.value.trim()) {
        searchAndMove(pickQuery.value.trim());
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

  modal && modal.addEventListener("click", (e) => { if (e.target === modal) closeModal(); });
  window.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal.classList.contains("is-open")) closeModal();
  });

  pickSearchBtn && pickSearchBtn.addEventListener("click", () => searchAndMove(pickQuery.value.trim()));
  pickQuery && pickQuery.addEventListener("keydown", (e) => {
    if (e.key === "Enter") { e.preventDefault(); searchAndMove(pickQuery.value.trim()); }
  });

  pickUseBtn && pickUseBtn.addEventListener("click", () => {
    if (!picked) return;

    latEl.value = picked[0];
    lngEl.value = picked[1];

    if (placeStatus) {
      placeStatus.textContent = "OK (" + picked[0].toFixed(6) + ", " + picked[1].toFixed(6) + ")";
    }
    closeModal();
  });
</script>

</body>
</html>

