package accumulat;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AtomicAccumulatorTest {
	private Accumulator acc;

	@Before
	public void setUp() throws Exception {
		acc = new AtomicAccumulator();
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

