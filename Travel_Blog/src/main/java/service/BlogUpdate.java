package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Command;
import model.JPBlogDao;
import model.JPBlogDto;

public class BlogUpdate implements Command {

  private static final String UPLOAD_PATH = "/Users/eugene/upload";

  @Override
  public void doCommand(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setCharacterEncoding("utf-8");

    int bno = Integer.parseInt(request.getParameter("bno"));
    String title = request.getParameter("title");
    String content = request.getParameter("content");

    // ✅ 추가: lat/lng
    String latStr = request.getParameter("lat");
    String lngStr = request.getParameter("lng");

    JPBlogDao dao = new JPBlogDao();

    String oldImg = dao.getImgfileByBno(bno);

    Part imgfile = request.getPart("imgfile");
    String newFileName = null;

    if (imgfile != null && imgfile.getSize() > 0) {
      String ori = Paths.get(imgfile.getSubmittedFileName()).getFileName().toString();
      newFileName = UUID.randomUUID().toString() + "_" + ori;

      File dir = new File(UPLOAD_PATH);
      if (!dir.exists()) dir.mkdirs();

      imgfile.write(UPLOAD_PATH + File.separator + newFileName);

      // 새 파일 저장 성공 -> 기존 파일 삭제
      if (oldImg != null && !oldImg.trim().isEmpty()) {
        File oldFile = new File(UPLOAD_PATH, oldImg);
        if (oldFile.exists()) oldFile.delete();
      }
    }

    JPBlogDto dto = new JPBlogDto();
    dto.setBno(bno);
    dto.setTitle(title);
    dto.setContent(content);

    // ✅ 추가: 좌표 세팅 (빈값이면 null로 두기)
    if (latStr != null && !latStr.isBlank()) dto.setLat(Double.parseDouble(latStr));
    else dto.setLat(null);

    if (lngStr != null && !lngStr.isBlank()) dto.setLng(Double.parseDouble(lngStr));
    else dto.setLng(null);

    if (newFileName != null) dto.setImgfile(newFileName);

    int result = dao.blogUpdate(dto);
    System.out.println("[UPDATE] result=" + result);

  }
}
