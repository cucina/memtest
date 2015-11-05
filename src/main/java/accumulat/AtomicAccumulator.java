package accumulat;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class AtomicAccumulator implements Accumulator {
	private AtomicInteger total = new AtomicInteger();

	@Override
	public int accumulate(int... values) {
		int i = IntStream.of(values).sum();
		total.addAndGet(i);
		return i;
	}

	@Override
	public int getTotal() {
		return total.get();
	}

	@Override
	public void reset() {
		total.set(0);

	}
}
