// CommandController.java
private final Map<Commands, CommandService> commandMap; 
 
public CommandController() 
{ 
 
    commandMap = new HashMap<>(); 
 
    commandMap.put(Commands.EXIT, new ExitCommandService()); 
    commandMap.put(Commands.HELP, new HelpCommandService()); 
    commandMap.put(Commands.BILANZIUS, new BilanziusCommandService()); 
 
} 
 
public String handleInput(String input) 
{ 
 
    String[] parts = input.split(" ", 2); 
    String commandStr = parts[0]; 
    String[] arguments = parts.length > 1 ? parts[1].split(" ") : new String[0]; 
 
    Commands command = Commands.fromString(commandStr); 
    CommandService commandService = commandMap.get(command); 
 
    if (commandService != null) { 
        return commandService.execute(arguments); 
    } 
 
    return "Unknown command :( . Type /help for a list of commands."; 
} 

// Commands.java
public enum Commands 
{ 
    EXIT("/exit", "Exit the application", null), 
    HELP("/help", "Show all commands", null), 
    BILANZIUS("/bilanzius", "Get information about the application", BilanziusCommandArguments.getAllArguments()); 
 
    // Hier werden die einzelnen Befehle hinzugefügt 
 
    private final String command; 
    private final String description; 
    private final String arguments; 
 
    Commands(String command, String description, String arguments) { 
        this.command = command; 
        this.description = description; 
        this.arguments = arguments; 
    } 
 
    public String getCommand() { 
        return command; 
    } 
 
    public String getDescription() { 
        return description; 
    } 
 
    public String getArguments() { 
        return arguments; 
    } 
 
    public static String getAllCommands() { 
 
        return Arrays.stream(Commands.values()).map( 
                c -> c.getCommand() 
                        + " - " + 
                        c.getDescription() 
                        + 
                        ( 
                                c.getArguments() 
                                        != null ? 
                                        " | " + (c.getArguments()) 
                                        : "" 
                        ) 
                ).reduce( 
                        (a, b) -> a + "\n" + b 
                ).orElse(""); 
    } 
 
    public static Commands fromString(String command) { 
        for (Commands c : Commands.values()) { 
            if (c.command.equals(command)) { 
                return c; 
            } 
        } 
        return null; 
    } 
}

public interface CommandService 
{ 
    String execute(String[] arguments); 
} 