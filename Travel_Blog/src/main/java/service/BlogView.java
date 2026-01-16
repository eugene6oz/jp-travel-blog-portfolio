package service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.JPBlogDao;
import model.JPBlogDto;
import model.JPCommentDao;
import model.JPCommentDto;
import model.MemberDao; // ✅ 추가 (너 프로젝트 DAO 이름에 맞게)

public class BlogView implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");

		// ✅ bno null 체크 먼저
		String bnoParam = request.getParameter("bno");
		if (bnoParam == null || bnoParam.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/jpblog/list.do");
			return;
		}
		int bno = Integer.parseInt(bnoParam);

		String loginUserId = (String) request.getSession().getAttribute("userid");
		boolean isLiked = false;

		// ✅ 댓글 페이징
		final int pageSize = 3;
		int cp = 1;
		String cpParam = request.getParameter("cp");
		if (cpParam != null && !cpParam.isEmpty()) {
			try { cp = Integer.parseInt(cpParam); } catch (Exception e) { cp = 1; }
		}
		if (cp < 1) cp = 1;

		JPBlogDao dao = new JPBlogDao();
		JPCommentDao commentDao = new JPCommentDao();

		// ✅ 1) 글 먼저 가져오기 (작성자 userid 필요)
		dao.viewCount(bno);
		JPBlogDto dto = dao.getOne(bno);

		// 글이 없으면 리스트로
		if (dto == null) {
			response.sendRedirect(request.getContextPath() + "/jpblog/list.do");
			return;
		}

		// ✅ 2) 작성자 프로필 이미지 가져오기 (로그인 유저 X, 작성자 O)
		MemberDao mdao = new MemberDao(); // DAO 이름 맞춰서
		String writerProfileImg = mdao.getProfileImgByUserId(dto.getUserid());

		// ✅ 3) 댓글 카운트 + 페이징 목록
		int totalComments = commentDao.countByBno(bno);
		int totalPages = (int) Math.ceil(totalComments / (double) pageSize);
		if (totalPages == 0) totalPages = 1;
		if (cp > totalPages) cp = totalPages;

		int startRow = (cp - 1) * pageSize + 1;
		int endRow = cp * pageSize;

		List<JPCommentDto> commentList = commentDao.getCommentsByBnoPaged(bno, startRow, endRow);

		// ✅ 좋아요 상태는 로그인 유저 기준(이건 맞음)
		if (loginUserId != null) isLiked = dao.isLiked(bno, loginUserId);
		int likeCount = dao.getLikeCount(bno);

		// ✅ JSP로 전달
		request.setAttribute("isLiked", isLiked);
		request.setAttribute("likeCount", likeCount);
		request.setAttribute("viewdto", dto);

		request.setAttribute("writerProfileImg", writerProfileImg); // ✅ 추가

		request.setAttribute("commentList", commentList);
		request.setAttribute("commentPage", cp);
		request.setAttribute("commentTotalPages", totalPages);
		request.setAttribute("commentTotal", totalComments);
	}
}

