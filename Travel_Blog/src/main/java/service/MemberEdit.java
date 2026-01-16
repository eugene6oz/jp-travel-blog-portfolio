package service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.MemberDao;
import model.MemberDto;

public class MemberEdit implements Command {
	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String userid = (String) request.getSession().getAttribute("userid");
		if (userid == null) {
			response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
			return;
		}

		MemberDao dao = new MemberDao();
		MemberDto dto = dao.searchUserId(userid);

		request.setAttribute("dto", dto);
	}
}