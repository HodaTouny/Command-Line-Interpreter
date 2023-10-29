public class Parser {
    private String commandName;
    private String[] args;
    public boolean parse(String input) {
        String[] tokens = input.trim().split("\\s+");
        if (tokens.length > 0) {
            commandName = tokens[0];
            if (tokens.length > 1 && (tokens[1].startsWith("-")||tokens[1].startsWith(">"))) {
                commandName += " " + tokens[1];
                args = new String[tokens.length - 2];
                System.arraycopy(tokens, 2, args, 0, tokens.length - 2);
            } else {
                args = new String[tokens.length - 1];
                System.arraycopy(tokens, 1, args, 0, tokens.length - 1);
            }
            return true;
        }
        return false;
    }


    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}

