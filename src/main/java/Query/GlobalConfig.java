package Query;

import java.io.File;

public class GlobalConfig {

	private static String globalDatabase;

	private static String pathSeparator = File.separator;

	private static String delimiter = "\u2088";

	private static String rowDelimiter = "\u2087";

	private static String sessionUserName;

	private static String vm;

	private static String vmIP;

	private static String basePath = "/home/sarthakpatel0301/csci-5408-w2022-group-19/system" + pathSeparator + "";

	public GlobalConfig() {
	}

	public GlobalConfig(String globalDatabase, String basePath) {
		this.globalDatabase = globalDatabase;
		this.basePath = basePath;
	}

	public String getSessionUserName() {
		return sessionUserName;
	}

	public void setSessionUserName(String sessionUserName) {
		GlobalConfig.sessionUserName = sessionUserName;
	}

	public String getGlobalDatabase() {
		return globalDatabase;
	}

	public void setGlobalDatabase(String globalDatabase) {
		this.globalDatabase = globalDatabase;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getPathSeparator() {
		return pathSeparator;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getRowDelimiter() {
		return rowDelimiter;
	}

	public static String getVm() {
		return vm;
	}

	public static void setVm(String vm) {
		GlobalConfig.vm = vm;
	}

	public static String getVmIP() {
		return vmIP;
	}

	public static void setVmIP(String vmIP) {
		GlobalConfig.vmIP = vmIP;
	}
	
	
}
