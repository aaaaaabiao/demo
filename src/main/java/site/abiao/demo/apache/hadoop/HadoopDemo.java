package site.abiao.demo.apache.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class HadoopDemo {

    public static FileSystem getHadoopFileSystem() {
        FileSystem fs = null;
        Configuration conf = null;
        //此时的conf不需任何设置，只需读取远程的配置文件即可
        conf = new Configuration();
        conf.set("dfs.client.use.datanode.hostname","true");
        // Hadoop的用户名，master机器的登录用户
        String hdfsUserName = "root";

        URI hdfsUri = null;
        try {
            // HDFS的访问路径
            hdfsUri = new URI("hdfs://10.28.132.48:9000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            // 根据远程的NN节点，获取配置信息，创建HDFS对象
            fs = FileSystem.get(hdfsUri,conf,hdfsUserName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return fs;
    }

    public static void main(String[] args){
        FileSystem fs = getHadoopFileSystem();
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                String opt = sc.next();
                String path = "";
                switch (opt) {
                    case "c":
                        path = sc.next();
                        createDir(fs, path);
                        break;
                    case "d":
                        path = sc.next();
                        deleteDir(fs, path);
                        break;
                    case "ls":
                        path = sc.next();
                        iteratorPath(fs, path);
                        break;
                    case "cp":
                        String remote = sc.next();
                        String local = sc.next();
                        copyLocalFileToHdfs(fs, remote, local);
                        break;
                }
            }
        }catch (Exception e){
            log.error("hdfs error,{}",e);
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 创建目录
     * @param fs
     * @return
     */
    public static void createDir(FileSystem fs, String dir){
        boolean b = false;
        Path path = new Path(dir);
        try {
            // even the path exist,it can also create the path.
            fs.mkdirs(path);
            log.info("mkdir success");
        } catch (IOException e) {
            log.error("mkdir error,{}",e);
        }
    }

    /**
     * 删除path，参数true相当于rm -r
     * @param fs
     * @return
     */
    public static void deleteDir(FileSystem fs, String dir){
        boolean b = false;
        Path path = new Path(dir);
        try {
            // even the path exist,it can also create the path.
            fs.delete(path,true);
            log.info("delete dir success");
        } catch (IOException e) {
            log.error("delete error,{}",e);
        }
    }

    /**
     * 重命名
     * @param fs
     * @return
     */
    public static void renamePath(FileSystem fs, String oldPathStr, String newPathStr){
        boolean b = false;
        Path oldPath = new Path(oldPathStr);
        Path newPath = new Path(newPathStr);
        try {
            // even the path exist,it can also create the path.
            fs.rename(oldPath,newPath);
            log.info("rename path success");
        } catch (IOException e) {
            log.error("rename error,{}",e);
        }
    }

    /**
     * 遍历文件夹及子文件
     * @param hdfs
     * @return
     */
    public static void iteratorPath(FileSystem hdfs,String path){
        FileStatus[] files;
        Path listPath = new Path(path);
        try {
            files = hdfs.listStatus(listPath);
            // 实际上并不是每个文件夹都会有文件的。
            if(files.length == 0){
                // 如果不使用toUri()，获取的路径带URL。
                log.info("==>root dir:{}",listPath.toUri().getPath());
            }else {
                // 判断是否为文件
                for (FileStatus f : files) {
                    if (files.length == 0 || f.isFile()) {
                        log.info("==>file:{}",f.getPath().toUri().getPath());
                    } else {
                        // 是文件夹，且非空，就继续遍历
                        iteratorPath(hdfs, f.getPath().toString());
                    }
                }
            }
        } catch (IOException e) {
            log.error("iteratorPath error,{}",e);
        }
    }

    /**
     * 读取远程hadoop集群的所有配置文件信息，并以键值对打印出来
     */
    public static void showAllConf(){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.137.100:9000");
        Iterator<Map.Entry<String,String>> it = conf.iterator();
        log.info("==================================================以下是远程hadoop的配置信息==============");
        while(it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            log.info(entry.getKey()+"=" +entry.getValue());
        }
        log.info("==================================================以上是远程hadoop的配置信息==============");
    }

    /**
     * 将远程hdfs中的test/readme.txt内容读取并打印到console并输出到E
     */
    public static void printHdfsFileContent(FileSystem hdfs){
        try {
            FSDataInputStream is = hdfs.open(new Path("/test/readme.txt"));
            OutputStream os = new FileOutputStream(new File("E:/hadooptest/readme.txt"));
            byte[] buff = new byte[1024];
            int length = 0;
            log.info("远程的/test/readme.txt内容如下：=======================》");
            while ((length = is.read(buff)) != -1) {
                System.out.println(new String(buff, 0, length));
                os.write(buff, 0, length);
                os.flush();
            }
        } catch (Exception e){
            log.error("printHdfsFileContent error,{}",e);
        }
    }

    /**
     * 文件上传,将本地的E:/hadooptest/navicat.zip上传到hdfs的/aa
     * @param hdfs
     */
    public static void copyLocalFileToHdfs(FileSystem hdfs, String remote, String local){
        Path HDFSPath = new Path(remote);
        Path localPath = new Path(local);
        // 如果上传的路径不存在会创建
        // 如果该路径文件已存在，就会覆盖
        try {
            hdfs.copyFromLocalFile(localPath,HDFSPath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("uploadLocalFileToHdfs error,{}",e);
        }
    }

    /**
     * 文件下载,将hdfs中/aa/navicat.zip文件下载到E:/hadooptest/,经过测试直接使用hdfs.copyToLocalFile下载不下来，所有用文件流来下载
     * @param hdfs
     */
    public static void downloadFileFromHdfs(FileSystem hdfs){
//        Path HDFSPath = new Path("/aa/navicat.zip");
//        Path localPath = new Path("E:/hadooptest/");
//        try {
//            log.info("====================开始下载=======================");
//            hdfs.copyToLocalFile(HDFSPath,localPath);
//            log.info("====================下载结束=======================");
//        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("downloadFileFromHdfs error,{}",e);
//        }
        try {
            FSDataInputStream ifs = hdfs.open(new Path("/aa/navicat.zip"));
            OutputStream os = new FileOutputStream(new File("E:/hadooptest/navicat.zip"));
            byte[] buff = new byte[1024];
            int length = 0;
            log.info("============开始下载=======================》");
            while ((length = ifs.read(buff)) != -1) {
                os.write(buff, 0, length);
                os.flush();
            }
        } catch (Exception e){
            log.error("printHdfsFileContent error,{}",e);
        }
    }

    /**
     * 在hdfs内部之间复制文件
     * 使用FSDataInputStream来打开文件open(Path p)
     * 使用FSDataOutputStream开创建写到的路径create(Path p)
     * 使用 IOUtils.copyBytes(FSDataInputStream,FSDataOutputStream,int buffer,Boolean isClose)来进行具体的读写
     * 说明：
     *  1.java中使用缓冲区来加速读取文件，这里也使用了缓冲区，但是只要指定缓冲区大小即可，不必单独设置一个新的数组来接受
     *  2.最后一个布尔值表示是否使用完后关闭读写流。通常是false，如果不手动关会报错的
     * @param hdfs
     */
    public static void copyInHdfs(FileSystem hdfs){
        Path inPath = new Path("/aa/navicat.zip");
        Path outPath = new Path("/test/navicat.zip");
        FSDataInputStream hdfsIn = null;
        FSDataOutputStream hdfsOut = null;
        try {
            hdfsIn = hdfs.open(inPath);
            hdfsOut = hdfs.create(outPath);
            IOUtils.copyBytes(hdfsIn,hdfsOut,1024*1024*64,false);
        } catch (IOException e) {
            log.error("copyInHdfs error,{}",e);
        }
    }



}
