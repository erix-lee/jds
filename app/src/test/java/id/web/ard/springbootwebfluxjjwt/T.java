package id.web.ard.springbootwebfluxjjwt;

import com.cnupa.app.security.PBKDF2Encoder;

public class T {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PBKDF2Encoder e=new PBKDF2Encoder();
		System.out.println(e.encode("admin"));
	}

}
