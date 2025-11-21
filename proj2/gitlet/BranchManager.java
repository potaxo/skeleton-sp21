package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Manages all branches and the active branch pointer (HEAD).
 * This class is serialized to a single file in .gitlet.
 */
public class BranchManager implements Serializable {

    /** Maps branch names (e.g., "main") to their head commit SHA-1 hashes. */
    private HashMap<String, String> branchHeads;

    /** Stores the name of the currently active branch (e.g., "main"). */
    private String activeBranchName;

    /** The file where this object is saved. */
    static final File BRANCH_FILE = Utils.join(Repository.GITLET_DIR, "branch_manager");

    /**
     * Creates a new, empty branch manager.
     */
    public BranchManager() {
        this.branchHeads = new HashMap<>();
        this.activeBranchName = null;
    }

    /**
     * Loads the BranchManager from its file, or creates a new one.
     */
    public static BranchManager load() {
        if (!BRANCH_FILE.exists()) {
            return new BranchManager();
        }
        return Utils.readObject(BRANCH_FILE, BranchManager.class);
    }

    /**
     * Saves this BranchManager object to its file.
     */
    public void save() {
        Utils.writeObject(BRANCH_FILE, this);
    }

    // --- New Branching Methods ---

    /**
     * Creates a new branch pointer.
     * @param branchName The name of the new branch.
     * @param commitHash The commit hash the new branch should point to.
     */
    public void createBranch(String branchName, String commitHash) {
        branchHeads.put(branchName, commitHash);
    }

    /**
     * Checks if a branch with this name already exists.
     */
    public boolean containsBranch(String branchName) {
        return branchHeads.containsKey(branchName);
    }

    /**
     * Sets the active branch name.
     */
    public void setActiveBranch(String branchName) {
        this.activeBranchName = branchName;
    }

    /**
     * Updates the commit hash for the *currently active* branch.
     * (Used by 'commit')
     */
    public void updateActiveBranch(String newCommitHash) {
        branchHeads.put(activeBranchName, newCommitHash);
    }

    /**
     * Returns the hash of the commit at the head of the given branch.
     */
    public String getBranchHead(String branchName) {
        return branchHeads.get(branchName);
    }

    /**
     * Returns the name of the currently active branch.
     */
    public String getActiveBranchName() {
        return activeBranchName;
    }


    // --- Refactored 'Trees' Methods ---

    /**
     * Gets the commit hash of the *active* branch.
     */
    public static String getCurrentCommitHash() {
        BranchManager bm = BranchManager.load();
        if (bm.activeBranchName == null) {
            return null; // Should only happen before init
        }
        return bm.branchHeads.get(bm.activeBranchName);
    }

    /**
     * Gets the Commit object for the *active* branch.
     */
    public static Commit getCurrentCommit() {
        String commitHash = getCurrentCommitHash();
        if (commitHash == null) {
            // This handles the case for the very first commit,
            // where the parent is null.
            return null;
        }
        File commitFile = Utils.join(Repository.COMMITS_DIR, commitHash);
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Removes the branch with the given name.
     */
    public void removeBranch(String branchName) {
        branchHeads.remove(branchName);
    }
}