package service;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.JPBlogDao;
import model.JPBlogDto;

public class BlogList implements Command {

    @Override
    public void doCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");

        String city = request.getParameter("city");
        String festival = request.getParameter("festival");
        String food = request.getParameter("food");
        String keyword = request.getParameter("keyword");

        // ✅ page 파라미터 받기
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isBlank()) {
            page = Integer.parseInt(pageStr);
        }

        int pageSize = 9;      // 한 페이지 9개
        int blockSize = 5;     // 페이지 버튼 5개씩

        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        JPBlogDao dao = new JPBlogDao();

        // ✅ 페이징 리스트 조회 (너가 만든 blogListPaging 사용)
        List<JPBlogDto> list = dao.blogListPaging(city, festival, food, keyword, start, end);

        // ✅ 전체 개수 (필터/검색 조건 동일하게 count)
        int totalCount = dao.countBlogs(city, festival, food, keyword);

        int totalPage = (int)Math.ceil(totalCount / (double)pageSize);

        // ✅ 페이지 블록 계산
        int startPage = ((page - 1) / blockSize) * blockSize + 1;
        int endPage = startPage + blockSize - 1;
        if (endPage > totalPage) endPage = totalPage;

        // ✅ JSP로 전달
        request.setAttribute("list", list);

        request.setAttribute("page", page);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("totalCount", totalCount);

        request.setAttribute("city", city);
        request.setAttribute("festival", festival);
        request.setAttribute("food", food);
        request.setAttribute("keyword", keyword);

        // 사이드바(인기/추천)는 그대로
        request.setAttribute("popularList", dao.getPopularTop(3));
        request.setAttribute("recommendList", dao.getRecommendTop(3));
    }
}

