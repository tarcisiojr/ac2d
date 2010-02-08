package ufu.mestrado.gui.util;

public class Logger {
	private Class<?> klass;
	private boolean debug = true;
	
	public static Logger getClass(Class<?> klass) {
		Logger logger = new Logger();
		logger.klass = klass;
		
		return logger;
	}
	
	public void debug(String message) {
		if (debug)
			info(message);
	}
	
	public void info(String info) {
		//System.out.println(klass.getName() + ": " + info);
	}
	
	public void error(String message) {
		System.err.println(klass.getName() + ": " + message);
	}
	
	public void error(String message, Throwable t) {
		System.err.println(klass.getName() + ": " + message);
		t.printStackTrace();
	}
}
