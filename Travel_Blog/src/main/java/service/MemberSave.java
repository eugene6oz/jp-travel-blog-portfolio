package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.MemberDao;
import model.MemberDto;
import util.PasswordBcrypt;

public class MemberSave implements Command {

  @Override
  public void doCommand(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("MemberSave 실행");

    request.setCharacterEncoding("utf-8");

    String userid = request.getParameter("userid");
    String password = request.getParameter("password");
    String password2 = request.getParameter("password2");
    String writer = request.getParameter("writer");
    String phone = request.getParameter("phone");
    String email = request.getParameter("email");

    // ✅ 0) 아이디 중복 체크 (중복이면 join으로 보내고 alert 띄우기)
    MemberDao dao = new MemberDao();
    boolean dup = dao.isUserIdExists(userid); // ✅ 이 메서드 DAO에 추가 필요(아래 참고)

    if (dup) {
      response.sendRedirect(request.getContextPath() + "/jpmem/join.do?err=dup");
      return;
    }

    // ✅ 1) 비밀번호 확인
    if (password == null || !password.equals(password2)) {
      response.sendRedirect(request.getContextPath() + "/jpmem/join.do?err=pw");
      return;
    }

    MemberDto dto = new MemberDto();
    dto.setWriter(writer);
    dto.setUserid(userid);

    String hashpassword = PasswordBcrypt.hashPassword(password);
    dto.setPassword(hashpassword);

    dto.setPhone(phone);
    dto.setEmail(email);

    int result = dao.memberSave(dto);
    System.out.println("[JOIN] insert rows=" + result);

    if (result > 0) {
    	  response.sendRedirect(request.getContextPath() + "/jpmem/login.do?success=1");
    	  System.out.println("회원가입 성공");
    	} else {
    	  response.sendRedirect(request.getContextPath() + "/jpmem/join.do?err=fail");
    	  System.out.println("");
    	}
  }
}

