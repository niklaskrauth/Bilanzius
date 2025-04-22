public interface SimpleCommand {
    String execute();
}

public interface ArgumentCommand {
    String execute(String[] args);
}