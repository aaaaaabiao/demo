package site.abiao.demo.lucene.directory;

import org.apache.commons.vfs2.FileObject;
import org.apache.lucene.store.*;

import java.io.IOException;

public class VFSLockFactory extends LockFactory {

    @Override
    public Lock obtainLock(Directory dir, String lockName) throws IOException {
        if (dir != null) {
            VFSDirectory vfsDir = (VFSDirectory) dir;
            FileObject lockfile = vfsDir.getDirectory().resolveFile(lockName);
            return new SimpleFSLock(lockfile);
        }
        return null;
    }

    static final class SimpleFSLock extends Lock {
        private final FileObject file;
        private volatile boolean closed;

        SimpleFSLock(FileObject file) throws IOException {
            this.file = file;
        }

        @Override
        public void ensureValid() throws IOException {
            if (closed) {
                throw new AlreadyClosedException("Lock instance already released: " + this);
            }
        }

        @Override
        public synchronized void close() throws IOException {
            if (closed) {
                return;
            }
            try {
                // NOTE: unlike NativeFSLockFactory, we can potentially delete someone else's
                // lock if things have gone wrong. we do best-effort check (ensureValid) to
                // avoid doing this.
                try {
                    ensureValid();
                } catch (Throwable exc) {
                    // notify the user they may need to intervene.
                    throw new LockReleaseFailedException("Lock file cannot be safely removed. Manual intervention is recommended.", exc);
                }
                // we did a best effort check, now try to remove the file. if something goes wrong,
                // we need to make it clear to the user that the directory may still remain locked.
                try {
                    file.delete();
                } catch (Throwable exc) {
                    throw new LockReleaseFailedException("Unable to remove lock file. Manual intervention is recommended", exc);
                }
            } finally {
                closed = true;
            }
        }
    }
}
