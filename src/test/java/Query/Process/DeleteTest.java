package Query.Process;

import Query.QueryCheck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteTest {

    QueryCheck queryParse = new QueryCheck();

    private String deleteValid1 = "delete from todo1 where todo == \"homework\";";
    private String deleteValid2 = "delete from todo2 where todo == \"homework\";";
    private String deleteValid3 = "delete from todo2 where todo == \"make lunch\";";

    private String deleteInvalid1 = "UPDATE todo SET is_completed = \"true\" WHERE todo_id == \"6\";";
    private String deleteInvalid2 = "UPDATE todo SET todo_id = \"6\" WHERE todo == \"homework\";";
    private String deleteInvalid3 = "UPDATE todo SET todo_id = \"3\" WHERE todo == \"homework\";";
    private String deleteInvalid4 = "UPDATE todo SET is_completed = \"true\" WHERE todo_id == \"6\";";

    @Test
    public void deleteValidTest1(){
        queryParse.queryCheck("USE TRIAL1;",true);

        System.out.println("Valid Update query---------------");
        assertEquals(true,queryParse.queryCheck(deleteValid1,true));
    }

    @Test
    public void deleteValidTest2(){
        queryParse.queryCheck("USE TRIAL1;",true);

        System.out.println("Valid Update query---------------");
        assertEquals(true,queryParse.queryCheck(deleteValid2,true));
    }

    @Test
    public void deleteValidTest3(){
        queryParse.queryCheck("USE TRIAL1;",true);

        System.out.println("Valid Update query---------------");
        assertEquals(true,queryParse.queryCheck(deleteValid3,true));
    }
}
