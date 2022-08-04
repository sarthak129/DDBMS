package Query.Process;

import Query.GlobalConfig;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Update {

    private Common common = new Common();
    private GlobalConfig globalConfig = new GlobalConfig();
    private String basePath = globalConfig.getBasePath();
    private String filePathSeparator = globalConfig.getPathSeparator();
    private String delimiter = globalConfig.getDelimiter();
    private String rowDelimiter = globalConfig.getRowDelimiter();

    public boolean check(String queryString, boolean doWrite) {

        boolean isValidQuery = false;

        String currentDatabase;

        queryString = queryString.toLowerCase().trim();

        String updateParseRegex = "(?:UPDATE)\\s+(?<table>\\w+)\\s+(?:SET)\\s+(?<updateColumn>\\w+)\\s+(?:=)\\s+(?:\\\"(?<updateValue>.+)\\\")\\s+(?:WHERE)\\s+(?<conditionColumn>\\w+)\\s+(?<condition>==|<|>|<=|>=|!=){1}\\s+(?:\\\"(?<conditionValue>.+)\\\");";

        Pattern updateParsePattern = Pattern.compile(updateParseRegex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

        //System.out.println("queryString = " + queryString);

        Matcher updateParseMatcher = updateParsePattern.matcher(queryString);


        //System.out.println("Update detected");
        currentDatabase = globalConfig.getGlobalDatabase();

        if (currentDatabase != null) {
           // System.out.println(updateParseMatcher.results().count()+" match found");
            updateParseMatcher.reset();
            if (updateParseMatcher.find()) {
                String tableName = updateParseMatcher.group("table").trim();
               // System.out.println("tableName = " + tableName);
                if (common.tableCheck(tableName)) {
                    String primaryKeyName = common.getPrimaryKey(tableName);

                    String updateColumnName = updateParseMatcher.group("updateColumn").trim();
                    String updateColumnValue = updateParseMatcher.group("updateValue").trim();
                    String conditionColumnName = updateParseMatcher.group("conditionColumn").trim();
                    String condition = updateParseMatcher.group("condition");
                    String conditionValue = updateParseMatcher.group("conditionValue").trim();

//                    System.out.println("updateColumnName = " + updateColumnName);
//                    System.out.println("updateColumnValue = " + updateColumnValue);
//                    System.out.println("conditionColumnName = " + conditionColumnName);
//                    System.out.println("condition = " + condition);
//                    System.out.println("conditionValue = " + conditionValue);
                    
                    List<String> columnNamesList = common.getColumnNames(tableName);

                    List<Object> primaryKeyList = common.getPrimaryKeyList(tableName);

                    if (columnNamesList.contains(updateColumnName) && columnNamesList.contains(conditionColumnName)) {
                       // System.out.println("columnNames are valid");
                        List<String[]> rowData = common.getData(tableName);
                        int updateColumnIndex = columnNamesList.indexOf(updateColumnName);
                        int conditionColumnIndex = columnNamesList.indexOf(conditionColumnName);

                        List<Integer> indexToUpdateList = common.getSelectedRows(tableName, conditionColumnIndex, condition, conditionValue);

                       // System.out.println("indexToUpdateList.size() = " + indexToUpdateList.size());
                      //  System.out.println("indexToUpdateList = " + indexToUpdateList);
                        
                        if (indexToUpdateList == null) {
                            System.out.println("Invalid operator for data type provided");
                            isValidQuery = false;
                        }
                        else if(indexToUpdateList.size() == 0){
                            System.out.println("No content to update");
                            isValidQuery = true;
                        }
                        else {
                            int updatedRowCount = 0;
                            if (primaryKeyName.equals(updateColumnName)) {
                                if (indexToUpdateList.size() == 1 && !primaryKeyList.contains(updateColumnValue)) {
                                    String[] oldRow = rowData.get(indexToUpdateList.get(0));
                                    String[] newRow = Arrays.copyOf(oldRow,oldRow.length);
                                   // System.out.println("newRow = " + Arrays.toString(oldRow));
                                    if (newRow[updateColumnIndex].equals(updateColumnValue)) {
                                        System.out.println("No changes");
                                    } else {
                                        newRow[updateColumnIndex] = updateColumnValue;
                                      //  System.out.println("oldRow = " + Arrays.toString(oldRow));
                                      //  System.out.println("newRow = " + Arrays.toString(newRow));
                                        updatedRowCount += common.updateDataRow(tableName, oldRow, newRow,doWrite);
                                       // System.out.println("updatedRowCount = " + updatedRowCount);
                                    }
                                    isValidQuery = true;
                                } else {
                                    System.out.println("Can't update primary key id with same values");
                                    isValidQuery = false;
                                }
                            } else {
                                for (int index : indexToUpdateList) {
                                    String[] oldRow = rowData.get(index);
                                    String[] newRow = Arrays.copyOf(oldRow,oldRow.length);
                                   // System.out.println("Arrays.toString(newRow) = " + Arrays.toString(oldRow));
                                    if (newRow[updateColumnIndex].equals(updateColumnValue)) {
                                        System.out.println("No changes");
                                    } else {
                                        newRow[updateColumnIndex] = updateColumnValue;
                                       // System.out.println("oldRow = " + Arrays.toString(oldRow));
                                      //  System.out.println("Arrays.toString(newRow) = " + Arrays.toString(newRow));
                                        updatedRowCount += common.updateDataRow(tableName, oldRow, newRow,doWrite);
                                       // System.out.println("updatedRowCount = " + updatedRowCount);
                                    }
                                    isValidQuery = true;
                                }
                            }

                            System.out.println(updatedRowCount + " rows updated");
                        }

                    } else {
                        System.out.println("Invalid column names");
                        isValidQuery = false;
                    }


                } else {
                    System.out.println(tableName + " table not found");
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
