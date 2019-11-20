package site.abiao.demo.apache.lucene.directory.hdfs;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.lucene.store.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;

@Slf4j
public class HdfsDirectory extends BaseDirectory{



  protected final Path path;
  protected final FileSystem fileSystem;

  public HdfsDirectory(Configuration configuration, String dir) throws IOException {
    super(NoLockFactory.INSTANCE);
    this.path = new Path(dir);
    fileSystem = path.getFileSystem(configuration);
    fileSystem.mkdirs(path);
  }

  @Override
  public String toString() {
    return "HdfsDirectory path=[" + path + "]";
  }

  @Override
  public IndexOutput createOutput(String name, IOContext context) throws IOException {
    log.debug(MessageFormat.format("createOutput [{0}] [{1}] [{2}]", name, context, path));
    if (fileExists(name)) {
      throw new IOException("File [" + name + "] already exists found.");
    }
    final FSDataOutputStream outputStream = openForOutput(name);
    return new HdfsOutputStream("hdfs", name, outputStream);
  }

  @Override
  public IndexOutput createTempOutput(String prefix, String suffix, IOContext context) throws IOException {
    return null;
  }

  protected FSDataOutputStream openForOutput(String name) throws IOException {
    return fileSystem.create(getPath(name));
  }


  @Override
  public IndexInput openInput(String name, IOContext context) throws IOException {
    log.debug(MessageFormat.format("openInput [{0}] [{1}] [{2}]", name, context, path));
    if (!fileExists(name)) {
      throw new FileNotFoundException("File [" + name + "] not found.");
    }
    FSDataInputStream inputStream = openForInput(name);
    long fileLength = fileLength(name);
    return new HdfsIndexInput(name, inputStream, fileLength);
  }

  protected FSDataInputStream openForInput(String name) throws IOException {
    return fileSystem.open(getPath(name));
  }

  @Override
  public String[] listAll() throws IOException {
    log.debug(MessageFormat.format("listAll [{0}]", path));
    FileStatus[] files = fileSystem.listStatus(path, new PathFilter() {
      @Override
      public boolean accept(Path path) {
        try {
          return fileSystem.isFile(path);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
    String[] result = new String[files.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = files[i].getPath().getName();
    }
    return result;
  }

  public boolean fileExists(String name) throws IOException {
    log.debug(MessageFormat.format("fileExists [{0}] [{1}]", name, path));
    return exists(name);
  }

  protected boolean exists(String name) throws IOException {
    return fileSystem.exists(getPath(name));
  }

  @Override
  public void deleteFile(String name) throws IOException {
    log.debug(MessageFormat.format("deleteFile [{0}] [{1}]", name, path));
    if (fileExists(name)) {
      delete(name);
    } else {
      throw new FileNotFoundException("File [" + name + "] not found");
    }
  }

  protected void delete(String name) throws IOException {
    fileSystem.delete(getPath(name), true);
  }

  @Override
  public long fileLength(String name) throws IOException {
    log.debug(MessageFormat.format("fileLength [{0}] [{1}]", name, path));
    return length(name);
  }

  protected long length(String name) throws IOException {
    return fileSystem.getFileStatus(getPath(name)).getLen();
  }

  @Override
  public void sync(Collection<String> names) throws IOException {

  }

  @Override
  public void rename(String source, String dest) throws IOException {
    fileSystem.rename(getPath(source), getPath(dest));
  }

  @Override
  public void syncMetaData() throws IOException {

  }

  @Override
  public void close() throws IOException {
    fileSystem.close();
  }

  public Path getPath() {
    return path;
  }

  private Path getPath(String name) {
    return new Path(path, name);
  }



}
