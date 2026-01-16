package model;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserIdCheck implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String userid = request.getParameter("userid");
		MemberDao dao = new MemberDao();
		int result = dao.useridcheck(userid);
		//클라이언트로 결과 전송. 호출한 곳으로 결과전송.
		response.getWriter().print(result);
		
		
	}

}
