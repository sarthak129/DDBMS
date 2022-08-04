package Query.Process;

import Query.GlobalConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DistributedSystem.DistributedSystemRun;

public class Common {

	private GlobalConfig globalConfig = new GlobalConfig();
	private String basePath = globalConfig.getBasePath();
	private String filePathSeparator = globalConfig.getPathSeparator();
	private String delimiter = globalConfig.getDelimiter();
	private String rowDelimiter = globalConfig.getRowDelimiter();

	public boolean databaseCheck(String databaseName) {
		boolean isDatabaseExists = false;

		String currentDataBase = globalConfig.getGlobalDatabase();
		File database = new File(basePath + databaseName);
		if (database.exists()) {
			isDatabaseExists = true;
		}

		return isDatabaseExists;
	}

	public boolean tableCheck(String tableName) {
		boolean isTableExists = false;

		String currentDataBase = globalConfig.getGlobalDatabase();
		File tableFile = new File(basePath + currentDataBase + filePathSeparator + tableName + ".txt");
		if (tableFile.exists()) {
			isTableExists = true;
		}

		return isTableExists;
	}

	public List<String> getTablesList(String databaseName) {

		List<String> tableList = new ArrayList<>();

		globalConfig.setGlobalDatabase(databaseName);
		//System.out.println("globalConfig = " + globalConfig.getGlobalDatabase());
		String tableFileName = basePath + databaseName + filePathSeparator + databaseName + "@tables.txt";
		//System.out.println("tableFileName = " + tableFileName);

		try {
			FileReader fileReader = new FileReader(tableFileName, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String tables = bufferedReader.readLine();
			String[] tableListArray = tables.split(delimiter);

			for (String tableName : tableListArray) {
				tableList.add(tableName.trim());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}
		return tableList;
	}

	public List<String[]> getStructure(String tableName) {

		List<String[]> structureList = new ArrayList<>();

		String currentDataBase = globalConfig.getGlobalDatabase();
		String structureFile = basePath + currentDataBase + filePathSeparator + tableName + "@structure.txt";

		try {
			FileReader fileReader = new FileReader(structureFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String columns = bufferedReader.readLine();

			String[] columnList = columns.split(delimiter);
			//System.out.println("columnList[0] = " + columnList[0]);
			String columnNames = columnList[0].substring(1, columnList[0].length() - 1);
			String columnTypes = columnList[1].substring(1, columnList[1].length() - 1);
			String[] columnNamesList = columnNames.split(",");
			String[] columnTypesList = columnTypes.split(",");

			structureList.add(columnNamesList);
			structureList.add(columnTypesList);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException IoE) {
			IoE.printStackTrace();
		}

		return structureList;

	}

	public List<String> getColumnNames(String tableName) {

		List<String> columnList = new ArrayList<>();

		String currentDataBase = globalConfig.getGlobalDatabase();
		String structureFile = basePath + currentDataBase + filePathSeparator + tableName + "@structure.txt";

		try {
			FileReader fileReader = new FileReader(structureFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String columns = bufferedReader.readLine();

			String[] columnsString = columns.split(delimiter);
			String columnNames = columnsString[0].substring(1, columnsString[0].length() - 1);
			String[] columnListArray = columnNames.split(",");
			for (String columnName : columnListArray) {
				columnList.add(columnName.trim());
			}
			//System.out.println("columnList = " + columnList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException IoE) {
			IoE.printStackTrace();
		}

		return columnList;

	}

	public List<String[]> getData(String tableName) {

		List<String[]> rowData = new ArrayList<>();

		String currentDataBase = globalConfig.getGlobalDatabase();
		String dataFile = basePath + currentDataBase + filePathSeparator + tableName + ".txt";

		try {
			FileReader fileReader = new FileReader(dataFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String rows = bufferedReader.readLine();
			String[] rowValuesArray = rows.split(delimiter);

			List<String> rowValues = new ArrayList<>();

			for (String row : rowValuesArray) {
				rowValues.add(row);
			}

			rowValues.remove(0);

			for (String row : rowValues) {
				row = row.substring(1, row.length() - 1);
				rowData.add(row.split(rowDelimiter));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException IoE) {
			IoE.printStackTrace();
		}

		return rowData;

	}

	public List<Integer> getSelectedRows(String tableName, int indexToFind, String condition, String conditionValue) {
		List<Integer> rowIndex = new ArrayList<>();

		List<String[]> rowData = getData(tableName);

		List<String[]> columnStructure = getStructure(tableName);

		String[] columnTypeList = columnStructure.get(1);
		for (int i = 0; i < rowData.size(); i++) {
			String[] rowValue = rowData.get(i);
			for (int j = 0; j < rowValue.length; j++) {
				if (indexToFind == j) {
//                    System.out.println("datatype matching....");
					if (columnTypeList[j].trim().equals("int")) {
						// System.out.println("int data type");
						int dataValue = Integer.valueOf(rowValue[j]);
						int conditionValueInt = Integer.valueOf(conditionValue);
						if (condition.equals("==")) {
							if (dataValue == conditionValueInt) {
								rowIndex.add(i);
							}
						} else if (condition.equals("!=")) {
							if (dataValue != conditionValueInt) {
								rowIndex.add(i);
							}
						} else if (condition.equals("<")) {
							if (dataValue < conditionValueInt) {
								rowIndex.add(i);
							}
						} else if (condition.equals(">")) {
							if (dataValue > conditionValueInt) {
								rowIndex.add(i);
							}
						} else if (condition.equals("<=")) {
							if (dataValue <= conditionValueInt) {
								rowIndex.add(i);
							}
						} else if (condition.equals(">=")) {
							if (dataValue >= conditionValueInt) {
								rowIndex.add(i);
							}
						} else {
							rowIndex = null;
							// System.out.println("Invalid condition operator for int datatype");
						}
					} else if (columnTypeList[j].trim().equals("varchar") || columnTypeList[j].trim().equals("text")) {
						// System.out.println("varchar or text data type");
						if (condition.equals("==")) {
							if (rowValue[j].equals(conditionValue)) {
								rowIndex.add(i);
							}
						} else {
							rowIndex = null;
							// System.out.println("Invalid condition operator for varchar or text
							// datatype");
							break;
						}
					} else if (columnTypeList[j].trim().equals("boolean")) {
						// System.out.println("boolean data type");
						if (condition.equals("==")) {
							if (rowValue[j].equals(conditionValue)) {
								rowIndex.add(i);
							}
						} else {
							rowIndex = null;
							// System.out.println("Invalid condition operator for data type boolean");
							break;
						}
					}
				}
			}
		}

		return rowIndex;
	}

	public int updateDataRow(String tableName, String[] oldRowArray, String[] updateRowArray, boolean doWrite) {
		int updateCount = 0;
		if (doWrite) {
			String currentDataBase = globalConfig.getGlobalDatabase();
			String dataFile = basePath + currentDataBase + filePathSeparator + tableName + ".txt";

			String oldRow = "[" + String.join(rowDelimiter, oldRowArray) + "]";
			String updateRow;
			if (updateRowArray != null) {
				updateRow = "[" + String.join(rowDelimiter, updateRowArray) + "]";
			} else {
				oldRow += delimiter;
				updateRow = "";
			}
			//System.out.println("oldRow = " + oldRow);
			//System.out.println("updateRow = " + updateRow);

//			FileReader fileReader = null;
			try {
				Path path = Paths.get(dataFile);
				BufferedReader bufferedReader = Files.newBufferedReader(path);
				String data = bufferedReader.readLine();

				data = data.replace(oldRow, updateRow);
				//System.out.println("data = " + data);

				bufferedReader.close();

				PrintWriter printWriter = new PrintWriter(new File(dataFile), StandardCharsets.UTF_8);
				printWriter.print(data);
				DistributedSystemRun.uploadFileData(dataFile, data, false);
				printWriter.close();
				updateCount = 1;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException IoE) {
				IoE.printStackTrace();
			}
		}
		return updateCount;
	}

	public int getPrimaryIndex(String tableName) {

		List<String> columnNameList = new ArrayList<>();

		String currentDataBase = globalConfig.getGlobalDatabase();
		String structureFile = basePath + currentDataBase + filePathSeparator + tableName + "@structure.txt";

		try {
			FileReader fileReader = new FileReader(structureFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String columns = bufferedReader.readLine();

			String[] columnList = columns.split(delimiter);
			String columnNames = columnList[0].substring(1, columnList[0].length() - 1);
			List<String> columnNamesList = Arrays.asList(columnNames.split(","));

			String primaryKey = getPrimaryKey(tableName);

			return columnNamesList.indexOf(primaryKey);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException IoE) {
			IoE.printStackTrace();
			return -1;
		}

	}

	public String getPrimaryKey(String tableName) {

		String primaryKey = "";

		String currentDataBase = globalConfig.getGlobalDatabase();
		String keyFile = basePath + currentDataBase + filePathSeparator + tableName + "@key.txt";

		try {
			FileReader fileReader = new FileReader(keyFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String keyNames = bufferedReader.readLine();

			if (keyNames.length() > 1) {
				String[] keyNamesList = keyNames.split(delimiter);
				primaryKey = keyNamesList[0].substring(1, keyNamesList[0].length() - 1);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException IoE) {
			IoE.printStackTrace();
		}

		return primaryKey;
	}

	public List<String[]> getForeignKeys(String tableName) {

		List<String[]> foreignKeyList = new ArrayList<>();

		String currentDataBase = globalConfig.getGlobalDatabase();
		String keyFile = basePath + currentDataBase + filePathSeparator + tableName + "@key.txt";

		try {
			FileReader fileReader = new FileReader(keyFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String keyNames = bufferedReader.readLine();

			if (keyNames.length() > 1) {
				String[] keyNamesList = keyNames.split(delimiter);
				if (keyNamesList.length > 1) {
					String foreignKey = keyNamesList[1].substring(1, keyNamesList[1].length() - 1);
					String foreignRefKey = keyNamesList[2].substring(1, keyNamesList[2].length() - 1);
					String[] tableForeignKeyList = foreignKey.split(",");
					String[] tableForeignRefKeyList = foreignRefKey.split(",");
					foreignKeyList.add(tableForeignKeyList);
					foreignKeyList.add(tableForeignRefKeyList);
					for (int i = 0; i < tableForeignKeyList.length; i++) {
						//System.out
						//		.println("foreign key: " + tableForeignKeyList[i] + " to " + tableForeignRefKeyList[i]);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException IoE) {
			IoE.printStackTrace();
		}

		return foreignKeyList;
	}

	public List<Object> getPrimaryKeyList(String tableName) {

		List<Object> primaryKeyList = new ArrayList<>();

		String currentDataBase = globalConfig.getGlobalDatabase();
		String keyFile = basePath + currentDataBase + filePathSeparator + tableName + ".txt";

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(keyFile, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String primaryKeyValues = bufferedReader.readLine();
			String[] primaryKeys = primaryKeyValues.split(delimiter);
			// System.out.println("primaryKeys.length = " + primaryKeys.length);
			for (int i = 1; i < primaryKeys.length; i++) {
				// System.out.println("primaryKeys[i] = " + primaryKeys[i]);
				String rowValuesString = primaryKeys[i];
				rowValuesString = rowValuesString.substring(1, rowValuesString.length() - 1);
				String[] rowValues = rowValuesString.split(rowDelimiter);
				primaryKeyList.add(rowValues[0]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException IoE) {
			IoE.printStackTrace();
		}

		return primaryKeyList;
	}

}
