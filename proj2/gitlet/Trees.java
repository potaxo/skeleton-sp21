package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.readObject;

// This class is used for recording the current commit tree
public class Trees implements Serializable {
    // 11.4.25 maybe there should be a lot of pointers not only the Head
    // showing the current commit, but also have branch head (in branch class)
    public String Head = null;

    static final File TREES_FILE = Utils.join(Repository.TREES_DIR, "trees");

    public Trees() {
    }

    public static Trees load() {
        if (!TREES_FILE.exists()) {
            return new Trees();
        }
        return Utils.readObject(TREES_FILE, Trees.class);
    }

    public void save() {
        Utils.writeObject(TREES_FILE, this);
    }

    public static Commit getCurrentCommit() {
        String parentHash = Trees.load().Head;
        File parentFile = Utils.join(Repository.COMMITS_DIR, parentHash);
        Commit parentCommit = readObject(parentFile, Commit.class);
        return parentCommit;
    }
    public static String getCurrentCommitHash() {
        String parentHash = Trees.load().Head;
        return parentHash;
    }
}
