package gitlet;

import java.io.Serializable;
import java.util.Date;
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


    public Commit(String message, String parentHash, TreeMap<String, String> fileMap) {
        this.message = message;
        this.firstParentHash = parentHash;
        this.fileMap = fileMap;

        // This is how you set the time!
        // The spec requires the initial commit (which has a null parent)
        // to have a special timestamp of 0 (the "epoch").
        if (parentHash == null) {
            this.date = new Date(0); // The epoch (0 milliseconds)
        } else {
            // For all other commits, use the current time.
            this.date = new Date();
        }
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
}
