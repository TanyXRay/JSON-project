package utils.exceptions;

public class SettingsFileReadException extends RuntimeException {
    public SettingsFileReadException(Exception e) {
        super("Settings file (shop.xml) can't read!", e);
    }
}

