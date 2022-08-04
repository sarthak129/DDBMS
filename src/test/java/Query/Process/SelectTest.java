package Query.Process;

import Query.QueryCheck;
import org.junit.jupiter.api.Test;

public class SelectTest {

    private QueryCheck queryParse = new QueryCheck();

    private String selectValid1 = "SELECT * FROM todo;";
    private String selectValid2 = "SELECT todo_id FROM todo;";
    private String selectValid3 = "SELECT todo_id,todo FROM todo;";
    private String selectValid4 = "SELECT * FROM todo WHERE todo_id == \"6\";";
    private String selectValid5 = "SELECT todo FROM todo WHERE todo_id == \"6\";";
    private String selectValid6 = "SELECT todo_id,todo,is_completed FROM todo WHERE todo_id <= \"6\";";
    private String selectValid7 = "SELECT todo_id,todo,is_completed FROM todo WHERE todo_id > \"6\";";

    private String selectInvalid1 = "";

    @Test
    public void setSelectValid1(){
        queryParse.queryCheck("USE trial1;",true);

        queryParse.queryCheck(selectValid1,true);
        queryParse.queryCheck(selectValid2,true);
        queryParse.queryCheck(selectValid3,true);
        queryParse.queryCheck(selectValid4,true);
        queryParse.queryCheck(selectValid5,true);
        queryParse.queryCheck(selectValid6,true);
        queryParse.queryCheck(selectValid7,true);

        queryParse.queryCheck(selectInvalid1,true);
    }

}
