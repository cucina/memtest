package memstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.junit.Test;

import static org.junit.Assert.*;

public class DriverTest {

	@Test
	public void testExample() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(baos);
		pw.println("CREATE 0 100 1.5");
		pw.println("UPDATE 0 105 1.6");
		pw.println("GET 0 100");
		pw.println("GET 0 110");
		pw.println("LATEST 0");
		pw.println("LATEST 1");
		pw.println("CREATE 1 110 2.5");
		pw.println("CREATE 1 115 2.4");
		pw.println("UPDATE 1 115 2.4");
		pw.println("UPDATE 1 120 2.3");
		pw.println("UPDATE 1 125 2.2");
		pw.println("LATEST 1");
		pw.println("GET 1 120");
		pw.println("UPDATE 1 120 2.35");
		pw.println("GET 1 122");
		pw.println("DELETE 1 122");
		pw.println("GET 1 125");
		pw.println("DELETE 1");
		pw.println("QUIT");
		pw.flush();
		InputStream in = new ByteArrayInputStream(baos.toByteArray());
		baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
		Driver driver = new Driver(in, out);
		driver.cycle();
		System.err.println(baos.toString());
		String[] resps = baos.toString().split("\\r?\\n");
		assertEquals("OK 1.5", resps[0]);
		assertEquals("OK 1.5", resps[1]);
		assertEquals("OK 1.5", resps[2]);
		assertEquals("OK 1.6", resps[3]);
		assertEquals("OK 105 1.6", resps[4]);
		assertEquals("ERR No history exists for identifier '1'", resps[5]);
		assertEquals("OK 2.5", resps[6]);
		assertEquals("ERR A history already exists for identifier '1'",
				resps[7]);
		assertEquals("OK 2.5", resps[8]);
		assertEquals("OK 2.4", resps[9]);
		assertEquals("OK 2.3", resps[10]);
		assertEquals("OK 125 2.2", resps[11]);
		assertEquals("OK 2.3", resps[12]);
		assertEquals("OK 2.3", resps[13]);
		assertEquals("OK 2.35", resps[14]);
		assertEquals("OK 2.35", resps[15]);
		assertEquals("OK 2.35", resps[16]);
		assertEquals("OK 2.35", resps[17]);
		assertEquals("Leaving", resps[18]);
	}
}
