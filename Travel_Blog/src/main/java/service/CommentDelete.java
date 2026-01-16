package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Command;
import model.JPCommentDao;

public class CommentDelete implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession(false); //세션생성
		String userid = (session == null) ? null : (String) session.getAttribute("userid");
		
		
		if(userid == null) {
			response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
			return;
		}
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		int cno = Integer.parseInt(request.getParameter("cno"));
		
		JPCommentDao dao = new JPCommentDao();
		dao.deleteComment(cno, userid);
		
		response.sendRedirect(request.getContextPath() + "/jpblog/view.do?bno=" + bno);
	}

}
