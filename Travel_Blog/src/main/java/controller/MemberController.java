package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UserIdCheck;
import service.MemberEdit;
import service.MemberEditPro;
import service.MemberLogin;
import service.MemberLogout;
import service.MemberSave;
import service.MemberWithdraw;
import service.MyPage;

@WebServlet("/jpmem/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MemberController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	protected void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String uri = request.getPathInfo();
		System.out.println("[JPMEM] uri=" + uri + ", method=" + request.getMethod());
		String member= null;
		
	
		switch(uri) {
		case "/join.do" : 
			member ="/member/MemberJoin.jsp";
			break;
			
		case "/joinpro.do":
			new MemberSave().doCommand(request, response);
			return;	
				
		case "/login.do" : 
			member ="/member/login.jsp";
			break;
			
		case "/loginpro.do":
			new MemberLogin().doCommand(request, response);
			break;
			
		case "/logout.do" : 
			new MemberLogout().doCommand(request, response);
			break;
			
		case "/mypage.do" :
			new MyPage().doCommand(request, response);
			member ="/member/MyPage.jsp";
			break;	
			
		case "/edit.do":
		    new MemberEdit().doCommand(request, response);
		    member = "/member/edit.jsp";   // 너가 만든 edit.jsp 경로로
		    break;

		case "/editpro.do":
			new MemberEditPro().doCommand(request, response);
			return;
		    
		    
		case "/withdraw.do":
		    new MemberWithdraw().doCommand(request, response);
		    return;    
			
			
		case "/useridcheck.do" :
			new UserIdCheck().doCommand(request, response);
			break;	
			
		default:
		    response.sendError(404, "No mapping for: " + uri);
		    return;	
			
		}
		
		
		if(member != null) {
		    if (response.isCommitted()) return;
			RequestDispatcher rd = request.getRequestDispatcher(member);
			rd.forward(request, response);
		}
	}
}
