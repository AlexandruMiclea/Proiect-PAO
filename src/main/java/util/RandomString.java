package util;
public class RandomString {
    public static String getRandomString() {
        String characterString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz"
                + ",./;'[]<>?:{}()";

        StringBuilder sb = new StringBuilder(20);

        for (int i = 0; i < 20; i++) {
            int index = (int) (characterString.length() * Math.random());
            sb.append(characterString.charAt(index));
        }

        return sb.toString();
    }
}
