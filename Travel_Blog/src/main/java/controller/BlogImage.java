package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jpblog/image")
public class BlogImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // BlogInsert에서 저장한 경로랑 반드시 동일해야 함
    private static final String UPLOAD_PATH = "/Users/eugene/upload";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");

        if (name == null || name.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "name is required");
            return;
        }

        File file = new File(UPLOAD_PATH, name);

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "file not found: " + name);
            return;
        }

        response.setContentType("image/*");

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {

            byte[] buf = new byte[8192];
            int len;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        }
        
        System.out.println("[IMAGE] name=" + name);
        System.out.println("[IMAGE] path=" + file.getAbsolutePath());
        System.out.println("[IMAGE] exists=" + file.exists());
    }
}

