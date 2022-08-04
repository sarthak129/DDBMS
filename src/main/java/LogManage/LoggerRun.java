package LogManage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DistributedSystem.DistributedSystemRun;
import Query.GlobalConfig;

public class LoggerRun {

	public String logs = "";
	public String TimeStamp;
	public String query = "";
	public String querydesc = "";
	public String userName = "";
	public String query_status = "";
	public boolean db_status = false;
	FileWriter general_log;
	FileWriter event_log;
	FileWriter query_log;
	private static String pathSeparator = File.separator;
	private static String basePath = "system" + pathSeparator + "";
//	public static String path_logs_general = "./system/@Log/general_log.txt";
//	public static String path_logs_event = "./system/@Log/event_log.txt";
//	public static String path_logs_query = "./system/@Log/query_log.txt";
public void createFolder(){

	File folder = new File(basePath + pathSeparator + "@log");
	if (folder.exists()) {
		System.out.println("Log folder already exists");
	} else {
		folder.mkdirs();
		System.out.println("Log folder created successfully");
	}
}

	public static String log_folder = basePath + pathSeparator + "@log";
	public static String path_logs_general = log_folder+ pathSeparator +"general_log.txt";
	public static String path_logs_event = log_folder+ pathSeparator +"event_log.txt";
	public static String path_logs_query = log_folder+ pathSeparator +"query_log.txt";

	private GlobalConfig globalConfig = new GlobalConfig();
	private String delimiter = globalConfig.getDelimiter();
	private String valuedelimiter = globalConfig.getRowDelimiter();

//    public static String delimiter = "\u2088";
//    public static String valuedelimiter= "\u2087";

	public LoggerRun() {
		writeToFile();
	}

	public void writeToFile() {
		try {
			createFolder();
			File generalLogFile = new File(path_logs_general);
			File eventLogFile = new File(path_logs_event);
			File queryLogFile = new File(path_logs_query);
			general_log = new FileWriter(generalLogFile, true);
			event_log = new FileWriter(eventLogFile, true);
			query_log = new FileWriter(queryLogFile, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDateandTime() {
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String curr_date = "";
		Date date1 = new Date();
		curr_date = date.format(date1);

		return curr_date;
	}

	public String EventLogStatus() {
		if (db_status) {
			query_status = "SUCCESS";
		} else {
			query_status = "FAILURE";
		}
		return query_status;
	}

	public void GeneralLogsWrite(String query_type, String db_name, String tablecount) {
		try {

			String general_log_text[] = { query_type + valuedelimiter + db_name + valuedelimiter + getDateandTime()
					+ valuedelimiter + tablecount + delimiter };
			for (int i = 0; i < general_log_text.length; i++) {
				general_log.write(general_log_text[i]);
			}
		} catch (IOException e) {
			System.out.println("General Logs Write Failed");
		}
	}

	public void EventLogsWrite(String user_name, String dbname, String tablename, String query_type, String status) {
		try {
			String event_log_text[] = { user_name + valuedelimiter + dbname + valuedelimiter + getDateandTime()
					+ valuedelimiter + tablename + valuedelimiter + query_type + valuedelimiter + status + delimiter };
			for (int i = 0; i < event_log_text.length; i++) {
				event_log.write(event_log_text[i]);
			}
		} catch (Exception e) {
			System.out.println("Crash error occured");
		}
	}

	public void QueryLogsWrite(String user_name, String dbname, String query) {
		try {
			String query_log_text[] = { user_name + valuedelimiter + dbname + valuedelimiter + query + valuedelimiter
					+ getDateandTime() + delimiter };
			for (int i = 0; i < query_log_text.length; i++) {
				query_log.write(query_log_text[i]);
			}
		} catch (Exception e) {
			System.out.println("Query Logs Write Failed");
		}
	}
}
