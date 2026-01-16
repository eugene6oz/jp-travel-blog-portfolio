package service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.MemberDao;
import model.MemberDto;
import util.PasswordBcrypt;

public class MemberEditPro implements Command {

    @Override
    public void doCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userid = (String) request.getSession().getAttribute("userid");
        if (userid == null) {
            response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
            return;
        }

        request.setCharacterEncoding("utf-8");
        String writer = request.getParameter("writer");
        String currentPw = request.getParameter("currentPassword");
        String newPw = request.getParameter("newPassword");
        String newPwConfirm = request.getParameter("newPasswordConfirm");
        
        MemberDao dao = new MemberDao();
        MemberDto dto = dao.searchUserId(userid);
        
        
        // 닉네임 수정
        if (writer != null && !writer.trim().isEmpty()) {
            dao.updateWriter(userid, writer.trim());
            request.getSession().setAttribute("writer", writer.trim());
        }

        if (currentPw != null && !currentPw.isEmpty()) {

            // 현재 비밀번호 검증
            if (!PasswordBcrypt.checkPassword(currentPw, dto.getPassword())) {
                request.setAttribute("msg", "현재 비밀번호가 올바르지 않습니다.");
                request.getRequestDispatcher("/member/edit.jsp")
                       .forward(request, response);
                return;
            }

            // 새 비밀번호 확인
            if (newPw == null || newPwConfirm == null || !newPw.equals(newPwConfirm)) {
                request.setAttribute("msg", "새 비밀번호가 서로 다릅니다.");
                request.getRequestDispatcher("/member/edit.jsp")
                       .forward(request, response);
                return;
            }

            
            String hashed = PasswordBcrypt.hashPassword(newPw);
            dao.UpdatePassword(userid, hashed);

            // 🔐 보안: 비번 바꾸면 로그아웃
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/jpmem/mypage.do");


    }
}
