package Analytics;

import java.io.*;
import java.util.*;
import Query.GlobalConfig;

import static LogManage.LoggerRun.path_logs_event;
import static LogManage.LoggerRun.path_logs_query;

public class AnalyticsRun {
	List<String[]> query_analytics_data = new ArrayList<>();

	private GlobalConfig globalConfig = new GlobalConfig();
	private String delimiter = globalConfig.getDelimiter();
	private String valuedelimiter = globalConfig.getRowDelimiter();
	private static String pathSeparator = File.separator;
	private static String basePath = "system" + pathSeparator + "";
	public void createFolder(){

		File folder = new File(basePath + pathSeparator + "@analytics");
		if (folder.exists()) {
			System.out.println("Analytics folder already exists");
		} else {
			folder.mkdirs();
			System.out.println("Analytics folder created successfully");
		}
	}
	public List<String[]> QueriesCount() {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			createFolder();
			FileReader file = new FileReader(path_logs_query);
			reader = new BufferedReader(file);
			String data = reader.readLine();
			String[] dataValueArray = data.split(delimiter);
			List<String> dataValues = new ArrayList<>();
			FileWriter analysisWriter = new FileWriter(basePath + pathSeparator + "@analytics"+pathSeparator+"Analysis.txt");
			writer = new BufferedWriter(analysisWriter);

			for (String row : dataValueArray) {
				dataValues.add(row);
			}
			for (String row : dataValues) {
				query_analytics_data.add(row.split(valuedelimiter));
			}
			Map<String, List<Map<String, Integer>>> map = new HashMap<>();
			for (int i = 0; i < query_analytics_data.size(); i++) {

				String a = Arrays.toString(query_analytics_data.get(i));
				a = a.substring(1, a.length() - 1);
				String[] query_list = a.split(",");

				if (map.containsKey(query_list[1])) {
					List<Map<String, Integer>> userListM = map.get(query_list[1]);
					for (int k = 0; k < userListM.size(); k++) {
						Map<String, Integer> user = userListM.get(k);
						if (user.containsKey(query_list[0])) {
							user.replace(query_list[0], user.get(query_list[0]) + 1);
						} else {
							user.put(query_list[0], 1);
						}
					}
				} else {
					List<Map<String, Integer>> userList = new ArrayList<>();
					Map<String, Integer> user = new HashMap<>();
					user.put(query_list[0], 1);
					userList.add(user);
					map.put(query_list[1], userList);
				}
			}
			String queryAnalysis = "";
			analysisWriter.write("This is the Analytics for Query Processing");
			analysisWriter.write("\n---------------------------------------------\n");
			for (var entry : map.entrySet()) {

				for (int i = 0; i < entry.getValue().size(); i++) {
					for (var entryUser : entry.getValue().get(i).entrySet()) {
						queryAnalysis = "User " + entryUser.getKey() + " submitted " + entryUser.getValue()
								+ " queries " + "for DB " + entry.getKey() + "\n";
						analysisWriter.write(queryAnalysis);
					}
				}

			}
			analysisWriter.close();
		} catch (Exception o) {
			System.out.println("Failed to get Queries Analytics");
		} finally {
			try {
				reader.close();
				writer.close();
			} catch (IOException e) {
				System.out.println("Failed to get Queries Analytics");
			}
		}
		return query_analytics_data;
	}

	public List<String[]> updateCount(String dbname) {
		BufferedReader reader = null;
		FileWriter analysisWriter = null;
		List<String[]> update_analytics_data = new ArrayList<>();
		try {
			FileReader fr = new FileReader(path_logs_event);
			analysisWriter = new FileWriter(basePath + pathSeparator + "@analytics"+pathSeparator+"Analysis.txt");
			Map<String, Integer> data = new HashMap<>();
			reader = new BufferedReader(fr);
			String str = reader.readLine();
			String[] dataValue = str.split(delimiter);
			List<String> dataList = new ArrayList<>();
			for (String row : dataValue) {
				dataList.add(row);
			}
			for (String row : dataList) {
				update_analytics_data.add(row.split(valuedelimiter));
			}
			for (int i = 0; i < update_analytics_data.size(); i++) {
				String a = Arrays.toString(update_analytics_data.get(i));
				a = a.substring(1, a.length() - 1);
				String[] dataArray = a.split(",");

				if (dataArray[1].trim().equalsIgnoreCase(dbname)) {
					if (dataArray[4].trim().equalsIgnoreCase("update")
							&& dataArray[5].trim().equalsIgnoreCase("success")) {
						if (data.containsKey(dataArray[3].trim())) {
							data.replace(dataArray[3].trim(), data.get(dataArray[3].trim()) + 1);
						} else {
							data.put(dataArray[3].trim(), 1);
						}
					}
				}
			}

			String queryAnalysis = "";
			analysisWriter.write("This is the Analytics for Query Processing");
			analysisWriter.write("\n---------------------------------------------\n");
			for (var entry : data.entrySet()) {
				queryAnalysis = "Total " + entry.getValue() + " Update operation are performed on " + entry.getKey();
				analysisWriter.write(queryAnalysis);
			}

			System.out.println(data);
		}

		catch (IOException f) {
			System.out.println("Failed to get Update Operations Analysis");
		} finally {
			try {
				reader.close();
				analysisWriter.close();
			} catch (IOException e) {
				System.out.println("Failed to get Queries Analytics");
			}
		}
		return update_analytics_data;
	}

//	public static void main(String[] args) {
//		AnalyticsRun a = new AnalyticsRun();
//		a.updateCount("db1");
//		// a.QueriesCount();
//	}
}
