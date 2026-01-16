package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBManager;

public class JPCommentDao {

	public int insertComment (int bno, String userid, String content) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql =
	            "INSERT INTO jpcomment (cno, bno, userid, content) " +
	            "VALUES (jpcomment_seq.NEXTVAL, ?, ?, ?)";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, userid);
			pstmt.setString(3, content);

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}



	// (기존) 댓글 전체 목록 - 이제 안 써도 됨(남겨둠)
    public List<JPCommentDto> getCommentsByBno(int bno) {
        List<JPCommentDto> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql =
            "SELECT cno, bno, userid, content, regdate " +
            "FROM jpcomment WHERE bno=? ORDER BY cno ASC";

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bno);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                JPCommentDto dto = new JPCommentDto();
                dto.setCno(rs.getInt("cno"));
                dto.setBno(rs.getInt("bno"));
                dto.setUserid(rs.getString("userid"));
                dto.setContent(rs.getString("content"));
                dto.setRegdate(rs.getString("regdate"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return list;
    }

    public int deleteComment(int cno, String userid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        String sql =
            "DELETE FROM jpcomment WHERE cno=? AND userid=?";

        try {
            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cno);
            pstmt.setString(2, userid);
            result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return result;
    }

    public List<JPCommentDto> getMyComments(String userid) {

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    List<JPCommentDto> list = new ArrayList<>();

	    String sql =
	        "SELECT c.cno, c.bno, c.userid, c.content, " +
	        "       TO_CHAR(c.regdate, 'YYYY-MM-DD') AS regdate, " +
	        "       b.title AS blogTitle " +
	        "FROM jpcomment c " +
	        "JOIN jpblog b ON c.bno = b.bno " +
	        "WHERE c.userid = ? " +
	        "ORDER BY c.cno DESC";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            JPCommentDto dto = new JPCommentDto();
	            dto.setCno(rs.getInt("cno"));
	            dto.setBno(rs.getInt("bno"));
	            dto.setUserid(rs.getString("userid"));
	            dto.setContent(rs.getString("content"));
	            dto.setRegdate(rs.getString("regdate"));
	            dto.setBlogTitle(rs.getString("blogTitle"));
	            list.add(dto);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }

	    return list;
	}
    
 // ✅ 댓글 개수
    public int countByBno(int bno) {
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	int cnt = 0;

    	String sql = "SELECT COUNT(*) FROM jpcomment WHERE bno=?";

    	try {
    		conn = DBManager.getConnection();
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, bno);
    		rs = pstmt.executeQuery();
    		if (rs.next()) cnt = rs.getInt(1);
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try { if (rs != null) rs.close(); } catch (Exception e) {}
    		try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
    		try { if (conn != null) conn.close(); } catch (Exception e) {}
    	}
    	return cnt;
    }

    // ✅ 댓글 페이징 목록
    public List<JPCommentDto> getCommentsByBnoPaged(int bno, int startRow, int endRow) {
    	List<JPCommentDto> list = new ArrayList<>();
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;

    	String sql =
    		"SELECT * FROM ( " +
    		"  SELECT t.*, ROW_NUMBER() OVER (ORDER BY cno ASC) rn " +
    		"  FROM ( " +
    		"    SELECT cno, bno, userid, content, " +
    		"           TO_CHAR(regdate, 'YYYY-MM-DD HH24:MI') AS regdate " +
    		"    FROM jpcomment " +
    		"    WHERE bno=? " +
    		"  ) t " +
    		") WHERE rn BETWEEN ? AND ?";

    	try {
    		conn = DBManager.getConnection();
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, bno);
    		pstmt.setInt(2, startRow);
    		pstmt.setInt(3, endRow);

    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			JPCommentDto dto = new JPCommentDto();
    			dto.setCno(rs.getInt("cno"));
    			dto.setBno(rs.getInt("bno"));
    			dto.setUserid(rs.getString("userid"));
    			dto.setContent(rs.getString("content"));
    			dto.setRegdate(rs.getString("regdate"));
    			list.add(dto);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try { if (rs != null) rs.close(); } catch (Exception e) {}
    		try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
    		try { if (conn != null) conn.close(); } catch (Exception e) {}
    	}
    	return list;
    }

    
}

