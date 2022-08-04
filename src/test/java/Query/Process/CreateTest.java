package Query.Process;

import Query.QueryCheck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateTest {

    private QueryCheck queryParse = new QueryCheck();

    private String createValidDB1 = "CREATE DATABASE IF NOT EXISTS Trial1;";
    private String createValidDB2 = "CREATE DATABASE trial1;";
    private String createInvalidDB1 = "CREATE DATABASE IF NOT EXISTS Trial1 trial2;";
    private String createInvalidDB2 = "CREATE DATABASE trial1 trial2;";
    private String createInvalidDB3 = "CREATE DATABASE  ;";

    private String createValidTable1 = "CREATE TABLE todo2  ( todo_id INT, todo VARCHAR, is_completed BOOLEAN, PRIMARY KEY (todo_id), FOREIGN KEY (task_id) REFERENCES tasks (task_id) ) ;";
    private String createValidTable2 = "CREATE TABLE IF NOT EXISTS todo1  ( todo_id INT, todo VARCHAR, is_completed BOOLEAN, PRIMARY KEY (todo_id), FOREIGN KEY (task_id) REFERENCES tasks (task_id), FOREIGN KEY (task_id) REFERENCES tasks (task_id)) ;";
    private String createInvalidTable5 = "CREATE TABLE todo  ( todo_id INT, todo VARCHAR, is_completed BOOLEAN, PRIMARY KEY (todo_id), FOREIGN KEY (task_id) REFERENCES tasks (task_id) ) ;";
    private String createInvalidTable1 = "CREATE TABLE todo3  (( todo_id INT, todo VARCHAR, is_completed BOOLEAN, PRIMARY KEY todo_id, FOREIGN KEY (task_id) REFERENCES tasks (task_id) ) ;";
    private String createInvalidTable2 = "CREATE TABLE tod  ( todo_id INT, todo VARCHAR, is_completed FLOAT, PRIMARY KEY todo_id, FOREIGN KEY (task_id) REFERENCES tasks (task_id) );";
    private String createInvalidTable3 = "CREATE TABLE tod  ( todo_id INT, todo VARCHAR, is_completed BOOLEAN, PRIMARY KEY todo_id, FOREIGN KEY (task_id) REFERENCES tasks (task_id) ) ";
    private String createInvalidTable4 = "CREATE TABLE  (( todo_id INT, todo VARCHAR, is_completed BOOLEAN, PRIMARY KEY todo_id, FOREIGN KEY (task_id) REFERENCES tasks (task_id) ) ";

    @Test
    public void createDBTest(){
        assertEquals(true, queryParse.queryCheck(createValidDB1, true));
        System.out.println();
        assertEquals(true, queryParse.queryCheck(createValidDB2,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(createInvalidDB1,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(createInvalidDB2,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(createInvalidDB3,true));
    }

    @Test
    public void createTableTest(){
        queryParse.queryCheck("USE trial1;",true);
        // TODO first delete all the files todo2.txt todo2@key.txt todo2@structure.txt
        System.out.println("New table-----------------------------");
        assertEquals(true, queryParse.queryCheck(createValidTable1,true));
        // TODO first delete all the files todo1.txt todo1@key.txt todo1@structure.txt
        System.out.println("New table check------------------------------");
        assertEquals(true, queryParse.queryCheck(createValidTable2,true));
        System.out.println("Table already exists test--------------------------");
        assertEquals(false, queryParse.queryCheck(createInvalidTable5,true));
        System.out.println("Structure not valid---------------------------");
        assertEquals(false, queryParse.queryCheck(createInvalidTable1,true));
        System.out.println("Structure not valid-------------------------");
        assertEquals(false, queryParse.queryCheck(createInvalidTable2,true));
        System.out.println("last semicolon missing --------------------");
        assertEquals(false, queryParse.queryCheck(createInvalidTable3,true));
        System.out.println("tablename missing-----------------------");
        assertEquals(false, queryParse.queryCheck(createInvalidTable4,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(createInvalidDB3,true));
    }

}