package gitlet;

import java.io.File;
import java.util.TreeMap;
import java.text.SimpleDateFormat; // For formatting the date
import java.util.List; // For global-log
import java.util.Locale; // For formatting the date
import java.lang.String;
import java.util.Set;
// BFS
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;


/** Represents a gitlet repository.
 *
 *  @author potaxo
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    public static final File MERGE_HEAD = Utils.join(GITLET_DIR, "MERGE_HEAD");
    public static final File COMMITS_DIR = Utils.join(GITLET_DIR, "commits");
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
            BLOBS_DIR.mkdir();
            INDEX_DIR.mkdir();
        }

    /*  Create the initial commit */
        Commit initialCommit = new Commit("initial commit", null, null, new TreeMap<>());
        String commitHash = initialCommit.save();

        // init the branch manager
        BranchManager bm = new BranchManager();
        bm.createBranch("main", commitHash); // Create the "main" branch
        bm.setActiveBranch("main");          // Set it as active
        bm.save();                           // Save the manager
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

        // --- Edge Case: File is identical to committed version ---
        TreeMap<String, String> commitMap = BranchManager.getCurrentCommit().getFileMap();
        if (sha1Hash.equals(commitMap.get(filename))) {
        // Contents are the same. Un-stage it if it's staged.
            if (index.getFilesToAdd().containsKey(filename)) {
                index.getFilesToAdd().remove(filename);
                index.save(); // Save changes to index
            }
            return; // All done!
        }

        if (!blobFile.exists()) {
            Utils.writeContents(blobFile, contents);
            // log: System.out.println("successfully create the" + blobFile);
        }
    }

    public static void commitCommand(String message) {
        Index index = Index.load();
        // Validate checks (message empty, index empty) ... [Reuse your existing checks]
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }
        if (Index.isIndexEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        String parentHash = BranchManager.getCurrentCommitHash();
        Commit parentCommit = BranchManager.getCurrentCommit();

        // 1. Determine Second Parent (for merges)
        String secondParentHash = null;
        if (MERGE_HEAD.exists()) {
            secondParentHash = Utils.readContentsAsString(MERGE_HEAD);
            MERGE_HEAD.delete(); // Clean up the state
        }

        // 2. Create new map (Copy Parent -> Apply Index)
        TreeMap<String, String> newFileMap = (parentCommit == null) ? new TreeMap<>() : new TreeMap<>(parentCommit.getFileMap());
        newFileMap.putAll(index.getFilesToAdd());
        for (String filename : index.getFilesToRemove()) {
            newFileMap.remove(filename);
        }

        // 3. Create Commit with TWO parents
        Commit commit = new Commit(message, parentHash, secondParentHash, newFileMap);

        // 4. Save and Update
        String commitHash = commit.save();
        BranchManager bm = BranchManager.load();
        bm.updateActiveBranch(commitHash);
        bm.save();

        index.clear();
        index.save();
    }

    public static void rmCommand(String filename) {
        // 1. Load the staging area and current commit's file map
        Index stagingArea = Index.load();
        Commit currentCommit = BranchManager.getCurrentCommit();

        // Get the map of files tracked by the current commit
        TreeMap<String, String> trackedFiles = currentCommit.getFileMap();

        // 2. Check if the file is staged or tracked
        boolean isStaged = stagingArea.getFilesToAdd().containsKey(filename);
        boolean isTracked = trackedFiles.containsKey(filename);

        // 3. Error Case: If not staged and not tracked, there's nothing to do.
        if (!isStaged && !isTracked) {
            System.out.println("No reason to remove the file.");
            return;
        }

        // 4. Action: Un-stage the file if it's staged for addition
        if (isStaged) {
            stagingArea.getFilesToAdd().remove(filename);
        }

        // 5. Action: Stage for removal AND delete from working directory if tracked
        if (isTracked) {
            // Stage it for removal by adding its name to the set
            stagingArea.stageForRemoval(filename);

            // Delete the file from the working directory
            File fileToDelete = Utils.join(CWD, filename);
            Utils.restrictedDelete(fileToDelete);
        }

        // 6. Save the updated staging area
        stagingArea.save();
    }
    public static void logCommand() {
        // Get the hash of the current HEAD commit
        String currentCommitHash = BranchManager.getCurrentCommitHash();

        // Loop backwards from HEAD until we hit the initial commit's null parent
        while (currentCommitHash != null) {
            // Load the commit object using its hash
            File commitFile = Utils.join(COMMITS_DIR, currentCommitHash);
            Commit currentCommit = Utils.readObject(commitFile, Commit.class);

            // Print the commit details
            printCommit(currentCommit, currentCommitHash);

            // Move to the parent commit
            currentCommitHash = currentCommit.getParentHash();
        }
        // TODO: handle the merge commit case
    }

    /**
     * Implements the 'global-log' command.
     * Finds and prints every commit ever made, in any order.
     */
    public static void globalLogCommand() {
        // Get a list of all filenames in the commits directory
        // These filenames *are* the commit hashes
        List<String> allCommitHashes = Utils.plainFilenamesIn(COMMITS_DIR);

        // Loop through every commit hash
        if (allCommitHashes != null) {
            for (String commitHash : allCommitHashes) {
                // Load the commit object
                File commitFile = Utils.join(COMMITS_DIR, commitHash);
                Commit commit = Utils.readObject(commitFile, Commit.class);

                // Print the commit details
                printCommit(commit, commitHash);
            }
        }
    }

    /**
     * Helper function to print a commit's information in the standard format.
     * @param commit The commit object to print.
     * @param hash The SHA-1 hash of that commit.
     */
    private static void printCommit(Commit commit, String hash) {
        // Example Date Format: Thu Nov 14 00:56:00 2025 -0600
        // Use Locale.US to ensure consistent formatting
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);

        System.out.println("===");
        System.out.println("commit " + hash);
        System.out.println("Date: " + sdf.format(commit.getDate()));
        System.out.println(commit.getMessage()); // Assumes Commit has getMessage()
        System.out.println(); // Extra newline
    }

    /**
     * Implements the 'find' command.
     * Finds and prints the hash of all commits with the given message.
     * @param message The exact commit message to find.
     */
    public static void findCommand(String message) {
        // Get a list of all filenames in the commits directory
        // These filenames *are* the commit hashes
        List<String> allCommitHashes = Utils.plainFilenamesIn(COMMITS_DIR);

        boolean found = false; // Flag to track if we find any matches

        // Loop through every commit hash
        if (allCommitHashes != null) {
            for (String commitHash : allCommitHashes) {
                // Load the commit object
                File commitFile = Utils.join(COMMITS_DIR, commitHash);
                Commit commit = Utils.readObject(commitFile, Commit.class);

                // --- This is the new logic ---
                // Check if the commit's message matches the one we're looking for
                if (commit.getMessage().equals(message)) {
                    System.out.println(commitHash);
                    found = true; // We found at least one
                }
                // --- End of new logic ---
            }
        }

        // If the flag is still false, we didn't find any.
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }
    /**
     * Implements the 'branch' command.
     * Creates a new branch pointer with the given name.
     * @param branchName The name for the new branch.
     */
    public static void branchCommand(String branchName) {
        BranchManager bm = BranchManager.load();

        // Check if branch already exists
        if (bm.containsBranch(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }

        // Get the current commit and create the new branch
        String currentCommitHash = BranchManager.getCurrentCommitHash();
        bm.createBranch(branchName, currentCommitHash);
        bm.save();
    }

    /**
     * Case 1: checkout -- [filename]
     * Takes the version of the file as it exists in the head commit
     * and puts it in the working directory, overwriting the version
     * of the file that's already there if there is one.
     */
    public static void checkoutFileFromHead(String filename) {
        String currentCommitHash = BranchManager.getCurrentCommitHash();
        checkoutFileFromCommit(currentCommitHash, filename);
    }

    /**
     * Case 2: checkout [commit id] -- [filename]
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory, overwriting the version of the file
     * that's already there if there is one.
     */
    public static void checkoutFileFromCommit(String commitId, String filename) {
        // 1. Validate commit ID (support short IDs)
        if (commitId.length() < Utils.UID_LENGTH) {
            // Try to find full ID
            List<String> allCommits = Utils.plainFilenamesIn(COMMITS_DIR);
            boolean found = false;
            for (String fullId : allCommits) {
                if (fullId.startsWith(commitId)) {
                    commitId = fullId;
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No commit with that id exists.");
                return;
            }
        } else {
            File commitFile = Utils.join(COMMITS_DIR, commitId);
            if (!commitFile.exists()) {
                System.out.println("No commit with that id exists.");
                return;
            }
        }

        // 2. Load the commit
        File commitFile = Utils.join(COMMITS_DIR, commitId);
        Commit commit = Utils.readObject(commitFile, Commit.class);

        // 3. Check if file exists in that commit
        if (!commit.getFileMap().containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        // 4. Get the blob hash and load contents
        String blobHash = commit.getFileMap().get(filename);
        File blobFile = Utils.join(BLOBS_DIR, blobHash);
        byte[] contents = Utils.readContents(blobFile);

        // 5. Write to working directory
        File destFile = Utils.join(CWD, filename);
        Utils.writeContents(destFile, contents);
    }

    /**
     * Case 3: checkout [branch name]
     * Takes all files in the commit at the head of the given branch,
     * and puts them in the working directory, overwriting the versions
     * of the files that are already there if they exist.
     */
    public static void checkoutBranch(String branchName) {
        BranchManager bm = BranchManager.load();
        if (!bm.containsBranch(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (branchName.equals(bm.getActiveBranchName())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        String targetCommitHash = bm.getBranchHead(branchName);

        if (checkoutAllFiles(targetCommitHash)) {
            bm.setActiveBranch(branchName); // Only change HEAD name, not the commit of the branch
            bm.save();
        }
    }
    public static void rmBranchCommand(String branchName) {
        BranchManager bm = BranchManager.load();

        if (!bm.containsBranch(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        if (branchName.equals(bm.getActiveBranchName())) {
            System.out.println("Cannot remove the current branch.");
            return;
        }

        bm.removeBranch(branchName);
        bm.save();
    }

    /**
     * Helper to resolve a short UID to a full SHA-1 hash.
     * Returns null if not found.
     */
    private static String resolveCommitId(String commitId) {
        if (commitId.length() == Utils.UID_LENGTH) {
            return commitId;
        }
        List<String> allCommits = Utils.plainFilenamesIn(COMMITS_DIR);
        if (allCommits != null) {
            for (String fullId : allCommits) {
                if (fullId.startsWith(commitId)) {
                    return fullId;
                }
            }
        }
        return null;
    }

    /**
     * Helper to checkout all files from a given commit.
     * Performs the untracked file safety check, deletions, and overwrites.
     * Returns true if successful, false if blocked by untracked files.
     */
    private static boolean checkoutAllFiles(String targetCommitId) {
        Commit targetCommit = Utils.readObject(Utils.join(COMMITS_DIR, targetCommitId), Commit.class);
        Commit currentCommit = BranchManager.getCurrentCommit();

        TreeMap<String, String> targetFiles = targetCommit.getFileMap();
        TreeMap<String, String> currentFiles = (currentCommit != null) ? currentCommit.getFileMap() : new TreeMap<>();

        // 1. Check for Untracked File Overwrites (Safety Check)
        List<String> workingFiles = Utils.plainFilenamesIn(CWD);
        if (workingFiles != null) {
            for (String filename : workingFiles) {
                if (targetFiles.containsKey(filename) && !currentFiles.containsKey(filename)) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return false;
                }
            }
        }

        // 2. Delete files tracked in current but not in target
        for (String filename : currentFiles.keySet()) {
            if (!targetFiles.containsKey(filename)) {
                Utils.restrictedDelete(Utils.join(CWD, filename));
            }
        }

        // 3. Overwrite/Create files from target commit
        for (String filename : targetFiles.keySet()) {
            String blobHash = targetFiles.get(filename);
            File blobFile = Utils.join(BLOBS_DIR, blobHash);
            byte[] contents = Utils.readContents(blobFile);
            Utils.writeContents(Utils.join(CWD, filename), contents);
        }

        // 4. Clear the Staging Area
        Index index = Index.load();
        index.clear();
        index.save();

        return true;
    }

    public static void resetCommand(String commitId) {
        String fullCommitId = resolveCommitId(commitId);
        if (fullCommitId == null || !Utils.join(COMMITS_DIR, fullCommitId).exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }

        // Reuse the checkout logic
        if (checkoutAllFiles(fullCommitId)) {
            BranchManager bm = BranchManager.load();
            bm.updateActiveBranch(fullCommitId); // Move the pointer
            bm.save();
        }
    }

    // Using BFS get split point
    private static String getSplitPoint(String commit1, String commit2) {
        Set<String> ancestors1 = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        // BFS for the first commit (Current)
        queue.add(commit1);
        while (!queue.isEmpty()) {
            String c = queue.poll();
            if (c == null || ancestors1.contains(c)) continue;
            ancestors1.add(c);

            Commit commit = Utils.readObject(Utils.join(COMMITS_DIR, c), Commit.class);
            if (commit.getParentHash() != null) queue.add(commit.getParentHash());
            if (commit.getSecondParentHash() != null) queue.add(commit.getSecondParentHash());
        }

        // BFS for the second commit (Given) - Find the first intersection
        queue.add(commit2);
        while (!queue.isEmpty()) {
            String c = queue.poll();
            if (ancestors1.contains(c)) {
                return c; // Found the split point!
            }
            Commit commit = Utils.readObject(Utils.join(COMMITS_DIR, c), Commit.class);
            if (commit.getParentHash() != null) queue.add(commit.getParentHash());
            if (commit.getSecondParentHash() != null) queue.add(commit.getSecondParentHash());
        }
        return null;
    }

    public static void mergeCommand(String branchName) {
        // 1. Validation Checks
        Index index = Index.load();
        BranchManager bm = BranchManager.load();
        if (!index.isIndexEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!bm.containsBranch(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branchName.equals(bm.getActiveBranchName())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        // 2. Get Commits
        String currentHash = BranchManager.getCurrentCommitHash();
        String givenHash = bm.getBranchHead(branchName);
        String splitPointHash = getSplitPoint(currentHash, givenHash);

        // 3. Ancestry Checks
        if (splitPointHash.equals(givenHash)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitPointHash.equals(currentHash)) {
            checkoutBranch(branchName); // Fast-forward
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        // 4. Merge Logic
        Commit currentCommit = BranchManager.getCurrentCommit();
        Commit givenCommit = Utils.readObject(Utils.join(COMMITS_DIR, givenHash), Commit.class);
        Commit splitCommit = Utils.readObject(Utils.join(COMMITS_DIR, splitPointHash), Commit.class);

        TreeMap<String, String> curFiles = currentCommit.getFileMap();
        TreeMap<String, String> givFiles = givenCommit.getFileMap();
        TreeMap<String, String> splFiles = splitCommit.getFileMap();

        // Collect all unique files from all three commits
        Set<String> allFiles = new HashSet<>();
        allFiles.addAll(curFiles.keySet());
        allFiles.addAll(givFiles.keySet());
        allFiles.addAll(splFiles.keySet());

        // Check for untracked file overwrites BEFORE processing
        List<String> workingFiles = Utils.plainFilenamesIn(CWD);
        if (workingFiles != null) {
            for (String file : workingFiles) {
                boolean trackedInCurrent = curFiles.containsKey(file);
                boolean presentInGiven = givFiles.containsKey(file);
                boolean presentInSplit = splFiles.containsKey(file);

                // If logic dictates we might overwrite/modify, and it's untracked -> fail
                // (Simplified check: if it's not tracked, but involved in merge, be safe)
                if (!trackedInCurrent && presentInGiven && !presentInSplit) {
                    System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    return;
                }
            }
        }

        boolean conflict = false;

        for (String filename : allFiles) {
            String hSpl = splFiles.get(filename);
            String hCur = curFiles.get(filename);
            String hGiv = givFiles.get(filename);

            // Logic Table implementation
            if (equals(hCur, hGiv)) {
                // Case 3: Same modification (or both deleted/absent). Do nothing.
                continue;
            }

            if (equals(hCur, hSpl)) {
                // Current matches Split (unchanged in current), but Given is different.
                // Case 1, 6: Accept Given's change (or deletion).
                if (hGiv == null) {
                    rmCommand(filename); // Case 6: Remove
                } else {
                    checkoutFileFromCommit(givenHash, filename); // Case 1: Checkout given
                    index.addFileToStage(filename, hGiv); // Stage it
                }
            } else if (equals(hGiv, hSpl)) {
                // Given matches Split, Current is different.
                // Case 2, 7: Keep Current's change. Do nothing.
                continue;
            } else {
                // Case 8: Conflict! (Both changed differently)
                conflict = true;
                handleConflict(filename, hCur, hGiv);
            }
        }

        // Save the merge state for the eventual commit
        Utils.writeContents(MERGE_HEAD, givenHash);
        index.save();

        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        } else {
            // Auto-commit if no conflicts
            commitCommand("Merged " + branchName + " into " + bm.getActiveBranchName() + ".");
        }
    }

    // Helper to handle null-safe string comparison
    private static boolean equals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private static void handleConflict(String filename, String curHash, String givHash) {
        String curContent = "";
        String givContent = "";

        if (curHash != null) {
            curContent = new String(Utils.readContents(Utils.join(BLOBS_DIR, curHash)));
        }
        if (givHash != null) {
            givContent = new String(Utils.readContents(Utils.join(BLOBS_DIR, givHash)));
        }

        String conflictContent = "<<<<<<< HEAD\n" + curContent + "=======\n" + givContent + ">>>>>>>\n";

        File file = Utils.join(CWD, filename);
        Utils.writeContents(file, conflictContent);

        // Stage the file
        addCommand(filename);
    }
}


