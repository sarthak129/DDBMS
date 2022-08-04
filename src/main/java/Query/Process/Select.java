package Query.Process;

import Query.GlobalConfig;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Select {

	private Common common = new Common();
	private GlobalConfig globalConfig = new GlobalConfig();
	private String basePath = globalConfig.getBasePath();
	private String filePathSeparator = globalConfig.getPathSeparator();
	private String delimiter = globalConfig.getDelimiter();
	private String rowDelimiter = globalConfig.getRowDelimiter();

	public boolean check(String queryString, boolean doWrite) {
		boolean isValidQuery = false;

		String selectParseRegex = "(?:SELECT)\\s+(?<columns>.+|\\*)\\s+(?:FROM)\\s+(?<table>\\w+)\\s*(?:;|(?:(?:WHERE)\\s+(?<conditionColumn>\\w*)\\s(?<condition>==|<|>|<=|>=|!=)+\\s+(?:[\\\"](?<conditionValue>\\w*)[\\\"])(?:;)))";

		Pattern selectParsePattern = Pattern.compile(selectParseRegex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

		Matcher selectParseMatcher = selectParsePattern.matcher(queryString);

		String currentDatabase;

		currentDatabase = globalConfig.getGlobalDatabase();

		if (currentDatabase != null) {
			if (selectParseMatcher.find()) {
				String tableName = selectParseMatcher.group("table").trim();
				//System.out.println("tableName = " + tableName);

				List<String[]> rowData = common.getData(tableName);

				String primaryKeyName = common.getPrimaryKey(tableName);
				List<String> columnNamesList = common.getColumnNames(tableName);

				String columnsToShow = selectParseMatcher.group("columns").trim();

				List<Integer> columnIndexToShowList = new ArrayList<>();

				List<String> columnsListToShow = new ArrayList<>();

				if (columnsToShow.equals("*")) {
					columnIndexToShowList = null;
					isValidQuery = true;
				} else {
					String[] columnArrayList = columnsToShow.split(",");
					//System.out.println("columnArrayList.length = " + columnArrayList.length);
					if (columnArrayList.length <= columnNamesList.size()) {
						for (String columnName : columnArrayList) {
							if (columnNamesList.contains(columnName)) {
								int indexToAdd = columnNamesList.indexOf(columnName);
								columnIndexToShowList.add(indexToAdd);
								isValidQuery = true;
							} else {
								isValidQuery = false;
								break;
							}
						}
					} else {
						System.out.println("length of given columns and original columns not match.");
						isValidQuery = false;
					}
					Collections.sort(columnIndexToShowList);
				}

				//System.out.println("columnIndexToShowList = " + columnIndexToShowList);
				//System.out.println("isValidQuery = " + isValidQuery);
				String conditionColumnName = selectParseMatcher.group("conditionColumn");
				String condition = selectParseMatcher.group("condition");
				String conditionValue = selectParseMatcher.group("conditionValue");

				List<Integer> indexToSelectList = new ArrayList<>();

				if (isValidQuery) {
					if (conditionColumnName == null) {
						System.out.println("No condition column found");
						printRows(rowData, columnNamesList, columnIndexToShowList, indexToSelectList, doWrite);
					} else {
						int conditionColumnIndex = columnNamesList.indexOf(conditionColumnName);
						indexToSelectList = common.getSelectedRows(tableName, conditionColumnIndex, condition,
								conditionValue);
						System.out.println("indexToSelectList = " + indexToSelectList);
						if (indexToSelectList == null) {
							System.out.println("Invalid operator for data type provided");
							isValidQuery = false;
						} else if (indexToSelectList.size() == 0) {
							System.out.println("No content to select and display");
							isValidQuery = true;
						} else {
							printRows(rowData, columnNamesList, columnIndexToShowList, indexToSelectList, doWrite);
						}
					}
				}
			}
		} else {
			System.out.println("Please set default schema/database first");
			System.out.println("enter command : \n use <database_name>;");
			isValidQuery = false;
		}

		return isValidQuery;
	}

	private void printRows(List<String[]> rowData, List<String> columnNamesList, List<Integer> columnIndexToShowList,
			List<Integer> listOfIndexToPrint, boolean doWrite) {
		if (doWrite) {
			System.out.println("---------selected query row start------------------");
			if (listOfIndexToPrint.size() > 0) {
				if (columnIndexToShowList == null) {
					for (int i = 0; i < listOfIndexToPrint.size(); i++) {
						System.out.println(Arrays.toString(rowData.get(listOfIndexToPrint.get(i))));
					}
				} else {
					for (int i = 0; i < listOfIndexToPrint.size(); i++) {
						int selectedIndex = listOfIndexToPrint.get(i);
						for (int columnIndex = 0; columnIndex < rowData.get(selectedIndex).length; columnIndex++) {
							if (columnIndexToShowList.contains(columnIndex)) {
								System.out.print(rowData.get(selectedIndex)[columnIndex] + ",");
							}
						}
						System.out.println("");
					}
				}
			} else {
				if (columnIndexToShowList == null) {
					for (String[] row : rowData) {
						System.out.println(Arrays.toString(row));
					}
				} else {
					for (String[] row : rowData) {
						for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
							if (columnIndexToShowList.contains(columnIndex)) {
								System.out.print(row[columnIndex] + ",");
							}
						}
						System.out.println("");
					}
				}
			}

			System.out.println("---------selected query details end------------------");

		}
	}
}
