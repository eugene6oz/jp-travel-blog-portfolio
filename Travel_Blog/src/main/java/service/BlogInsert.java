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

public class BlogInsert implements Command {

	@Override
	public void doCommand(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("utf-8");
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String userid = request.getParameter("userid");
		String type = request.getParameter("type");
		String category = request.getParameter("category");
		String latStr = request.getParameter("lat");
		String lngStr = request.getParameter("lng");
		
		//첨부파일은 getParameter(). 메서드로 요청할 수 없다
		Part imgfile = request.getPart("imgfile");
		
		
		
		String fileName = null;
		if(imgfile != null && imgfile.getSize()>0) {// 첨부파일이 있으면
			String oriFileName = Paths.get(imgfile.getSubmittedFileName()).getFileName().toString();
			//오리지널 파일 이름 구하기
			fileName = UUID.randomUUID().toString()+"_"+oriFileName;
			//파일 중복을 피하기 위해서 오리지널 파일 이름을 변경
			String uploadPath = "/Users/eugene/upload";
			//물리적으로 첨부파일 저장할 경로
			File uploadDir = new File(uploadPath);
			//uploadPath 파일 경로 정보 객체 생성
			if(!uploadDir.exists()) {
				//uploadDir 폴더가 존재하지 않으면
				uploadDir.mkdirs();
				//최상위 폴더부터 하위폴더 생성한다
			}
			
			String filePath = uploadPath+File.separator+fileName; //separator 운영체제에맞는 폴더구분자
			//파일 전체 경로를 구한다
			//separator 운영체제에 따라 파일 경로 자동으로 바꾼다
			imgfile.write(filePath);
			//파일을 물리적으로 저장한다
			System.out.println("파일저장 완료 : "+filePath);
		
		}

		if (type == null || type.trim().isEmpty()) {
		    System.out.println("type이 비었습니다.");
		}

		if (category == null || category.trim().isEmpty()) {
		    System.out.println("세부 분류(category)가 비었습니다.");
		    // 여기서 return 하거나, 다시쓰기 처리 추천
		}
		
		
		
		JPBlogDto dto = new JPBlogDto();
		dto.setTitle(title);
		dto.setContent(content);
		dto.setUserid(userid);
		dto.setImgfile(fileName);
		dto.setType(type);
		
		dto.setCity(null);
		dto.setFestival(null);
		dto.setFood(null);
		
		if ("CITY".equals(type)) {
		    dto.setCity(category);            // 도쿄/교토/...
		} else if ("FESTIVAL".equals(type)) {
		    dto.setFestival(category);        // 벚꽃축제/불꽃축제/...
		} else if ("FOOD".equals(type)) {
		    dto.setFood(category);            // 카페/라멘/스시/...
		}

		System.out.println("lat param = " + latStr);
		System.out.println("lng param = " + lngStr);
		
		if (latStr != null && !latStr.isBlank()) dto.setLat(Double.parseDouble(latStr));
		if (lngStr != null && !lngStr.isBlank()) dto.setLng(Double.parseDouble(lngStr));
		
		JPBlogDao dao = new JPBlogDao();
		int result = dao.blogInsert(dto);
        System.out.println("insert result=" + result);


	
		}
	}