package memstore;

import java.io.Console;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 *
 * @author vlevine
 */
public class Driver {
	private static final String CREATE = "CREATE";

	private static final String UPDATE = "UPDATE";

	private static final String DELETE = "DELETE";

	private static final String GET = "GET";

	private static final String LATEST = "LATEST";

	private static Map<Integer, Map<Integer, String>> store = new HashMap<>();

	private static Map<String, CommandProcessor> processors = new HashMap();
	static {
		processors.put(CREATE, new Create());
		processors.put(UPDATE, new Update());
		processors.put(DELETE, new Delete());
		processors.put(GET, new Get());
		processors.put(LATEST, new Latest());
	}

	/**
	 *
	 *
	 * @param args .
	 */
	public static void main(String[] args) {
	}

	/**
    *
    */
	public void cycle() {
		Console c = System.console();
		String command;

		while ((command = c.readLine()) != null) {
			if (command.contains("QUIT")) {
				c.printf("Leaving");
				System.exit(0);
			}
			try {
				c.printf("OK %s", processCommand(command));
			} catch (Exception e) {
				c.printf("ERR %s", e.getMessage());
			}
		}
	}

	private String processCommand(String command) {
		String[] parts = command.split("\\s");

		if (parts.length == 0) {
			throw new IllegalArgumentException("Empty command");
		}

		if (parts.length == 1) {
			throw new IllegalArgumentException("No argument for command '" + parts[0] + "'");
		}

		CommandProcessor proc = processors.get(parts[0]);
		if (proc == null)
			throw new IllegalArgumentException("Invalid command '" + parts[0] + "'");
		return proc.process(parts);
	}

	private static class Latest extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			// TODO Auto-generated method stub

		}
	}

	private static class Get extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			// TODO Auto-generated method stub

		}
	}

	private static class Delete extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			// TODO Auto-generated method stub

		}
	}

	private static class Update extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> item = store.get(parts[0]);
			if (item == null) {
				throw new IllegalArgumentException("No history exists for identifier '" + parts[0]
						+ "'");
			}
			String prev = findLatestToTime((Integer) parts[1], item);
			item.put((Integer) parts[1], (String) parts[2]);
			store.put((Integer) parts[0], item);
			return prev;

		}
	}

	private static class Create extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> item = store.get(parts[0]);
			if (item != null) {
				throw new IllegalArgumentException("A history already exists for identifier '"
						+ parts[0] + "'");
			}
			Comparator<Integer> c1 = (Integer k1, Integer k2) -> k1.compareTo(k2);
			item = new TreeMap<Integer, String>(c1);
			item.put((Integer) parts[1], (String) parts[2]);
			store.put((Integer) parts[0], item);
			return (String) parts[2];
		}
	}
}
