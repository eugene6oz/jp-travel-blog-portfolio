package service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Command;
import model.JPBlogDao;

public class BlogDelete implements Command {

    // BlogInsert/BlogImage와 같은 경로로 맞추기
    private static final String UPLOAD_PATH = "/Users/eugene/upload";

    @Override
    public void doCommand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String bnoStr = request.getParameter("bno");
        
        if (bnoStr == null || bnoStr.isEmpty()) return;

        int bno;
        try {
            bno = Integer.parseInt(bnoStr);
        } catch (Exception e) {
            return;
        }

        JPBlogDao dao = new JPBlogDao();

        // 1) 삭제 전에 imgfile 파일명 가져오기
        String imgfile = dao.getImgfileByBno(bno);

        // 2) DB 삭제
        int result = dao.deleteBlog(bno);
        System.out.println("[DELETE] bno=" + bno + " result=" + result);

        // 3) DB 삭제 성공했으면 실제 파일도 삭제
        if (result > 0 && imgfile != null && !imgfile.trim().isEmpty()) {
            File f = new File(UPLOAD_PATH, imgfile);
            if (f.exists()) {
                boolean ok = f.delete();
                System.out.println("[DELETE] file=" + f.getAbsolutePath() + " ok=" + ok);
            } else {
                System.out.println("[DELETE] file not found=" + f.getAbsolutePath());
            }
        }
    }
}
