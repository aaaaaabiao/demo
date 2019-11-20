package site.abiao.demo.tool;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class JsonPathTest {

    public static void parseTest() throws IOException {
        String content = FileUtils.readFileToString(new File("/Users/hubiao/java/demo/src/main/resources/test.json"),"utf-8");
        JSONObject questionJsonObject = (JSONObject) JSONPath.extract(content, "$..entities..questions[0]");
        JSONObject answerJsonObject = (JSONObject) JSONPath.extract(content, "$..entities..answers[0]");

        String title = JSONPath.eval(questionJsonObject, "$..title[0]").toString();
        String desc = JSONPath.eval(questionJsonObject, "$..detail[0]").toString();

        String answer = JSONPath.eval(answerJsonObject, "$..content[0]").toString();
        System.out.println(title);
    }

    public static void main(String[] args) throws IOException {
        parseTest();
    }
}
