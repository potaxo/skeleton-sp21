package gitlet;

import jh61b.junit.In;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *
 *  @author potaxo
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
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    public static final File COMMITS_DIR = Utils.join(GITLET_DIR, "commits");
    public static final File TREES_DIR = Utils.join(GITLET_DIR, "trees");
    public static final File BLOBS_DIR = Utils.join(GITLET_DIR, "blobs");
    public static final File INDEX_DIR = Utils.join(GITLET_DIR, "index");

    /* TODO: fill in the rest of this class. */
    public static void initCommand() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        } else {
            GITLET_DIR.mkdir();
            COMMITS_DIR.mkdir();
            TREES_DIR.mkdir();
            BLOBS_DIR.mkdir();
            INDEX_DIR.mkdir();
        }

    /*  Create the initial commit */
        Commit initialCommit = new Commit("initialCommit", null, null );
        byte[] commitBytes = Utils.serialize(initialCommit);
        String commitHash = Utils.sha1(commitBytes);
        File commitFile = Utils.join(COMMITS_DIR, commitHash);
        Utils.writeObject(commitFile, commitBytes); // Use writeObject from Utils

        // Init the tree and set the Head pointer to initcommit if there isn't tree file created.
        Trees tree = Trees.load();
        if (tree.Head == null) {
            tree.Head = commitHash;
        }
        tree.save();

    /* TODO: Set up branches */
    /* TODO: set the HEAD pointer to point the main branch. */
    }

    public static void addCommand(String filename) {
        /* Get and check sourceFile */
        File sourceFile = Utils.join(CWD, filename);
        if (!sourceFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        /* read contents */
        byte[] contents = Utils.readContents(sourceFile);

        // Give this sha1hash name
        String sha1Hash = Utils.sha1(contents);
        File blobFile = Utils.join(BLOBS_DIR, sha1Hash);

        // Handle the index
        Index index = Index.load();
        // TODO: Examine the interaction between index and commit
        index.addFileToStage(filename, sha1Hash);
        index.save();

        /*
        // --- Edge Case: File is identical to committed version ---
        if (sha1Hash.equals(commitMap.get(filename))) {
        // Contents are the same. Un-stage it if it's staged.
            if (index.contains(filename)) {
                index.remove(filename);
                index.save(); // Save changes to index
            }
            return; // All done!
        }
        */
        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, contents);
            // log: System.out.println("successfully create the" + blobFile);
        }
    }
    public static void commitCommand(String message) {
        // Create a new commit and fill the necessary information
        // Inherit parents' fileToAdd(track?)
        // Link the filesToAdd filesToAdd property etc in index(update the updated file)
            // Maybe already checked the repeat situation in addCommand.
        // store the new commit and update the Head
        // clear the filesToAdd index panel.

    }
}
