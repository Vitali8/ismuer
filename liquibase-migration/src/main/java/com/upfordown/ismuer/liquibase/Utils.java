package com.upfordown.ismuer.liquibase;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.upfordown.ismuer.liquibase.model.ChangeLog;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<String, String> CQL_COMMENTS = new HashMap<>() {{
        put("//", "\n");
        put("--", "\n");
        put("/*", "*/");
    }};

    private static final String CQL_QUOTE = "'";

    public static String md5(final String value) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public static ChangeLog convertToChangeLog(final Row row) {
        final ChangeLog changeLog = new ChangeLog();

        changeLog.setKeyspace(row.getString("keyspace_name"));
        changeLog.setFileName(row.getString("file_name"));
        changeLog.setExecutedAt(row.getLong("executed_at"));
        changeLog.setMd5sum(row.getString("md5sum"));
        changeLog.setOrderExecuted(row.getInt("order_executed"));

        return changeLog;
    }

    public static String removeComments(final String cqlScript) {
        boolean stringStarts = false;
        String commentStart = null;

        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < cqlScript.length(); i++) {
            final String currentChar = Character.toString(cqlScript.charAt(i));
            // It's string starts
            final boolean inComment = commentStart != null && !commentStart.isEmpty();
            if (!inComment && currentChar.equals(CQL_QUOTE)) {
                stringStarts = !stringStarts;
                // Append only if not in comment
                result.append(currentChar);
                continue;
            }

            // We are in string, append (if not in comment)
            if (stringStarts && !inComment) {
                result.append(currentChar);
                continue;
            }

            // Check, maybe comment starts
            if (!inComment && i < cqlScript.length() - 1) {
                final String doubleChar = currentChar + cqlScript.charAt(i + 1);
                if (CQL_COMMENTS.containsKey(doubleChar)) {
                    commentStart = doubleChar;
                    // Increase, as we have 2 symbols
                    i++;
                    continue;
                }
            }

            // Check, maybe comment ends
            if (inComment) {
                final String closer = CQL_COMMENTS.get(commentStart);
                if (currentChar.equals(closer)) {
                    commentStart = null;
                    continue;
                }
                // Maybe it has 2 symbols
                if (closer.length() == 2 && i < cqlScript.length() - 1) {
                    final String doubleChar = currentChar + cqlScript.charAt(i + 1);
                    if (doubleChar.equals(closer)) {
                        commentStart = null;
                        // Increase, as we have 2 symbols
                        i++;
                        continue;
                    }
                }
            }

            if (!inComment) {
                // It's normal symbol, just append
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    public static boolean isTableCreated(final CqlSession cqlSession, final String tableName, final String keyspace) {
        return cqlSession.getMetadata().getKeyspace(keyspace).flatMap(ks -> ks.getTable(tableName)).isPresent();
    }
}

