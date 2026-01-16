package model;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
	 void doCommand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
//dao 메서드를 호출하여 crud 작업처리를 위한 설계도를 만들기 위해. override 하여 사용