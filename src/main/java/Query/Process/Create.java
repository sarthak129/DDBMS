package Query.Process;

import Query.GlobalConfig;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DistributedSystem.DistributedSystemRun;

public class Create {

	private GlobalConfig globalConfig = new GlobalConfig();
	private String basePath = globalConfig.getBasePath();
	private String filePathSeparator = globalConfig.getPathSeparator();
	private String delimiter = globalConfig.getDelimiter();

	Common common = new Common();

	public boolean check(String queryString, boolean doWrite) {
		boolean isValidQuery = false;

		queryString = queryString.toLowerCase();

		List<String> tableColumnName = new ArrayList<>();
		List<String> tableColumnType = new ArrayList<>();
		List<String> primaryKeys = new ArrayList<>();
		List<String> foreignKeys = new ArrayList<>();
		List<String> foreignRefKeys = new ArrayList<>();

		String currentDataBase;

		// initial query
		// String createParseRegex = "(?:create)\\s+(?:table|database)(?:\\s+(?:IF NOT
		// EXISTS))?\\s+(\\w*)(;|([^;]*))";

		// complex query regex
		String createParseRegex = "(?:create)\\s+(?:(?:database\\s+(?:IF NOT EXISTS\\s*)?(?<database>\\w*);)|(?:table\\s+(?:IF NOT EXISTS\\s*)?(?<table>\\w*)\\s*(?<structure>[^;]*);))";

		String structureValueRegex = "(?>(\\w*|(?>primary key)|(?>foreign key))\\s+(int|varchar|text|boolean|(?:[(](\\w*)[)]))(?:\\s+(?:references)\\s+(\\w*)\\s+(?:[(](\\w*)[)])|,|$))";

		Pattern createParsePattern = Pattern.compile(createParseRegex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

		Pattern structureParsePattern = Pattern.compile(structureValueRegex,
				Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

		// group 1 - database name or table name
		// group 2 - if ; then it's valid for database
		// group 3 - if it's contains (table structure)
		Matcher createParseMatcher = createParsePattern.matcher(queryString);

		if (queryString.contains("database")) {
			// System.out.println("database detected");
			if (createParseMatcher.find()) {
				if (createParseMatcher.group("database").isEmpty()) {
					System.out.println("Invalid query no database name found");
					isValidQuery = false;
				} else {
					String dbName = createParseMatcher.group("database");
					// System.out.println("Database Name " + dbName);
					File folder = new File(basePath + dbName);
					if (folder.exists()) {
						System.out.println("Database named " + dbName + " already exists");
					} else {
						folder.mkdirs();
						writeMetaData(dbName);
						DistributedSystemRun.createFolder(basePath + dbName);
						System.out.println("Database Created successfully");
					}
					isValidQuery = true;
				}
			}
		}
		//
		// table key structure text file
//        primaryKey=id
//        foreigeKey=[salaryId]
//        foreigeKeyRef=[salary.id]
		else if (queryString.contains("table")) {
			currentDataBase = globalConfig.getGlobalDatabase();
			// System.out.println("table detected");
			// System.out.println("currentDataBase = " + currentDataBase);
			if (currentDataBase != null) {
				if (createParseMatcher.find()) {
					if (createParseMatcher.group("table").isEmpty() || createParseMatcher.group("table").isBlank()) {
						System.out.println("Invalid query no table name found");
						isValidQuery = false;
					} else {
						System.out.println("valid table query");
						String tableName = createParseMatcher.group("table");
						//System.out.println("Table Name " + tableName);
						if (common.tableCheck(tableName)) {
							System.out.println(tableName + " table already exists.");
							isValidQuery = false;
						} else {
							String structure = createParseMatcher.group("structure");
							structure = structure.trim();
							structure = structure.substring(1, structure.length() - 1);
							//System.out.println("structure = " + structure);
							String[] structureList = structure.split(",");
							Matcher structureParseMatcher = structureParsePattern.matcher(structure);
							//System.out.println(structureList.length);
							if (structureList.length == structureParseMatcher.results().count()) {
								//System.out.println("valid structure");
								structureParseMatcher.reset();
								while (structureParseMatcher.find()) {
									if (structureParseMatcher.group(1).equals("primary key")) {
										//System.out.println("Primary Key");
										String primaryKey = structureParseMatcher.group(3).trim();
										//System.out.println("structureParseMatcher.group(3) = " + primaryKey);
										primaryKeys.add(primaryKey);
									} else if (structureParseMatcher.group(1).equals("foreign key")) {
										//System.out.println("Foreign Key");
										String foreignKey = structureParseMatcher.group(3).trim();
										String foreignRefKey = structureParseMatcher.group(4) + "."
												+ structureParseMatcher.group(5).trim();
										//System.out.println("structureParseMatcher.group(3) = " + foreignKey);
										foreignKeys.add(foreignKey);
										//System.out.println("structureParseMatcher.group(4) = " + foreignRefKey);
										foreignRefKeys.add(foreignRefKey);
									} else {
										//System.out.println("Data column value");
										String columnName = structureParseMatcher.group(1).trim();
										String columnType = structureParseMatcher.group(2).trim();
										//System.out.println("columnName = " + columnName);
										//System.out.println("columnType = " + columnType);
										tableColumnName.add(columnName);
										tableColumnType.add(columnType);
									}

								}
								writeTableList(currentDataBase, tableName, doWrite);
								writeTable(currentDataBase, tableName, tableColumnName, doWrite);
								writeStructure(currentDataBase, tableName, tableColumnName, tableColumnType, doWrite);
								writeKey(currentDataBase, tableName, primaryKeys, foreignKeys, foreignRefKeys, doWrite);
								isValidQuery = true;
							} else {
								System.out.println("invalid table query because of invalid structure");
								isValidQuery = false;
							}
						}
					}
				} else {
					System.out.println("invalid table query");
					isValidQuery = false;
				}
			} else {
				System.out.println("Please set default schema/database first");
				System.out.println("enter command : \n use <database_name>;");
				isValidQuery = false;
			}
		} else {
			System.out.println("invalid create query table or database not found");
			isValidQuery = false;
		}
		return isValidQuery;
	}

	public boolean writeTableList(String dbName, String tableName, boolean doWrite) {
		boolean isWritten = false;

		String fileName = basePath + dbName + filePathSeparator + "" + dbName + "@tables.txt";
		//System.out.println("fileName = " + fileName);
		if (doWrite) {
			try {
				File file = new File(fileName);
				if (file.exists()) {
					FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					PrintWriter printWriter = new PrintWriter(bufferedWriter);
					printWriter.print(delimiter + tableName.trim());
					DistributedSystemRun.uploadFileData(fileName, delimiter + tableName.trim(), true);
					printWriter.close();
					bufferedWriter.close();
					fileWriter.close();
				}
				// create new file
				else {
					PrintWriter printWriter = new PrintWriter(file, StandardCharsets.UTF_8);
					printWriter.print(tableName.trim());
					DistributedSystemRun.uploadFileData(fileName, tableName.trim(), false);
					printWriter.close();
				}
				isWritten = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isWritten;
	}

	public boolean writeTable(String dbName, String tableName, List<String> tableColumnName, boolean doWrite) {
		boolean isWritten = false;
		if (doWrite) {
			String fileName = basePath + dbName + filePathSeparator + "" + tableName + ".txt";
			//System.out.println("fileName = " + fileName);

			try {
				PrintWriter fileWriter = new PrintWriter(new File(fileName), StandardCharsets.UTF_8);
				fileWriter.print(tableColumnName.toString().trim() + delimiter);
				DistributedSystemRun.uploadFileData(fileName, tableColumnName.toString().trim() + delimiter, false);
				fileWriter.close();
				isWritten = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isWritten;
	}

	public boolean writeStructure(String dbName, String tableName, List<String> tableColumnName,
			List<String> tableColumnType, boolean doWrite) {
		boolean isWritten = false;
		if (doWrite) {
			String fileName = basePath + dbName + filePathSeparator + "" + tableName + "@structure.txt";
			//System.out.println("fileName = " + fileName);

			try {
				PrintWriter fileWriter = new PrintWriter(new File(fileName), StandardCharsets.UTF_8);
				fileWriter.print(tableColumnName.toString().trim() + delimiter + tableColumnType.toString().trim());
				DistributedSystemRun.uploadFileData(fileName,
						tableColumnName.toString().trim() + delimiter + tableColumnType.toString().trim(), false);
				fileWriter.close();
				isWritten = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isWritten;
	}

	public boolean writeKey(String dbName, String tableName, List<String> primaryKeys, List<String> foreignKeys,
			List<String> foreignRefKeys, boolean doWrite) {
		boolean isWritten = false;
		if (doWrite) {
			String fileName = basePath + dbName + filePathSeparator + "" + tableName + "@key.txt";
			//System.out.println("fileName = " + fileName);

			try {
				PrintWriter fileWriter = new PrintWriter(new File(fileName), StandardCharsets.UTF_8);
				fileWriter.print(primaryKeys.toString().trim() + delimiter + foreignKeys.toString().trim() + delimiter
						+ foreignRefKeys.toString().trim());
				DistributedSystemRun.uploadFileData(fileName, primaryKeys.toString().trim() + delimiter
						+ foreignKeys.toString().trim() + delimiter + foreignRefKeys.toString().trim(), false);
				fileWriter.close();
				fileWriter.close();
				isWritten = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isWritten;
	}

	public boolean writeMetaData(String dbName) {
		boolean isWritten = false;
		String fileName = basePath + "@globalMetaData.txt";
		try {
			File file = new File(fileName);
			if (file.exists()) {
				FileWriter fileWriter = new FileWriter(file, true);
				fileWriter.write(dbName + delimiter);
				DistributedSystemRun.uploadFileData(fileName, dbName + delimiter, true);
				fileWriter.close();
			}
			// create new file
			else {
				PrintWriter printWriter = new PrintWriter(file, StandardCharsets.UTF_8);
				printWriter.print(dbName + delimiter);
				DistributedSystemRun.uploadFileData(fileName, dbName + delimiter, false);
				printWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isWritten;
	}

}
