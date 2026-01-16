package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Command;
import model.JPBlogDao;

public class BlogLike implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		
		String userid = (String) session.getAttribute("userid");
		
		if(userid == null) {
			response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
			return;
		}
		
		int bno = Integer.parseInt(request.getParameter("bno"));
		String mode = request.getParameter("mode"); // "like" or "unlike"
		
		JPBlogDao dao = new JPBlogDao();
		
		if ("unlike".equals(mode)) {
            dao.deleteLike(bno, userid);
        } else {
            dao.insertLike(bno, userid);
        }
		
		response.sendRedirect(
	            request.getContextPath() + "/jpblog/view.do?bno=" + bno
	        );
		
	}

}
