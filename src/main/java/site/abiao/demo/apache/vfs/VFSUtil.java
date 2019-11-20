package site.abiao.demo.apache.vfs;

import org.apache.commons.vfs2.*;

import java.io.File;

public class VFSUtil {

    private static FileSystemManager fileSystemManager;

    static {
        try {
            fileSystemManager = VFS.getManager();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }


    public static void createFile(String uri) throws FileSystemException {
        FileObject file = fileSystemManager.resolveFile(uri);
        file.createFile();
    }



    public static void main(String[] args) throws FileSystemException {
//        createFile("file:///Users/hubiao/java/data/hello.txt");
        createFile("hdfs://10.28.132.48:9000/test.txt");
    }
}
