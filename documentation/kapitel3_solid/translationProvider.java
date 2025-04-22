public interface TranslationProvider {
    String getMessage(String key, Object... args);
    Set<String> keySet();
}