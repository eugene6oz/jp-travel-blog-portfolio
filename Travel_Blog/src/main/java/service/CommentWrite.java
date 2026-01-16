package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Command;
import model.JPCommentDao;

public class CommentWrite implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession(false);
		String userid = (session != null) ? (String) session.getAttribute("userid") : null;
		
		if(userid == null) {
			response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
			return;
		}
		
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		String content = request.getParameter("content");
		
		JPCommentDao dao = new JPCommentDao();
		dao.insertComment(bno, userid, content);
		
		response.sendRedirect(request.getContextPath() + "/jpblog/view.do?bno=" + bno);
		
	}

}
