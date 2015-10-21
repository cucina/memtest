package memstore;

import java.util.Map;

/**
 * 
 *
 * @author vlevine
 */
public abstract class AbstractCommandProcessor implements CommandProcessor {
	/**
	 *
	 *
	 * @param args .
	 *
	 * @return .
	 */
	@Override
	public String process(String[] args) {
		Object[] objects = new Object[args.length];

		objects[0] = Integer.parseInt(args[1]);

		if (args.length > 2) {
			objects[1] = Integer.parseInt(args[2]);
		}

		if (args.length > 3) {
			objects[2] = args[3];
		}

		return processCommand(objects);
	}

	protected String findLatestToTime(Integer time, Map<Integer, String> items) {
		for (Map.Entry<Integer, String> entry : items.entrySet()) {
			if(entry.getKey() <= time) {
				return entry.getValue();
			}
		}
		
		return null;
	}

	/**
	 *
	 *
	 * @return .
	 */
	protected abstract String processCommand(Object... objects);
}
