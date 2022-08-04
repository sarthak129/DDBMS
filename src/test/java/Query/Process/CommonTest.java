package Query.Process;

import Query.QueryCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommonTest {

    Common common = new Common();

    QueryCheck queryParse = new QueryCheck();
    
    @Test
    public void metadataTests(){

        queryParse.queryCheck("USE trial1;", true);
        
        String tableName = "todo";
        
        System.out.println("common.getPrimaryKey(tableName) = " + common.getPrimaryKey(tableName).toString());
        System.out.println("common.getStructure(tableName) = " + common.getStructure(tableName).toString());
        System.out.println("common.getForeignKeys(tableName) = " + common.getForeignKeys(tableName).toString());
        System.out.println("common.getPrimaryKeyList(tableName).toString() = " + common.getPrimaryKeyList(tableName).toString());

        System.out.println("common.getColumnNames(tableName).toString() = " + common.getColumnNames(tableName).toString());
        List<String[]> rowData = common.getData(tableName);

        for(String[] row:rowData){
            System.out.println("Arrays.toString(row).toString() = " + Arrays.toString(row).toString());
        }

        System.out.println("common.getSelectedRows(tableName,1,>,\"homework\") = " + common.getSelectedRows(tableName,0,">","4"));
        System.out.println("common.getSelectedRows(tableName,1,>=,\"homework\") = " + common.getSelectedRows(tableName,0,"<","3"));
    }
    

}
