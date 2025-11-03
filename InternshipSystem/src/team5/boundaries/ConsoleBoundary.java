package team5.boundaries;

public abstract class ConsoleBoundary {

	public static void clearScreen() {
    	System.out.print("\033[H\033[2J");
        System.out.flush();
    }
	
	public static String safeValue(Object value) {
		if (value == null) {
			return "N/A";
		}
		if (value instanceof String) {
			String str = (String) value;
			return str.isEmpty() ? "N/A" : str;
		}
		return value.toString();
	}

	public static String valueOrNA(Enum<?> value) {
		return value != null ? value.name() : "N/A";
	}
}
