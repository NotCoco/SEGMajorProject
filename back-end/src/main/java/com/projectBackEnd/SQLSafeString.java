package main.java.com.projectBackEnd;

public class SQLSafeString implements SafeString {
    private String safeVersion;
    public SQLSafeString(String unsafeString) {
        safeVersion = escapeString(unsafeString);
    }
    public String escapeString(String toConvert) { return toConvert; }
    public String toString() {
        return safeVersion;
    }
}
