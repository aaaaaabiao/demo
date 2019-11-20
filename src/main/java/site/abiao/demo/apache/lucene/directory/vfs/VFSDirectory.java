package site.abiao.demo.apache.lucene.directory.vfs;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.ftp.FtpFileSystemConfigBuilder;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.apache.lucene.store.*;

/**
 * 扩展Lucene索引的存储,使用commons-vfs做为lucene的存储引擎
 *
 * @author hubiao
 */
public class VFSDirectory extends BaseDirectory {

    private FileSystemManager fileManager;
    private FileObject directory;
    private FileSystemOptions opts;

    public VFSDirectory(String repository_uri) throws IOException {
         this(repository_uri, VFSLockFactory.INSTANCE);
    }
    public VFSDirectory(String repository_uri, LockFactory lockFactory) throws IOException {
        super(lockFactory);
        opts = new FileSystemOptions();
        FtpFileSystemConfigBuilder.getInstance().setPassiveMode(opts, true);
        this.fileManager = VFS.getManager();
        this.directory = fileManager.resolveFile(repository_uri, opts);
        if (!directory.exists())
            directory.createFolder();

    }


    @Override
    public String[] listAll() throws IOException {
        FileObject[] files = directory.getChildren();
        int len = files.length;
        String[] names = new String[len];
        for (int i = 0; i < len; i++) {
            names[i] = files[i].getName().getBaseName();
        }
        return names;
    }

    @Override
    public void deleteFile(String name) throws IOException {
        FileObject file = fileManager.resolveFile(directory, name);
        if (!file.delete()) {
            throw new IOException("Cannot delete" + file);
        }
    }

    @Override
    public long fileLength(String name) throws IOException {
        FileObject file = fileManager.resolveFile(directory, name);
        return file.getContent().getSize();
    }

    @Override
    public IndexOutput createOutput(String name, IOContext context) throws IOException {
        FileObject file = fileManager.resolveFile(directory, name);
        return new VFSIndexOutput(file,"vfs");
    }

    @Override
    public IndexOutput createTempOutput(String prefix, String suffix, IOContext context) throws IOException {
        return null;
    }

    @Override
    public void sync(Collection<String> names) throws IOException {

    }

    @Override
    public void rename(String source, String dest) throws IOException {
        ensureOpen();
        FileObject sourceFile = directory.resolveFile(source);
        FileObject destFile = directory.resolveFile(dest);
        sourceFile.moveTo(destFile);
    }

    @Override
    public void syncMetaData() throws IOException {

    }

    @Override
    public IndexInput openInput(String name, IOContext context) throws IOException {
        FileObject file = fileManager.resolveFile(directory, name);
        return new VFSIndexInput("VFS",file);
    }


    @Override
    public void close() throws IOException {
        directory.close();
    }

    class VFSIndexInput extends BufferedIndexInput{

        private RandomAccessContent content;
        private long length;

        public VFSIndexInput(String resourceDesc, FileObject file) throws IOException {
            super(resourceDesc);
            this.content = file.getContent().getRandomAccessContent(RandomAccessMode.READ);
            this.length = content.length();
        }

        @Override
        protected void readInternal(byte[] b, int offset, int length) throws IOException {
            this.content.readFully(b, offset, length);
        }

        @Override
        protected void seekInternal(long pos) throws IOException {
            this.content.seek(pos);
        }

        @Override
        public void close() throws IOException {
            this.content.close();
        }

        @Override
        public long length() {
            return this.length;
        }
    }

    class VFSIndexOutput extends OutputStreamIndexOutput{
        static final int CHUNK_SIZE = 8192;
        public VFSIndexOutput(FileObject file, String name) throws FileSystemException {
            super("VFSIndexOutput", name, file.getContent().getOutputStream(), CHUNK_SIZE);
        }

    }

    public FileSystemManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileSystemManager fileManager) {
        this.fileManager = fileManager;
    }

    public FileObject getDirectory() {
        return directory;
    }

    public void setDirectory(FileObject directory) {
        this.directory = directory;
    }
}