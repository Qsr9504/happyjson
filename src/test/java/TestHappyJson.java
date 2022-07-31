import com.qsr.HappyJson;
import org.testng.annotations.Test;

/**
 * @Description:
 * @Author: Joe Throne
 * @CreatedTime: 2022-07-31 22:50
 * TODO:
 */

@Test
public class TestHappyJson {

    public void testHappyJson(){
//        String str = "{\"action\":{\"content\":\"{\\\"staffTopic\\\":\\\"usrk0yX,Q7KO4MM\\\",\\\"agentStatus\\\":2,\\\"userId\\\":10119168}\",\"topic\":\"usrk,0yXQ7KO4MM\",\"id\":\"86248\",\"ct\":2018,\"code\":\"CHANGE_STATUS\",\"common\":\"{\\\"msgFrom\\\":1}\",\"clientSendTimestamp\":1659180837319,\"msgid\":\"14ec79dd-47c7-4569-bf7a-ed2be007dabe\",\"domain\":0}}";
        String str = "{ \n" +
                "\t\t\"name\":\"名称demo\", \n" +
                "\t\t\"info\":[\"一\",\"二\",{ \"name\":\"名称2\", \"info\":\"简介2\" }]\n" +
                "\t}\n";
        System.out.println(HappyJson.happyGetOnlyOne(str, "info",true));
    }
}
