package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeMap;

/** Represents a gitlet commit object.
 *  @author potaxo
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    // Filename is sha1hash
    /** The message of this Commit. */
    private String message;
    private Date date;
    // Using sha1hash as reference
    private String firstParentHash = null;
    private String secondParentHash = null;
    private final TreeMap<String, String> fileMap;


    public Commit(String message, String parentHash, String secondParentHash, TreeMap<String, String> fileMap) {
        this.message = message;
        this.firstParentHash = parentHash;
        this.secondParentHash = secondParentHash; // Set the second parent
        this.fileMap = fileMap;

        if (parentHash == null) {
            this.date = new Date(0);
        } else {
            this.date = new Date();
        }
    }

    public String getSecondParentHash() {
        return secondParentHash;
    }

    public String getParentHash() {
        return this.firstParentHash;
    }

    public TreeMap<String, String> getFileMap() {
        return this.fileMap;
    }

    public Date getDate() {
        return this.date;
    }

    /* return the hash of the commit is saved */
    public String save() {
        byte[] commitBytes = Utils.serialize(this);
        String commitHash = Utils.sha1(commitBytes);
        File commitFile = Utils.join(Repository.COMMITS_DIR, commitHash);
        Utils.writeObject(commitFile, this);
        return commitHash;// Use writeObject from Utils
    }
    // In Commit.java
    public String getMessage() {
        return this.message;
    }

}
