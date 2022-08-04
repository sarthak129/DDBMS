package Query.Process;

import Query.GlobalConfig;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Delete {

    private Common common = new Common();
    private GlobalConfig globalConfig = new GlobalConfig();
    private String basePath = globalConfig.getBasePath();
    private String filePathSeparator = globalConfig.getPathSeparator();
    private String delimiter = globalConfig.getDelimiter();
    private String rowDelimiter = globalConfig.getRowDelimiter();

    public boolean check(String queryString, boolean doWrite) {

        boolean isValidQuery = false;

        queryString = queryString.toLowerCase().trim();

        String deleteParseRegex = "(?:delete)\\s+(?:from)\\s+(?<table>\\w+)\\s+(?:where)\\s+(?<conditionColumn>\\w+)\\s*(?<condition>==|<|>|<=|>=|!=){1}\\s*(?:\\\"(?<conditionValue>.+)\\\");";

        Pattern deleteParsePattern = Pattern.compile(deleteParseRegex,Pattern.MULTILINE|Pattern.CASE_INSENSITIVE);

        Matcher deleteParseMatcher = deleteParsePattern.matcher(queryString);

        String currentDatabase;

        currentDatabase = globalConfig.getGlobalDatabase();
        if (currentDatabase != null) {
            if (deleteParseMatcher.find()) {
                String tableName = deleteParseMatcher.group("table").trim();
               // System.out.println("tableName = " + tableName);
                if (common.tableCheck(tableName)) {
                    String primaryKeyName = common.getPrimaryKey(tableName);

                    String conditionColumnName = deleteParseMatcher.group("conditionColumn").trim();
                    String condition = deleteParseMatcher.group("condition");
                    String conditionValue = deleteParseMatcher.group("conditionValue").trim();

                    List<String> columnNamesList = common.getColumnNames(tableName);

                    List<Object> primaryKeyList = common.getPrimaryKeyList(tableName);

                    if (columnNamesList.contains(conditionColumnName)) {
                       // System.out.println("Column name is valid");

                        List<String[]> rowData = common.getData(tableName);
                        int conditionColumnIndex = columnNamesList.indexOf(conditionColumnName);

                        List<Integer> indexToDeleteList = common.getSelectedRows(tableName, conditionColumnIndex, condition, conditionValue);

                      //  System.out.println("indexToDeleteList.size() = " + indexToDeleteList.size());
                      //  System.out.println("indexToDeleteList = " + indexToDeleteList);

                        if (indexToDeleteList == null) {
                            System.out.println("Invalid operator for data type provided");
                            isValidQuery = false;
                        } else if (indexToDeleteList.size() == 0) {
                            System.out.println("No content to delete");
                            isValidQuery = true;
                        } else {
                            int deletedRowCount = 0;
                            for (int index : indexToDeleteList) {
                                String[] oldRow = rowData.get(index);
                               // System.out.println("oldRow = " + Arrays.toString(oldRow));
                                deletedRowCount += common.updateDataRow(tableName, oldRow, null,doWrite);
                                
                            }
                            deletedRowCount = indexToDeleteList.size();
                            System.out.println(deletedRowCount + " row deleted");
                            isValidQuery = true;
                        }

                    } else {
                        System.out.println("Given condition column not found");
                        isValidQuery = false;
                    }
                } else {
                    System.out.println(tableName + " table not found");
                    isValidQuery = false;
                }
            }
        } else {
            System.out.println("Please set default schema/database first");
            System.out.println("enter command : \n use <database_name>;");
            isValidQuery = false;
        }

        return isValidQuery;

    }
}
