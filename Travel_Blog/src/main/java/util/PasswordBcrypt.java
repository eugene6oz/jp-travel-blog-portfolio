package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordBcrypt {

	//비밀번호 암호화
		public static String hashPassword(String password) {
			return BCrypt.hashpw(password, BCrypt.gensalt());
		}
		// password를 BCrypt실행해서 암호화를 시켜라 (무작위 해시값대입)
		
		//gensalt() 메서드는 솔드(salt)를 자동으로 생성
		//salt : 해시 함수에 추가되는 임이의 데이터로 동일한 데이터라도 솔트가 다르면 해시값이 달라짐
		// 해시된 비밀번호 반환
		
		//비밀번호 비교
		public static boolean checkPassword(String password, String hasheld) {
			return BCrypt.checkpw(password, hasheld);
			
			//password와 hasheld를 비교해서 같으면 true를 리턴 다르면 false를 리턴
		}
	
}
