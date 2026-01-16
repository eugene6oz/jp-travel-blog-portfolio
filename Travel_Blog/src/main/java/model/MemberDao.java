package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBManager;

public class MemberDao {

	public int useridcheck(String userid) {

		Connection conn=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from JPMEMBER where userid = ?";
		int ok=-1;
		
		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ok=1;
			}else {
				ok=-1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return ok;
	} //useridCheck end
		
	public int memberSave(MemberDto dto) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		
		String sql="insert into JPMEMBER (writer,userid,password,phone,email) values(?,?,?,?,?)";
		
		try {
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,dto.getWriter() );
			pstmt.setString(2,dto.getUserid() );
			pstmt.setString(3,dto.getPassword() );
			pstmt.setString(4,dto.getPhone() );
			pstmt.setString(5,dto.getEmail() );
			
			result = pstmt.executeUpdate();
			
			System.out.println(result);
			

			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public MemberDto searchUserId(String userid) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		MemberDto dto = null;
		
		String sql = "SELECT * FROM JPMEMBER WHERE USERID = ?";
		
		try {
			
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);	
			
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDto();
				dto.setUserid(rs.getString("userid"));
				dto.setWriter(rs.getString("writer"));
				dto.setPassword(rs.getString("password"));
				dto.setPhone(rs.getString("phone"));
				dto.setEmail(rs.getString("email"));
				dto.setProfile_Img(rs.getString("profile_img"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	}
	
	public int deleteMember(String userid) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int result = 0;
		String sql = "DELETE FROM jpmember WHERE userid=?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}
	
	public int updateWriter(String userid, String writer) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "UPDATE jpmember SET writer = ? WHERE userid = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writer);
			pstmt.setString(2, userid);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public int UpdatePassword(String userid, String password) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int result = 0;

	    String sql = "UPDATE jpmember SET password=? WHERE userid=?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, password);
	        pstmt.setString(2, userid);
	        result = pstmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	    return result;
	}

	public int updateProfileImg(String userid, String profileImg) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int result = 0;

	    String sql = "UPDATE JPMEMBER SET PROFILE_IMG = ? WHERE USERID = ?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, profileImg);
	        pstmt.setString(2, userid);
	        result = pstmt.executeUpdate();

	        System.out.println("updateProfileImg result=" + result + ", userid=" + userid + ", img=" + profileImg);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return result;
	}

	public String getProfileImgByUserId(String userid) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String img = null;

	    String sql = "SELECT profile_img FROM jpmember WHERE userid=?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        rs = pstmt.executeQuery();
	        if (rs.next()) img = rs.getString("profile_img");
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (rs != null) rs.close(); } catch(Exception e) {}
	        try { if (pstmt != null) pstmt.close(); } catch(Exception e) {}
	        try { if (conn != null) conn.close(); } catch(Exception e) {}
	    }
	    return img;
	}
	
	public boolean isUserIdExists(String userid) {
	    String sql = "SELECT 1 FROM member WHERE userid = ?";
	    try (Connection conn = DBManager.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, userid);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            return rs.next(); // 있으면 중복
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
}
