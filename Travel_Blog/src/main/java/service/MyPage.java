package service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Command;
import model.JPBlogDao;
import model.JPBlogDto;
import model.JPCommentDao;
import model.JPCommentDto;

public class MyPage implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession(); //세션생성
		String userid = (String) session.getAttribute("userid");
		
		
		
		if (userid == null) {
		    response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
		    return;
		}
		 
		 
		 
		 JPBlogDao dao = new JPBlogDao();
		 JPCommentDao cdao = new JPCommentDao();
		 

		int postCount = dao.getPostCount(userid);
		int commentCount = dao.getCommentCount(userid);
		 
		 //내글
		 List<JPBlogDto> myPosts = dao.getMyPosts(userid);
		 
		 // 내 댓글(원글 제목 포함)
	     List<JPCommentDto> myComments = cdao.getMyComments(userid);
	     
	     //내 좋아요
	     List<JPBlogDto> myLikes = dao.getMyLikedPosts(userid);

	     request.setAttribute("myPosts", myPosts);
	     request.setAttribute("myComments", myComments);
	     request.setAttribute("myLikes", myLikes);
	     request.setAttribute("postCount", postCount);
	     request.setAttribute("commentCount", commentCount);
	     
		
	}

}
