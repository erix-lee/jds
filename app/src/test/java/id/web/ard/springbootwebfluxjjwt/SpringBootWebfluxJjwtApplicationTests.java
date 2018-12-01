package id.web.ard.springbootwebfluxjjwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cnupa.app.security.PBKDF2Encoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootWebfluxJjwtApplicationTests {

	@Test
	public void contextLoads() {
		PBKDF2Encoder e=new PBKDF2Encoder();
		System.out.println(e.encode("admin"));
	}

}
