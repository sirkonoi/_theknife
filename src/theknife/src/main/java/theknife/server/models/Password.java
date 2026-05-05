package theknife.server.models;

public class Password {
    private String encrypted;

    public Password(String value) {
        this.encrypted = encrypt(value);
    }

    public static String encrypt(String clearValue) {
        int key = clearValue.length();
        String encrypted = "";

        for (int i = 0; i < clearValue.length(); i++) {
            char enChar = (char) (clearValue.charAt(i) + key);
            encrypted += enChar;
        }

        return encrypted;
    }

    public static String decrypt(String encryptedValue) {
        int key = encryptedValue.length();
        String decrypted = "";

        for (int i = 0; i < encryptedValue.length(); i++) {
            char enChar = (char) (encryptedValue.charAt(i) - key);
            decrypted += enChar;
        }

        return decrypted;
    }

    @Override
    public String toString() {
        return encrypted;
    }
}
