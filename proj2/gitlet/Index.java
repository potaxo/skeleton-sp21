package gitlet;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.HashSet;
import java.io.File;

/* This class is for staging area, have two private properties */
public class Index implements Serializable {
    private TreeMap<String, String> filesToAdd;
    // This store the filename to be removed(untrack)
    private HashSet<String> filesToRemove;
    static final File INDEX_FILE = Utils.join(Repository.INDEX_DIR, "index");

    public Index() {
        this.filesToAdd = new TreeMap<>();
        this.filesToRemove = new HashSet<>();
    }

    public TreeMap<String, String> getFilesToAdd() {
        return filesToAdd;
    }

    // TODO: Remove stage

    public static Index load() {
        if (!INDEX_FILE.exists()) {
            return new Index();
        }
        return Utils.readObject(INDEX_FILE, Index.class);
    }

    public void addFileToStage(String filename, String sha1hash) {
        filesToAdd.put(filename, sha1hash);
    }

    /**
     * Stages a file for removal.
     * @param filename The name of the file to remove.
     */
    public void stageForRemoval(String filename) {
        // Add the filename (e.g., "test.txt") to the removal set.
        filesToRemove.add(filename);
    }

    public void save() {
        Utils.writeObject(INDEX_FILE, this);
    }

    // TODO: need to be filled
    public void clear() {
        filesToAdd = null;
        filesToRemove = null;
    }

    private boolean isIndexEmptyHelper() {
        if (this.filesToAdd.isEmpty() && this.filesToRemove.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isIndexEmpty() {
        Index index = Index.load();
        return index.isIndexEmptyHelper();
    }
}
