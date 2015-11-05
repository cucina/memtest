package accumulat;

import java.util.stream.IntStream;

public class PrimitiveAccumulator implements Accumulator {
	private int total = 0;

	@Override
	public int accumulate(int... values) {
		int i = IntStream.of(values).sum();
		total += i;
		return i;
	}

	@Override
	public int getTotal() {
		return total;
	}

	@Override
	public void reset() {
		total = 0;

	}
}
