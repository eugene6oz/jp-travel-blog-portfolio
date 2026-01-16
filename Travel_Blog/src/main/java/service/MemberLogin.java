package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Command;
import model.MemberDao;
import model.MemberDto;
import util.PasswordBcrypt;

public class MemberLogin implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");
		
		MemberDto dto = new MemberDao().searchUserId(userid);
		
		if (dto == null) {
            request.setAttribute("msg", "IDまたはパスワードが正しくありません");
            request.getRequestDispatcher("/member/login.jsp")
                   .forward(request, response);
            return;
        }
		boolean ok = PasswordBcrypt.checkPassword(password, dto.getPassword());
		
		if (!ok) {
            request.setAttribute("msg", "IDまたはパスワードが正しくありません");
            request.getRequestDispatcher("/member/login.jsp")
                   .forward(request, response);
            return;
        }
		
		HttpSession session = request.getSession();
		session.setAttribute("userid", dto.getUserid());
        session.setAttribute("writer", dto.getWriter());
        session.setAttribute("role", dto.getRole()); // 있으면 좋음
        session.setAttribute("profileImg", dto.getProfile_Img());

        
        response.sendRedirect(request.getContextPath() + "/jpblog/list.do");
	}

}
