package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (!validateNumArgs("init", args, 1)) {
                    System.out.println("Incorrect operands, args should be 1");
                    return;
                }
                Repository.initCommand();
                break;

            case "add":
                // handle the `add [filename]` command
                if (!validateNumArgs("add", args, 2)) {
                    System.out.println("Incorrect operands, args should be 2");
                    return;
                }
                String filename = args[1];
                Repository.addCommand(filename);
                break;

            case "commit":
                if (args.length == 1) {
                    System.out.println("Please enter a commit message.");
                    return;
                } else if (Index.isIndexEmpty()) {
                    System.out.println("No changes added to the commit.");
                    return;
                }
                validateNumArgs("commit", args, 2);
                String message = args[1];
                Repository.commitCommand(message);
                break;

            case "rm":
                if (!validateNumArgs("rm", args, 2)) {
                    System.out.println("Incorrect operands."); // Or your preferred message
                    return;
                }
                Repository.rmCommand(args[1]);
                break;
            // --- ADD THESE NEW CASES ---
            case "log":
                if (!validateNumArgs("log", args, 1)) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.logCommand();
                break;

            case "global-log":
                if (!validateNumArgs("global-log", args, 1)) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.globalLogCommand();
                break;

            case "find":
                if (!validateNumArgs("find", args, 2)) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.findCommand(args[1]);
                break;

            case "branch":
                if (!validateNumArgs("branch", args, 2)) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.branchCommand(args[1]);
                break;

            case "checkout":
                // Case 3: checkout [branch name] (length 2)
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1]);
                }
                // Case 1: checkout -- [file name] (length 3)
                else if (args.length == 3) {
                    if (!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    Repository.checkoutFileFromHead(args[2]);
                }
                // Case 2: checkout [commit id] -- [file name] (length 4)
                else if (args.length == 4) {
                    if (!args[2].equals("--")) {
                        System.out.println("Incorrect operands.");
                        return;
                    }
                    Repository.checkoutFileFromCommit(args[1], args[3]);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;

            case "rm-branch":
                if (!validateNumArgs("rm-branch", args, 2)) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.rmBranchCommand(args[1]);
                break;

            case "reset":
                if (!validateNumArgs("reset", args, 2)) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.resetCommand(args[1]);
                break;
            // TODO: FILL THE REST IN

            default:
                System.out.println("No command with that name exists.");
                break;
        }
    }
    /**
     * Helper function to check if the number of arguments is correct.
     * Exits with an error message if not.
     *
     * @param cmd The command name (e.g., "init")
     * @param args The args array
     * @param expected The expected number of arguments (including the command itself)
     */
    public static boolean validateNumArgs(String cmd, String[] args, int expected) {
        if (args.length != expected) {
            return false;
        } else {
            return true;
        }
    }
}
