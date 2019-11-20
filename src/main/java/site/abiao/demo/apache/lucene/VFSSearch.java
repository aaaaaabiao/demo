package site.abiao.demo.apache.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.abiao.demo.apache.lucene.analyzer.IKAnalyzer4Lucene7;
import site.abiao.demo.apache.lucene.datas.Contants;
import site.abiao.demo.apache.lucene.directory.vfs.VFSDirectory;

import java.util.Scanner;

/**
 * 根据索引搜索
 * TODO
 * @author Snaiclimb
 * @date 2018年3月25日
 * @version 1.8
 */

@Slf4j
public class VFSSearch {
    Logger logger = LoggerFactory.getLogger(VFSSearch.class);
    public static void search(String indexUri, String q) throws Exception {


        // 得到读取索引文件的路径
        Directory dir = new VFSDirectory(indexUri);
        // 通过dir得到的路径下的所有的文件
        IndexReader reader = DirectoryReader.open(dir);
        // 建立索引查询器
        IndexSearcher is = new IndexSearcher(reader);
        // 实例化分析器
        Analyzer analyzer = new IKAnalyzer4Lucene7();
        // 建立查询解析器
        /**
         * 第一个参数是要查询的字段； 第二个参数是分析器Analyzer
         */
        QueryParser parser = new QueryParser("contents", analyzer);
        // 根据传进来的p查找
        Query query = parser.parse(q);
        // 计算索引开始时间
        long start = System.currentTimeMillis();
        // 开始查询
        /**
         * 第一个参数是通过传过来的参数来查找得到的query； 第二个参数是要出查询的行数
         */
        TopDocs hits = is.search(query, 10);
        // 计算索引结束时间
        long end = System.currentTimeMillis();
        System.out.println("匹配 " + q + " ，总共花费" + (end - start) + "毫秒" + "查询到" + hits.totalHits + "个记录");
        // 遍历hits.scoreDocs，得到scoreDoc


        QueryScorer contentScorer = new QueryScorer(query, "contents");
        SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<em>", "</em>");
        Highlighter contentHighlighter = new Highlighter(htmlFormatter, contentScorer);
        contentHighlighter.setTextFragmenter(new SimpleSpanFragmenter(contentScorer));

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            String fullPath = doc.get("fullPath");
            String content = doc.get("contents");
            String highLighterContent = contentHighlighter.getBestFragment(analyzer, "contents", content);
            log.info("\n得分为:{}\n文件路径为:{}\n内容为:{}", scoreDoc.score,fullPath,highLighterContent);

        }

        // 关闭reader
        reader.close();
    }

    public static void main(String[] args) {
        String indexDir = String.format("ftp://%s:%s@%s%s",
                Contants.username , Contants.password, Contants.domain, "/test");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String q = scanner.next();
            log.info("query:{}", q);
            if (!"exit".equals(q)) {
                try {
                    search(indexDir, q);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                break;
            }
        }
    }
}

