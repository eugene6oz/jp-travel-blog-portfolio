package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Command;

public class MemberLogout implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			session.invalidate(); // 세션 무효화
		}
		
		response.sendRedirect(request.getContextPath() + "/jpblog/list.do");
	}

}
