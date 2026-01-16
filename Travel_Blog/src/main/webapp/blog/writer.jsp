<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>


<main class="write-page">
  <div class="write-wrap">

    <!-- 상단 바 -->
    <div class="write-topbar">
      <div class="write-title">
        <span class="badge-dot"></span>
        <strong>新規投稿</strong>
      </div>

      <div class="write-actions">
        <a class="btn-ghost" href="<%=request.getContextPath()%>/index.jsp">戻る</a>
        <button class="btn-primary" type="submit" form="writeForm">投稿</button>
      </div>
    </div>

    <!-- 카드 -->
    <div class="write-card">

      <!-- 왼쪽: 이미지 업로드/미리보기 -->
      <section class="media-pane">
        <div class="media-box">
          <div class="media-empty" id="mediaEmpty">
            <div class="media-icon">📷</div>
            <div class="media-text">
              写真をアップロードしてください<br/>
              <small>jpg / png 推奨</small>
            </div>
            <label class="btn-outline" for="images">ファイルを選択</label>
          </div>

          <div class="media-preview" id="mediaPreview" aria-hidden="true">
            <img id="previewImg" alt="미리보기" />
            <button type="button" class="btn-mini" id="changeBtn">写真を変更</button>
          </div>
        </div>

        <div class="media-hint">
          <span>Tip:</span> まずは旅行写真を1枚アップして、投稿を書いてみてください。
        </div>
      </section>

      <!-- 오른쪽: 폼 -->
      <section class="form-pane">

        <form id="writeForm"
              action="<%=request.getContextPath()%>/jpblog/writepro.do"
              method="post"
              enctype="multipart/form-data">

          <!-- 작성자(로그인 붙이면 세션으로 바꾸면 됨) -->
          <div class="row">
            <label class="lbl">作成者</label>
             <input class="inp"
         			name="userid"
         			type="text"
         			value="${sessionScope.userid}"
         			readonly />
          </div>

          <div class="row">
            <label class="lbl">タイトル</label>
            <input class="inp" name="title" type="text" placeholder="例）京都1日目：穴場グルメ" required />
          </div>

          <div class="row">
			<label class="lbl">カテゴリ</label>
			<select name="type" id="typeSelect" class="inp" required>
			  <option value="CITY">都市</option>
			  <option value="FOOD">グルメ</option>
			  <option value="FESTIVAL">祭り</option>
			</select>

			<label class="lbl" id="subLabel">都市</label>
			<select class="inp" name="category" id="subSelect">
			  <!-- JS가 옵션을 넣어줌 -->
			</select>
          </div>

		<div class="row">
		  <label class="lbl">場所</label>
		  <input type="text" id="placeInput" class="inp"
		         placeholder="場所(例: 大濠公園 / 渋谷スクランブル交差点)">
		  <button type="button" id="btnOpenPicker" class="btn-small">地図で選ぶ</button>
		  <small id="placeStatus" style="color:#777; margin-left:8px;"></small>
		</div>
		
		<input type="hidden" name="lat" id="lat">
		<input type="hidden" name="lng" id="lng">

          <div class="row">
            <label class="lbl">本文</label>
            <textarea class="ta" name="content" rows="8" placeholder="旅行の記録を残してみましょう :)"
                      required></textarea>

            <div class="counter">
              <span id="count">0</span>/2000
            </div>
          </div>

          <div class="row">
            <label class="lbl">メイン写真</label>
            <input class="file" id="images" name="imgfile" type="file" accept="image/*" />
            <div class="help">
              * ファイルを選択すると、左側にプレビューが表示されます。
            </div>
          </div>

          <!-- 하단 버튼(모바일용) -->
          <div class="mobile-actions">
            <button class="btn-primary w100" type="submit">投稿</button>
          </div>

        </form>

        <!-- 간단 가이드 -->
        <div class="guide">
          <div class="guide-title">アップロード確認</div>
          <ul class="guide-list">
            <li>タイトル／本文は必須</li>
            <li>写真は1枚（メイン画像）基準</li>
            <li>DB連携後は view.jsp でそのまま表示可能</li>
          </ul>
        </div>

      </section>

    </div>
  </div>
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

<script>
  // 글자수 카운트
  const ta = document.querySelector('textarea[name="content"]');
  const countEl = document.getElementById('count');
  if (ta && countEl) {
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
      mediaEmpty.style.display = 'none';
      mediaPreview.style.display = 'flex';
      mediaPreview.setAttribute('aria-hidden', 'false');
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
    changeBtn.addEventListener('click', () => fileInput.click());
  }

  const typeSelect = document.getElementById("typeSelect");
  const subSelect  = document.getElementById("subSelect");
  const subLabel   = document.getElementById("subLabel");

  /*
    ✅ DB/필터 연동을 깨지 않기 위해
    - value(실제 전송되는 값)는 기존 한글 그대로 유지
    - 화면에 보이는 label/text만 일본어로 표시
  */
  const optionsMap = {
    CITY: {
      label: "都市",
      required: true,
      items: [
        { value: "도쿄", label: "東京" },
        { value: "교토", label: "京都" },
        { value: "오사카", label: "大阪" },
        { value: "후쿠오카", label: "福岡" },
        { value: "삿포로", label: "札幌" },
        { value: "소도시", label: "地方都市" }
      ]
    },
    FOOD: {
      label: "グルメ分類",
      required: true,
      items: [
        { value: "라멘", label: "ラーメン" },
        { value: "스시", label: "寿司" },
        { value: "야키니쿠", label: "焼肉" },
        { value: "이자카야", label: "居酒屋" },
        { value: "카페", label: "カフェ" }
      ]
    },
    FESTIVAL: {
      label: "お祭り分類",
      required: true,
      items: [
        { value: "불꽃축제", label: "花火大会" },
        { value: "벚꽃축제", label: "桜まつり" },
        { value: "지역축제", label: "地域のお祭り" },
        { value: "겨울축제", label: "冬まつり" }
      ]
    }
  };

  function renderSubOptions(type) {
    const cfg = optionsMap[type] || optionsMap.CITY;

    // 라벨(보이는 텍스트)만 일본어
    subLabel.textContent = cfg.label;

    // 옵션 초기화
    subSelect.innerHTML = "";

    // 첫 옵션 (보이는 텍스트만 일본어)
    const first = document.createElement("option");
    first.value = "";
    first.textContent = "選択";
    subSelect.appendChild(first);

    // 나머지 옵션 추가 (value=한글 유지, text=일본어)
    cfg.items.forEach(obj => {
      const opt = document.createElement("option");
      opt.value = obj.value;       // ✅ DB 연동 값 유지
      opt.textContent = obj.label; // ✅ 화면 표시만 일본어
      subSelect.appendChild(opt);
    });

    // required 여부
    if (cfg.required) subSelect.setAttribute("required", "required");
    else subSelect.removeAttribute("required");
  }

  // 최초 1회 렌더
  renderSubOptions(typeSelect.value);

  // type 변경 시마다 렌더
  typeSelect.addEventListener("change", (e) => {
    renderSubOptions(e.target.value);
  });
  
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
  let picked = null; // [lat, lng]

  function initMap(){
    const fallback = [35.681236, 139.767125]; // 도쿄역
    map = L.map("pickMap").setView(fallback, 12);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 19,
      attribution: "&copy; OpenStreetMap contributors"
    }).addTo(map);

    marker = L.marker(fallback, { draggable: true }).addTo(map);

    // 지도 클릭 → 핀 이동
    map.on("click", (e) => setPicked([e.latlng.lat, e.latlng.lng], true));

    // 핀 드래그 종료 → 좌표 반영
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

      const lat = parseFloat(data[0].lat);
      const lng = parseFloat(data[0].lon);
      setPicked([lat, lng], true);

    }catch(e){
      console.log(e);
      alert("検索エラー");
    }
  }

  function openModal(){
    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.style.overflow = "hidden";

    // 검색어 기본값
    if (pickQuery) pickQuery.value = (placeInput.value || "").trim();

    // 지도 생성 1회
    if (!map) initMap();

    // 모달 열린 뒤 사이즈 재계산(흰 화면 방지)
    setTimeout(() => {
      map.invalidateSize();

      // 이미 hidden 좌표 있으면 그 좌표로
      if (latEl.value && lngEl.value && !isNaN(parseFloat(latEl.value)) && !isNaN(parseFloat(lngEl.value))) {
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

  // 버튼 이벤트
  openBtn && openBtn.addEventListener("click", openModal);
  closeBtn && closeBtn.addEventListener("click", closeModal);

  // 오버레이 클릭 닫기
  modal && modal.addEventListener("click", (e) => { if(e.target === modal) closeModal(); });

  // ESC 닫기
  window.addEventListener("keydown", (e) => {
    if(e.key === "Escape" && modal.classList.contains("is-open")) closeModal();
  });

  // 검색
  pickSearchBtn && pickSearchBtn.addEventListener("click", () => searchAndMove(pickQuery.value.trim()));
  pickQuery && pickQuery.addEventListener("keydown", (e) => {
    if(e.key === "Enter") { e.preventDefault(); searchAndMove(pickQuery.value.trim()); }
  });

  // ✅ “이 위치를 사용” → hidden lat/lng 저장
  pickUseBtn && pickUseBtn.addEventListener("click", () => {
    if(!picked) return;

    latEl.value = picked[0];
    lngEl.value = picked[1];

    placeStatus.textContent = "OK (" + picked[0].toFixed(6) + ", " + picked[1].toFixed(6) + ")";
    closeModal();
  });
	  
</script>

</body>
</html>

