package memstore;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
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

	private static Map<Integer, Map<Integer, String>> store = new HashMap<Integer, Map<Integer, String>>();

	private static Map<String, CommandProcessor> processors = new HashMap<String, CommandProcessor>();
	static {
		processors.put(CREATE, new Create());
		processors.put(UPDATE, new Update());
		processors.put(DELETE, new Delete());
		processors.put(GET, new Get());
		processors.put(LATEST, new Latest());
	}
	private InputStream in;
	private PrintStream out;

	/**
	 *
	 *
	 * @param args
	 *            .
	 */
	public static void main(String[] args) {
		Driver d = new Driver(System.in, System.out);
		d.cycle();
	}

	public Driver(InputStream in, PrintStream out) {
		this.in = in;
		this.out = out;
	}

	/**
    *
    */
	public void cycle() {
		String command;
		Scanner s = new Scanner(in);
		while ((command = s.nextLine()) != null) {
			if (command.contains("QUIT")) {
				out.println("Leaving");
				s.close();
				return;
			}
			try {
				out.println("OK " + processCommand(command));
			} catch (Exception e) {
				out.println("ERR " + e.getMessage());
			}
		}
	}

	private String processCommand(String command) {
		String[] parts = command.split("\\s");

		if (parts.length == 0) {
			throw new IllegalArgumentException("Empty command");
		}

		if (parts.length == 1) {
			throw new IllegalArgumentException("No argument for command '"
					+ parts[0] + "'");
		}

		CommandProcessor proc = processors.get(parts[0]);
		if (proc == null)
			throw new IllegalArgumentException("Invalid command '" + parts[0]
					+ "'");
		return proc.process(parts);
	}

	private static class Latest extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> items = findItems((Integer) parts[0], store);
			if (items.isEmpty()) {
				throw new IllegalArgumentException("Observations are empty");
			}
			Map.Entry<Integer, String> item = items.entrySet().iterator()
					.next();
			return item.getKey() + " " + item.getValue();
		}
	}

	private static class Get extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> items = findItems((Integer) parts[0], store);
			if (items.isEmpty()) {
				throw new IllegalArgumentException("Observations are empty");
			}
			if (parts.length < 2 || parts[1] == null) {
				throw new IllegalArgumentException(
						"Not enough argument for GET");
			}
			return findLatestToTime((Integer) parts[1], items);
		}
	}

	private static class Delete extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> items = findItems((Integer) parts[0], store);
			if (items.isEmpty()) {
				throw new IllegalArgumentException("Observations are empty");
			}
			if (parts.length < 2 || parts[1] == null) { // delete all
				String latest = items.values().iterator().next();
				store.remove(parts[0]);
				return latest;
			} else {
				String latest = findLatestToTime((Integer) parts[1], items);
				for (Iterator<Map.Entry<Integer, String>> it = items.entrySet()
						.iterator(); it.hasNext();) {
					Map.Entry<Integer, String> entry = it.next();
					if (entry.getKey() >= (Integer) parts[1]) {
						it.remove();
					}
				}
				return latest;
			}
		}
	}

	private static class Update extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> items = findItems((Integer) parts[0], store);
			String prev = findLatestToTime((Integer) parts[1], items);
			items.put((Integer) parts[1], (String) parts[2]);
			store.put((Integer) parts[0], items);
			return prev;

		}
	}

	private static class Create extends AbstractCommandProcessor {
		protected String processCommand(Object... parts) {
			Map<Integer, String> item = store.get(parts[0]);
			if (item != null) {
				throw new IllegalArgumentException(
						"A history already exists for identifier '" + parts[0]
								+ "'");
			}
			Comparator<Integer> c1 = new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}
			};
			item = new TreeMap<Integer, String>(c1);
			item.put((Integer) parts[1], (String) parts[2]);
			store.put((Integer) parts[0], item);
			return (String) parts[2];
		}
	}
}
