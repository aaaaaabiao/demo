package site.abiao.demo.tool;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class JsoupTest {

    public static void test() throws IOException {
        String content = FileUtils.readFileToString(new File("/Users/hubiao/java/demo/src/main/resources/jsoup.html"), "UTF-8");
        Document doc = Jsoup.parse(content);
        doc.select("div[class=\"wgt-best-mask\"]").remove();
//        Element e = doc.select("div[accuse = \"aContent\"]").get(0);
        Element e = doc.select("div[accuse=\"aContent\"]").get(0);
        System.out.println(e.html());


    }

    public static void main(String[] args) throws IOException {
        test();
    }
}
