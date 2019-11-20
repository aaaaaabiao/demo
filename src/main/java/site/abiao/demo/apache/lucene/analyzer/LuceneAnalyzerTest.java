package site.abiao.demo.apache.lucene.analyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
@Slf4j
public class LuceneAnalyzerTest {

    private static void doToken(TokenStream ts) throws IOException {
        ts.reset();
        CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
        while (ts.incrementToken()) {
            System.out.print(cta.toString() + "|");
        }
        System.out.println();
        ts.end();
        ts.close();
    }


    public static void analyzer(Analyzer ana) {
        String etext = "Analysis is one of the main causes of slow indexing. Simply put, the more you analyze the slower analyze the indexing (in most cases).";
        String chineseText = "张三说的确实在理。";
        // Lucene core模块中的 StandardAnalyzer 英文分词器
        try {
            TokenStream ts = ana.tokenStream("content", etext);
            System.out.println("英文分词效果：");
            doToken(ts);
            ts = ana.tokenStream("content", chineseText);
            System.out.println("中文分词效果：");
            doToken(ts);
        } catch (IOException e) {

        }
    }

    public static void analyzer(Analyzer ana, String content) {
        // Lucene core模块中的 StandardAnalyzer 英文分词器
        try {
            TokenStream ts = ana.tokenStream("content", content);
            System.out.println("分词效果：");
            doToken(ts);
        } catch (IOException e) {

        }
    }




    public static void main(String[] args) {

        String content = "厉害了我的国一经播出，受到各方好评，强烈激发了国人的爱国之情、自豪感！";
//        System.out.println("标准分词器分词：");
//        analyzer(new StandardAnalyzer());
//
//        System.out.println("smart中文分词器分词：");
//        analyzer(new SmartChineseAnalyzer());
//
//        System.out.println("IK分词器(细粒)分词器：");
//        //细粒切分
//        analyzer(new IKAnalyzer4Lucene7());
//
//        System.out.println("IK分词器(智能)分词器：");
//        //智能切分
//        analyzer(new IKAnalyzer4Lucene7(true));

        System.out.println("IK分词器(细粒)分词器：");
        analyzer(new IKAnalyzer4Lucene7(), content);

        System.out.println("IK分词器(智能)分词器：");
        analyzer(new IKAnalyzer4Lucene7(true), content);
    }
}
