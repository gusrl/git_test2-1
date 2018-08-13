package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class HospitalDAO {
	
	Connection con; //오라클 서버와 연결할때 사용
	PreparedStatement psmt;//오라클 서버와 쿼리전송 역활
	ResultSet rs;//쿼리의 결과를 받을때 사용
	
	public HospitalDAO() {		
		/*
		try {
			Context ctx = new InitialContext(); 
			DataSource source = 
			  (DataSource)
			  ctx.lookup("java:comp/env/jdbc/myoracle");
			
			con = source.getConnection();
			System.out.println("DBCP연결성공");
		}
		catch(Exception e) {
			System.out.println("DBCP연결실패");
			e.printStackTrace();
		}
		*/
		String driver="oracle.jdbc.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1522:orcl";
		try {
			Class.forName(driver);
			String id = "kosmo";
			String pw = "1234";
			con = DriverManager.getConnection(url, id, pw);
			System.out.println("DB연결 성공");
		}catch(Exception e) {
			System.out.println("DB연결 실패");
		}
	}
	
	public void close() {
		try {
			if(rs!=null) rs.close();
			if(psmt!=null) psmt.close();
			if(con!=null) con.close();
		}
		catch(Exception e) {
			System.out.println("자원반납시 예외발생");
			e.printStackTrace();
		}
	}
	
	public int memberRegist(HospitalMemberDTO dto) {
		//적용된 행의 갯수확인을 위한 변수
		int affected = 0;
		try {
			String query = "INSERT INTO hospital_member ( "
				+ " mem_flag, mem_name, mem_gender, mem_age, mem_id, mem_pass, mem_dis, mem_int, "
				+ " tel, mobile, zipcode, addr1, addr2, email) "
				+ " VALUES ( "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			psmt = con.prepareStatement(query);
			
			psmt.setString(1, dto.getMem_flag());
			psmt.setString(2, dto.getMem_name());
			psmt.setString(3, dto.getMem_gender());
			psmt.setString(4, dto.getMem_age());
			psmt.setString(5, dto.getMem_id());
			psmt.setString(6, dto.getMem_pass());
			psmt.setString(7, dto.getMem_dis());
			psmt.setString(8, dto.getMem_int());
			psmt.setString(9, dto.getTel());
			psmt.setString(10, dto.getMobile());
			psmt.setString(11, dto.getZipcode());
			psmt.setString(12, dto.getAddr1());
			psmt.setString(13, dto.getAddr2());
			psmt.setString(14, dto.getEmail());
			
			affected = psmt.executeUpdate();
		}
		catch(Exception e) {
			System.out.println("insert중 예외발생");
			e.printStackTrace();
		}
		
		return affected;
	}
	
	public Map<String, String> memberLogin(String id,
			String pwd){
		
		Map<String, String> maps = 
				new HashMap<String, String>();
		
		String query = "SELECT id, pass, name, email FROM "
				+ " hospital_member WHERE id=? AND pass=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			psmt.setString(2, pwd);
			rs = psmt.executeQuery();
			
			//결과셋이 있는 경우에만 레코드를 읽어온다.
			if(rs.next()) {		
				//Map에 추가할때는 put()메소드 사용함.
				maps.put("id", rs.getString(1));
				maps.put("pass", rs.getString(2));
				maps.put("name", rs.getString("name"));
				maps.put("email", rs.getString("email"));
			}
			else {
				System.out.println("결과셋이 없습니다.");
			}
		}
		catch(Exception e) {
			System.out.println("getMemberDTO오류");
			e.printStackTrace();
		}
		
		return maps;
	}
	
}
