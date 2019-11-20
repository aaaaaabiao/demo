package site.abiao.demo.apache.lucene.datas;

public class Contants {
    //Ftp服务连接信息
    public static String username = "test";
    public static String password = "test";
    public static String domain = "10.28.132.48";


    public static final String indexDir = "/Users/hubiao/java/data/lucene/index";
    public static final String hdfsDir = "hdfs://10.28.132.48:9000/test";
    public static final String ftpDir = String.format("ftp://%s:%s@%s%s",
            Contants.username , Contants.password, Contants.domain, "/test");
}
