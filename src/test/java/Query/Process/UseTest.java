package Query.Process;

import Query.QueryCheck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UseTest {

    QueryCheck queryParse = new QueryCheck();

    String useValidDB1 = "USE trial2;";
    String useValidDB2 = "USe  trial1;";
    String useValidDB3 = "USe  trial1;";
    String useInvalidDB1 = "USE trial66;";
    String useInvalidDB2 = "USE  ;";
    String useInvalidDB3 = "USE ";

    @Test
    public void useDBTest(){
        assertEquals(true, queryParse.queryCheck(useValidDB1,true));
        System.out.println();
        assertEquals(true, queryParse.queryCheck(useValidDB2,true));
        System.out.println();
        assertEquals(true, queryParse.queryCheck(useValidDB3,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(useInvalidDB1,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(useInvalidDB2,true));
        System.out.println();
        assertEquals(false, queryParse.queryCheck(useInvalidDB3,true));
    }
}
