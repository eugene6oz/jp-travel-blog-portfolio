package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBManager;

public class JPBlogDao {

	public int blogInsert(JPBlogDto dto) {

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int result = 0;

	    String sql =
	    		 "INSERT INTO JPBLOG (BNO, TITLE, CONTENT, USERID, IMGFILE, TYPE, CITY, FESTIVAL, FOOD, LAT, LNG, VIEWS, REGDATE) " +
	    		"VALUES (JPBLOG_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, SYSDATE)";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);

	        pstmt.setString(1, dto.getTitle());
	        pstmt.setString(2, dto.getContent());
	        pstmt.setString(3, dto.getUserid());
	        pstmt.setString(4, dto.getImgfile());
	        pstmt.setString(5, dto.getType());
	        pstmt.setString(6, dto.getCity());
	        pstmt.setString(7, dto.getFestival());
	        pstmt.setString(8, dto.getFood());
	        
	        if (dto.getLat() == null) pstmt.setNull(9, java.sql.Types.NUMERIC);
	        else pstmt.setDouble(9, dto.getLat());

	        if (dto.getLng() == null) pstmt.setNull(10, java.sql.Types.NUMERIC);
	        else pstmt.setDouble(10, dto.getLng());


	        result = pstmt.executeUpdate();

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

	public int countBlogs(String city, String festival, String food, String keyword) {

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    int cnt = 0;

	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT COUNT(*) FROM jpblog b WHERE 1=1 ");

	    if (city != null && !city.trim().isEmpty()) {
	        sql.append(" AND b.type='CITY' AND b.city=? ");
	    } else if (festival != null && !festival.trim().isEmpty()) {
	        sql.append(" AND b.type='FESTIVAL' AND b.festival=? ");
	    } else if (food != null && !food.trim().isEmpty()) {
	        sql.append(" AND b.type='FOOD' AND b.food=? ");
	    }

	    // ✅ 제목만 검색
	    if (keyword != null && !keyword.trim().isEmpty()) {
	        sql.append(" AND b.title LIKE ? ");
	    }

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql.toString());

	        int idx = 1;

	        if (city != null && !city.trim().isEmpty()) {
	            pstmt.setString(idx++, city);
	        } else if (festival != null && !festival.trim().isEmpty()) {
	            pstmt.setString(idx++, festival);
	        } else if (food != null && !food.trim().isEmpty()) {
	            pstmt.setString(idx++, food);
	        }

	        if (keyword != null && !keyword.trim().isEmpty()) {
	            pstmt.setString(idx++, "%" + keyword.trim() + "%");
	        }

	        rs = pstmt.executeQuery();
	        if (rs.next()) cnt = rs.getInt(1);

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return cnt;
	}

	public List<JPBlogDto> blogListPaging(String city, String festival, String food, String keyword, int start,int end) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ( ");
		sql.append("  SELECT t.*, ROW_NUMBER() OVER (ORDER BY t.bno DESC) rn FROM ( ");
		sql.append("    SELECT b.bno, b.title, b.userid, b.imgfile, b.views, ");
		sql.append("           TO_CHAR(b.regdate, 'YYYY-MM-DD HH24:MI') AS regdate, ");
		sql.append("           NVL(c.cnt, 0) AS commentCount ");
		sql.append("    FROM jpblog b ");
		sql.append("    LEFT JOIN (SELECT bno, COUNT(*) cnt FROM jpcomment GROUP BY bno) c ");
		sql.append("      ON b.bno = c.bno ");
		sql.append("    WHERE 1=1 ");

		if (city != null && !city.trim().isEmpty()) {
			sql.append(" AND b.type='CITY' AND b.city=? ");
		} else if (festival != null && !festival.trim().isEmpty()) {
			sql.append(" AND b.type='FESTIVAL' AND b.festival=? ");
		} else if (food != null && !food.trim().isEmpty()) {
			sql.append(" AND b.type='FOOD' AND b.food=? ");
		}

// ✅ 제목만 검색
		if (keyword != null && !keyword.trim().isEmpty()) {
			sql.append(" AND b.title LIKE ? ");
		}

		sql.append("  ) t ");
		sql.append(") WHERE rn BETWEEN ? AND ? ");

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			int idx = 1;

			if (city != null && !city.trim().isEmpty()) {
				pstmt.setString(idx++, city);
			} else if (festival != null && !festival.trim().isEmpty()) {
				pstmt.setString(idx++, festival);
			} else if (food != null && !food.trim().isEmpty()) {
				pstmt.setString(idx++, food);
			}

			if (keyword != null && !keyword.trim().isEmpty()) {
				pstmt.setString(idx++, "%" + keyword.trim() + "%");
			}

			pstmt.setInt(idx++, start);
			pstmt.setInt(idx++, end);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setUserid(rs.getString("userid"));
				dto.setViews(rs.getInt("views"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCommentCount(rs.getInt("commentCount"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	
	

	public List<JPBlogDto> blogList() {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		String sql = "SELECT b.bno, b.title, b.userid, b.imgfile, b.views, "
				+ "       TO_CHAR(b.regdate, 'YYYY-MM-DD HH24:MI') AS regdate, "
				+ "       NVL(c.cnt, 0) AS commentCount " + "FROM jpblog b "
				+ "LEFT JOIN (SELECT bno, COUNT(*) cnt FROM jpcomment GROUP BY bno) c " + "ON b.bno = c.bno "
				+ "ORDER BY b.bno DESC";

		try {

			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setUserid(rs.getString("userid"));
				dto.setViews(rs.getInt("views"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCommentCount(rs.getInt("commentCount"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public void viewCount(int bno) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		String sql = "UPDATE JPBLOG SET VIEWS = VIEWS + 1 WHERE BNO = ?";

		try {

			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bno);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public JPBlogDto getOne(int bno) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		JPBlogDto dto = new JPBlogDto();

		String sql = "SELECT * FROM JPBLOG WHERE BNO = ?";

		try {

			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bno);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setUserid(rs.getString("userid"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setViews(rs.getInt("views"));
				dto.setRegdate(rs.getString("regdate"));
				
				 	dto.setType(rs.getString("type"));
				    dto.setCity(rs.getString("city"));
				    dto.setFood(rs.getString("food"));
				    dto.setFestival(rs.getString("festival"));

				    // ✅ 추가: 위도/경도 (null 처리 중요)
				    double lat = rs.getDouble("lat");
				    dto.setLat(rs.wasNull() ? null : lat);

				    double lng = rs.getDouble("lng");
				    dto.setLng(rs.wasNull() ? null : lng);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dto;
	}

	public String getImgfileByBno(int bno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String imgfile = null;
		String sql = "SELECT imgfile FROM jpblog WHERE bno = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();

			if (rs.next())
				imgfile = rs.getString("imgfile");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return imgfile;
	}

	public int deleteBlog(int bno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "DELETE FROM jpblog WHERE bno = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public int blogUpdate(JPBlogDto dto) {

		  Connection conn = null;
		  PreparedStatement pstmt = null;
		  int result = 0;

		  // ✅ 좌표까지 업데이트
		  String sqlHasImg = "UPDATE jpblog SET title=?, content=?, imgfile=?, lat=?, lng=? WHERE bno=?";
		  String sqlNoImg  = "UPDATE jpblog SET title=?, content=?, lat=?, lng=? WHERE bno=?";

		  try {
		    conn = DBManager.getConnection();

		    if (dto.getImgfile() != null) {
		      pstmt = conn.prepareStatement(sqlHasImg);
		      pstmt.setString(1, dto.getTitle());
		      pstmt.setString(2, dto.getContent());
		      pstmt.setString(3, dto.getImgfile());

		      // lat
		      if (dto.getLat() != null) pstmt.setDouble(4, dto.getLat());
		      else pstmt.setNull(4, java.sql.Types.NUMERIC);

		      // lng
		      if (dto.getLng() != null) pstmt.setDouble(5, dto.getLng());
		      else pstmt.setNull(5, java.sql.Types.NUMERIC);

		      pstmt.setInt(6, dto.getBno());

		    } else {
		      pstmt = conn.prepareStatement(sqlNoImg);
		      pstmt.setString(1, dto.getTitle());
		      pstmt.setString(2, dto.getContent());

		      // lat
		      if (dto.getLat() != null) pstmt.setDouble(3, dto.getLat());
		      else pstmt.setNull(3, java.sql.Types.NUMERIC);

		      // lng
		      if (dto.getLng() != null) pstmt.setDouble(4, dto.getLng());
		      else pstmt.setNull(4, java.sql.Types.NUMERIC);

		      pstmt.setInt(5, dto.getBno());
		    }

		    result = pstmt.executeUpdate();

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


	public List<JPBlogDto> getPopularTop(int limit) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		String sql = "SELECT * FROM (" + "  SELECT bno, title, userid, views " + "  FROM jpblog "
				+ "  ORDER BY views DESC, bno DESC" + ") WHERE ROWNUM <= ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, limit);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setUserid(rs.getString("userid"));
				dto.setViews(rs.getInt("views"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<JPBlogDto> getRecommendTop(int limit) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		String sql = "SELECT * FROM (" + "  SELECT b.bno, b.title, b.userid, b.views, COUNT(l.userid) AS like_cnt "
				+ "  FROM jpblog b " + "  LEFT JOIN jpblog_like l ON b.bno = l.bno "
				+ "  GROUP BY b.bno, b.title, b.userid, b.views " + "  ORDER BY like_cnt DESC, b.bno DESC"
				+ ") WHERE ROWNUM <= ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, limit);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setUserid(rs.getString("userid"));
				dto.setViews(rs.getInt("views"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public int insertLike(int bno, String userid) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "INSERT INTO jpblog_like (bno, userid) VALUES (?, ?)";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, userid);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			result = -1;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int deleteLike(int bno, String userid) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "DELETE FROM jpblog_like WHERE bno=? AND userid=?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, userid);
			result = pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	public boolean isLiked(int bno, String userid) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT 1 FROM jpblog_like WHERE bno=? AND userid=?";
		boolean liked = false;

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			pstmt.setString(2, userid);
			rs = pstmt.executeQuery();
			if (rs.next())
				liked = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return liked;
	}

	public int getLikeCount(int bno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;

		String sql = "SELECT COUNT(*) FROM jpblog_like WHERE bno=?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			if (rs.next())
				cnt = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return cnt;
	}

	public List<JPBlogDto> blogListByCity(String city) {
		// TODO: 3단계에서 SQL로 city 필터링 구현

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		String sql = "SELECT bno, title, content, userid, imgfile, category, views, regdate " + "FROM jpblog "
				+ "WHERE type = 'CITY' AND city = ? " + "ORDER BY bno DESC";

		try {

			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, city);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setUserid(rs.getString("userid"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setCategory(rs.getString("category"));
				dto.setViews(rs.getInt("views"));
				dto.setRegdate(rs.getString("regdate"));

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<JPBlogDto> blogListByFestival(String festival) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		String sql = "SELECT bno, title, content, userid, imgfile, category, views, regdate, festival " + "FROM jpblog "
				+ "WHERE type = 'FESTIVAL' AND festival = ? " + "ORDER BY bno DESC";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, festival);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setUserid(rs.getString("userid"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setCategory(rs.getString("category"));
				dto.setViews(rs.getInt("views"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setFestival(rs.getString("festival")); // DTO에 있어야 함

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public List<JPBlogDto> blogListByFood(String food) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<JPBlogDto> list = new ArrayList<>();

		String sql = "SELECT bno, title, content, userid, imgfile, category, views, regdate, food " + "FROM jpblog "
				+ "WHERE type = 'FOOD' AND food = ? " + "ORDER BY bno DESC";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, food);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setUserid(rs.getString("userid"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setCategory(rs.getString("category"));
				dto.setViews(rs.getInt("views"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setFood(rs.getString("food")); // DTO에 있어야 함

				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

//✅ 내 글 목록
	public List<JPBlogDto> getMyPosts(String userid) {

		List<JPBlogDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT bno, title, userid, imgfile, views, regdate "
				+ "FROM jpblog WHERE userid=? ORDER BY bno DESC";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setUserid(rs.getString("userid"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setViews(rs.getInt("views"));
				dto.setRegdate(rs.getString("regdate"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
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
		return list;
	}

	// ✅ 내가 좋아요한 글 목록
	public List<JPBlogDto> getMyLikedPosts(String userid) {
		List<JPBlogDto> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT b.bno, b.title, b.userid, b.imgfile, b.views, b.regdate "
				+ "FROM jpblog_like l JOIN jpblog b ON l.bno = b.bno " + "WHERE l.userid=? ORDER BY l.likedate DESC";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JPBlogDto dto = new JPBlogDto();
				dto.setBno(rs.getInt("bno"));
				dto.setTitle(rs.getString("title"));
				dto.setUserid(rs.getString("userid"));
				dto.setImgfile(rs.getString("imgfile"));
				dto.setViews(rs.getInt("views"));
				dto.setRegdate(rs.getString("regdate"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
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
		return list;
	}

	public int countMyPosts(String userid) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;

		String sql = "SELECT COUNT(*) FROM jpblog WHERE userid = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			if (rs.next())
				cnt = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
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

		return cnt;
	}

	public int countMyLikes(String userid) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int cnt = 0;

		String sql = "SELECT COUNT(*) FROM jpblog_like WHERE userid = ?";

		try {
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs = pstmt.executeQuery();
			if (rs.next())
				cnt = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
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

		return cnt;
	}
	
	public int getPostCount(String userid) {

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int count = 0;

	    String sql = "SELECT COUNT(*) FROM jpblog WHERE userid = ?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return count;
	}
	public int getCommentCount(String userid) {

	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    int count = 0;

	    String sql = "SELECT COUNT(*) FROM jpcomment WHERE userid = ?";

	    try {
	        conn = DBManager.getConnection();
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userid);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return count;
	}
	
	
}