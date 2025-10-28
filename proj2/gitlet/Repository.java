package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here
import java.util.HashMap;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    public static final File INDEX_FILE = Utils.join(GITLET_DIR, "index");

    /* TODO: fill in the rest of this class. */
    public static void initCommand() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        } else {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOBS_DIR.mkdir();
            INDEX_FILE.mkdir();
        }

    /* TODO: Create the initial commit */
    /* TODO: Set up branches */
    /* TODO: set the HEAD pointer to point the main branch. */
    /* TODO: Set up index folder */
        System.out.println("Initialized empty Gitlet repository in "
                + GITLET_DIR.getAbsolutePath());
    }

    public static void addCommand(String filename) {
        /* Get and check sourceFile */
        File sourceFile = Utils.join(CWD, "filename");
        if (!sourceFile.exists()) {
            System.out.println("File does not exist");
            return;
        }

        /* read contents */
        byte[] contents = Utils.readContents(sourceFile);

        // Give this sha1hash name
        String sha1Hash = Utils.sha1(contents);
        File blobFile = Utils.join(BLOBS_DIR, sha1Hash);

        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, contents);
        }

        // TODO: Handle stage.
    }
}
