package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.BlogDelete;
import service.BlogInsert;
import service.BlogLike;
import service.BlogList;
import service.BlogUpdate;
import service.BlogView;
import service.CommentDelete;
import service.CommentWrite;

@MultipartConfig(
		fileSizeThreshold = 1024*1024*2, //2MB 메모리 또는 임시폴더에 잠깐 저장
		maxFileSize = 1024*1024*10,      //10MB 파일 1개당 최대 크기.
		maxRequestSize = 1024*1024*50    //50MB 폼 전체 합산 크기 파일 여러개+텍스트 합쳐서 50MB까지 허용.
		)

@WebServlet("/jpblog/*")
public class BlogController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BlogController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}
	
	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");

		String uri = request.getPathInfo(); // /blog 다음의 경로를 가져옴
		String page = null;
		
		switch (uri) {
		
			case "/list.do":
				new BlogList().doCommand(request, response);
				page = "/index.jsp";
				break;
		
			case "/write.do":
				page = "/blog/writer.jsp";
				System.out.println("FORWARD PAGE = " + page);
				break;
				
			case "/writepro.do":
				new BlogInsert().doCommand(request, response);
			    response.sendRedirect(request.getContextPath() + "/jpblog/list.do");
				return;
				
			case "/view.do":
			    new BlogView().doCommand(request, response);
			    page = "/blog/view.jsp";
			    break;
			    
			case "/delete.do":
			    new BlogDelete().doCommand(request, response);
			        response.sendRedirect(request.getContextPath() + "/jpmem/mypage.do");
			    return;
			    
			case "/update.do":
			    new BlogView().doCommand(request, response); // bno로 viewdto 가져오는 기존 서비스 재사용
			    page = "/blog/update.jsp";
			    break;    
				
			case "/updatepro.do":
			    new BlogUpdate().doCommand(request, response);
			    response.sendRedirect(request.getContextPath()
			        + "/jpblog/view.do?bno=" + request.getParameter("bno"));
			    return;
			    
			case "/like.do":
				// 좋아요 기능 처리
				new BlogLike().doCommand(request, response);
				break;
			    
			case "/commentWrite.do":
				new CommentWrite().doCommand(request, response);
			    return;

			case "/commentDelete.do":
				new CommentDelete().doCommand(request, response);
			    return;	
			    
			default:
		        // 매칭 안 될 때 확인용
		        response.sendError(404, "No mapping for: " + uri);
		        return;    
				
		}
		// 여기에 URI에 따른 처리 로직을 추가하세요.
		
		System.out.println("FORWARD PAGE = " + page);
		if (page != null) {
			RequestDispatcher rd = request.getRequestDispatcher(page);
			rd.forward(request, response);
		}
	}
	
	

}
