package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import model.MemberDao;

@WebServlet("/jpmem/profileImgUpdate.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 10 * 1024 * 1024,
    maxRequestSize = 20 * 1024 * 1024
)
public class ProfileImgUpdate extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();

        String userid = (String) session.getAttribute("userid");
        if (userid == null) {
            response.sendRedirect(request.getContextPath() + "/jpmem/login.do");
            return;
        }

        Part filePart = request.getPart("profileImg");
        if (filePart == null || filePart.getSize() == 0) {
            response.sendRedirect(request.getContextPath() + "/jpmem/mypage.do");
            return;
        }

        String original = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot != -1) ext = original.substring(dot).toLowerCase();

        if (!(ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png") || ext.equals(".gif") || ext.equals(".webp"))) {
            response.sendRedirect(request.getContextPath() + "/jpmem/mypage.do");
            return;
        }

        String saveName = UUID.randomUUID().toString().replace("-", "") + ext;

        String saveDir = request.getServletContext().getRealPath("/image/profile");
        File dir = new File(saveDir);
        if (!dir.exists()) dir.mkdirs();

        filePart.write(new File(dir, saveName).getAbsolutePath());

        MemberDao dao = new MemberDao();
        dao.updateProfileImg(userid, saveName);

        session.setAttribute("profileImg", saveName);

        response.sendRedirect(request.getContextPath() + "/jpmem/mypage.do");
    }
}

