package site.abiao.demo.apache.vfs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;

import java.net.URISyntaxException;

/**
 * 连接ftp文件服务器
 *
 * @author hubiao
 */
@Slf4j
public class VFSFtp {

    private static String username = "test";
    private static String password = "test";
    private static String domain = "10.28.132.48";

    private FileSystemManager fileSystemManager;
    private FileSystemOptions opts;
    public VFSFtp(String domain, String username, String password) {
        try {
            opts = new FileSystemOptions();
            StaticUserAuthenticator auth = new StaticUserAuthenticator(domain, username, password);
            DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
            FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
            fileSystemManager = VFS.getManager();

        } catch (FileSystemException e) {
            log.error("created fileSystemManager failed" , e);
        }
    }


    public void createFile(String filename) {
        String uri = String.format("ftp://%s:%s@%s/%s",username , password, domain, filename);
//        String uri = String.format("ftp://%s",filename);
        try {
            FileObject file = fileSystemManager.resolveFile(uri, opts);
            if (file.exists()) {
                log.info("file exists!!!");
            }
            file.createFile();
        } catch (FileSystemException e) {
            log.error("createFile failed", e);
        }
    }

    public void deleteAll(String dir) throws URISyntaxException, FileSystemException {
        String uri = String.format("ftp://%s:%s@%s%s",username , password, domain, dir);
        FileObject file = fileSystemManager.resolveFile(uri,opts);
        FileObject[] children = file.getChildren();
        for (FileObject f : children) {
            log.info(f.getName().getBaseName());
            f.delete();
        }
    }

    public static void main(String[] args) throws URISyntaxException, FileSystemException {
        VFSFtp ftp = new VFSFtp(domain, username, password);
//        ftp.createFile("java1.txt");
        ftp.deleteAll("/test");
    }
}
