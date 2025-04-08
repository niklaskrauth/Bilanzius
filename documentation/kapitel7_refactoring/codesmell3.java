public CommandController() 
{
} 
 
public String handleInput(String input) 
{ 
 
    String output; 
 
    switch (input) { 
        case "/exit": 
            System.exit(0); 
            output = "Goodbye User"; 
            break; 
        case "/help": 
            HelpCommandService helpCommandService = new HelpCommandService(); 
            output = helpCommandService.getAllCommands(); 
            break; 
        default: 
            output = "Something went wrong :( "; 
            break; 
    } 
 
    return output; 
} 