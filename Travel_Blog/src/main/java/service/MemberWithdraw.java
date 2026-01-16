package service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.MemberDao;
import model.MemberDto;
import util.PasswordBcrypt;

public class MemberWithdraw implements Command {
  @Override
  public void doCommand(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String userid = (String) request.getSession().getAttribute("userid");
    String password = request.getParameter("password");

    if (userid == null || password == null) return;

    MemberDao dao = new MemberDao();
    MemberDto dto = dao.searchUserId(userid);
    if (dto == null) return;

    if (!PasswordBcrypt.checkPassword(password, dto.getPassword())) {
      response.sendRedirect(request.getContextPath() + "/jpmem/mypage.do?err=badpw");
      return;
    }

    dao.deleteMember(userid);
    request.getSession().invalidate();
    response.sendRedirect(
    	    request.getContextPath() + "/jpmem/login.do?withdraw=success"
    	);
    	return;
    
  }
}