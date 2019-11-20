package site.abiao.demo.apache.lucene.directory.hdfs;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.lucene.store.OutputStreamIndexOutput;


public class HdfsOutputStream extends OutputStreamIndexOutput {

    static final int CHUNK_SIZE = 8192;
    /**
     * Sole constructor.  resourceDescription should be non-null, opaque string
     * describing this resource; it's returned from {@link #toString}.
     *
     * @param resourceDescription
     * @param name
     */
    protected HdfsOutputStream(String resourceDescription, String name, FSDataOutputStream fsDataOutputStream) {
        super("hdfsIndexOutput", name, fsDataOutputStream, CHUNK_SIZE);

    }
}
