package ExportData;

import Query.GlobalConfig;
import Query.Process.Common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportDataRun {

    private Common common = new Common();

    private GlobalConfig globalConfig = new GlobalConfig();
    private String basePath = globalConfig.getBasePath();
    private String filePathSeparator = globalConfig.getPathSeparator();
    private String rowDelimiter = globalConfig.getRowDelimiter();
    private String delimiter = globalConfig.getDelimiter();

    public void generateDump(String databaseName){

        if(common.databaseCheck(databaseName)){

            String dataDumpFile = basePath + filePathSeparator + databaseName + "@dump.sql";

            List<String> tableList = common.getTablesList(databaseName);

            try {
                FileWriter fileWriter = new FileWriter(dataDumpFile,false);
                fileWriter.write("\nCREATE DATABASE " + databaseName + ";");
                fileWriter.write("\n");

                for(String table:tableList){

                    String createTableQuery = "CREATE TABLE "+table+" ( ";

                    List<String[]> structure = common.getStructure(table);

                    for(int i = 0; i < structure.get(0).length; i++){
                        String columnName = structure.get(0)[i];
                        String columnType = structure.get(1)[i];
                        createTableQuery += columnName+" "+columnType+",";
                    }

                    String primaryKey = common.getPrimaryKey(table);
                    if(primaryKey != null){
                        createTableQuery += "PRIMARY KEY ("+primaryKey+"),";
                    }

                    List<String[]> foreignKeysList = common.getForeignKeys(table);
                    System.out.println("foreignKeysList = " + foreignKeysList.size());

                    // TODO make good file if table doesn't have the keys see trial2@dump.sql
                    if(foreignKeysList.size() > 0){
                        for(int i = 0; i < foreignKeysList.get(0).length; i++){
                            String foreignKey = foreignKeysList.get(0)[i];
                            String[] foreignRefKeys = foreignKeysList.get(1)[i].split("[.]");
                            String foreignRefKeyTable = foreignRefKeys[0];
                            String foreignRefKeyColumn = foreignRefKeys[1];
                            createTableQuery += "FOREIGN KEY ("+foreignKey+") REFERENCES "+foreignRefKeyTable+" ("+foreignRefKeyColumn+"),";
                        }
                    }

                    createTableQuery = createTableQuery.substring(0,createTableQuery.length()-1);
                    createTableQuery += ");";

                    fileWriter.write(createTableQuery);
                    fileWriter.write("\n");

                    List<String[]> rowData = common.getData(table);

                    if(rowData.size() > 0) {

                        for (String[] row : rowData) {
                            String insertQuery = "INSERT INTO " + table + " VALUES (";
                            for (String columnValue : row) {
                                insertQuery += columnValue + ",";
                            }
                            insertQuery = insertQuery.substring(0, insertQuery.length() - 1);
                            insertQuery += ");";

                            fileWriter.write(insertQuery);
                            fileWriter.write("\n");
                        }
                    }

                    fileWriter.write("\n\n");

                }
                fileWriter.close();

                File file = new File(dataDumpFile);
                System.out.println("File successfully written at " + file.getAbsolutePath());


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else{
            System.out.println(databaseName+" database not found/exits.");
        }

    }

}
