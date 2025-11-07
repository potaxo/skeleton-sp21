package gitlet;

import java.io.File;
import java.io.Serializable;

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
}
