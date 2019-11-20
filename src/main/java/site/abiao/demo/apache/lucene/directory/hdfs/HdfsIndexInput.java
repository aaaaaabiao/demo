package site.abiao.demo.apache.lucene.directory.hdfs;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.lucene.store.BufferedIndexInput;

import java.io.IOException;


public class HdfsIndexInput extends BufferedIndexInput {

    private FSDataInputStream inputStream;
    private long len;
    public HdfsIndexInput(String resourceDesc, FSDataInputStream inputStream, long len) {
        super(resourceDesc);
        this.inputStream = inputStream;
        this.len = len;
    }

    @Override
    protected void readInternal(byte[] b, int offset, int length) throws IOException {
        inputStream.read(b, offset, length);
    }

    @Override
    protected void seekInternal(long pos) throws IOException {
        inputStream.seek(pos);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public long length() {
        return len;
    }
}
