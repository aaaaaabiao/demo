package site.abiao.demo.apache.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import site.abiao.demo.apache.lucene.analyzer.IKAnalyzer4Lucene7;
import site.abiao.demo.apache.lucene.datas.Contants;
import site.abiao.demo.apache.lucene.directory.hdfs.HdfsDirectory;
import site.abiao.demo.apache.lucene.directory.vfs.VFSDirectory;


/**
 * TODO  索引文件
 *
 * @author Snaiclimb
 * @version 1.8
 * @date 2018年3月30日
 */
@Slf4j
public class Indexer {


    // 写索引实例
    private IndexWriter writer;

    /**
     * 构造方法 实例化IndexWriter
     *
     *
     * @throws IOException
     */
    public Indexer(String dirType) throws IOException {
        Directory directory = null;

        switch (dirType) {
            case "mmap":
                directory = FSDirectory.open(Paths.get(Contants.indexDir));
                break;
            case "hdfs":
                Configuration config = new Configuration();
                config.set("dfs.client.use.datanode.hostname","true");
                directory = new HdfsDirectory(config, Contants.hdfsDir);
            break;
            case "ftp":
                directory = new VFSDirectory(Contants.ftpDir);
                break;

        }
        //得到索引所在目录的路径

        // 标准分词器
        Analyzer analyzer = new IKAnalyzer4Lucene7();
        //保存用于创建IndexWriter的所有配置。
        IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
        //实例化IndexWriter
        writer = new IndexWriter(directory, iwConfig);
    }

    /**
     * 关闭写索引
     *
     * @return 索引了多少个文件
     * @throws Exception
     */
    public void close() throws IOException {
        writer.close();
    }

    /**
     * 索引文件夹下的文件
     *
     * @param
     * @param
     * @param
     * @return
     */
    public int index(String dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            //索引指定文件
            indexFile(file);
        }
        //返回索引了多少个文件
        return writer.numDocs();

    }

    /**
     * 索引指定文件
     *
     * @param f
     */
    private void indexFile(File f) throws Exception {
        //输出索引文件的路径
        System.out.println("索引文件：" + f.getCanonicalPath());
        //获取文档，文档里再设置每个字段
        Document doc = getDocument(f);
        //开始写入,就是把文档写进了索引文件里去了；
        writer.addDocument(doc);
    }

    /**
     * 获取文档，文档里再设置每个字段
     *
     * @param f
     * @return document
     */
    private Document getDocument(File f) throws Exception {
        Document doc = new Document();
        //把设置好的索引加到Document里，以便在确定被索引文档
        doc.add(new Field("contents", FileUtils.readFileToString(f), TextField.TYPE_STORED));
        //Field.Store.YES：把文件名存索引文件里，为NO就说明不需要加到索引文件里去
        doc.add(new TextField("fileName", f.getName(), Field.Store.YES));
        //把完整路径存在索引文件里
        doc.add(new TextField("fullPath", f.getCanonicalPath(), Field.Store.YES));
        return doc;
    }


    public void createIndex(String dataDir) {
        int numIndexed = 0;
        //索引开始时间
        long start = System.currentTimeMillis();
        try {
            numIndexed = index(dataDir);
        } catch (Exception e) {
            log.info("创建索引异常", e);
        }
        //索引结束时间
        long end = System.currentTimeMillis();
        System.out.println("索引：" + numIndexed + " 个文件 花费了" + (end - start) + " 毫秒");
    }


    /**
     * 清空索引
     *
     * @param
     */
    public void clearIndex() {

        if (writer == null) {
            log.warn("获取到IndexWriter对象为空，停止清空索引操作");
            return;
        }
        log.info("开始清空索引");
        try {
            writer.deleteAll();
        } catch (IOException e) {
            log.error("清除索引异常", e);
        }
        log.info("清空索引结束");
    }

    public void getStatus() {
        log.info("当文档的个数为{}", writer.numDocs());
    }

    public static void main(String[] args) throws IOException {
        String dataDir = "/Users/hubiao/java/data/lucene/doc";
        Indexer indexer = null;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String type = scanner.next();
                indexer = new Indexer(type);
                String cmd = scanner.next();
                switch (cmd) {
                    case "c":
                        indexer.createIndex(dataDir);
                        break;
                    case "d":
                        indexer.clearIndex();
                        break;
                    default:
                        break;
                }
                indexer.getStatus();
            } finally {
                indexer.close();
            }
        }
    }
}


