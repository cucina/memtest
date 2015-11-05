package accumulat;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PrimitiveAccumulatorTest {
	private Accumulator acc;

	@Before
	public void setUp() throws Exception {
		acc = new PrimitiveAccumulator();
	}

	@Test
	public void testAccumulate() {
		assertEquals(0, acc.getTotal());
		assertEquals(6, acc.accumulate(1, 2, 3));
		assertEquals(4, acc.accumulate(4));
		assertEquals(10, acc.getTotal());
	}

	@Test
	public void testReset() {
		assertEquals(0, acc.getTotal());
		assertEquals(6, acc.accumulate(1, 2, 3));
		assertEquals(6, acc.getTotal());
		acc.reset();
		assertEquals(0, acc.getTotal());
	}

	@Test
	public void testThreads() throws Exception {
		Accr r = new Accr(acc);

		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		assertEquals(10, acc.getTotal());
	}

}

class Accr implements Runnable {
	private Accumulator a;

	public Accr(Accumulator a) {
		this.a = a;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.err.println("Interrupted");
				return;
			}
			a.accumulate(1);
		}
	}
}
