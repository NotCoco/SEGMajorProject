package main.java.com.projectBackEnd;

//TODO Make this a static so it's easily called from all the other places? I.e. No interface
/**
 * Object that takes a string and converts it into one that is safe for SQL parsing.
 */
public class SQLSafeString implements SafeString {
    private String safeVersion;

    /**
     * Given parameter in constructor is converted into field variable for access via toString()
     * @param unsafeString The string that might be unsafe
     */
    public SQLSafeString(String unsafeString) {
        safeVersion = escapeString(unsafeString);
    }

    /**
     * Converts a string to a safe string
     * @param toConvert The old string
     * @return The safe string
     */
    public String escapeString(String toConvert) { return toConvert; }

    /**
     * @return The safe string
     */
    public String toString() {
        return safeVersion;
    }
}
