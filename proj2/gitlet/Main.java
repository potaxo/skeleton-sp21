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
