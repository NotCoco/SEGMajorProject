package main.java.com.projectBackEnd;

import java.io.Serializable;

/**
 * Interface implemented by all database entity tables
 */
public interface TableEntity {
    Serializable getPrimaryKey();
    TableEntity copy(TableEntity newCopy);
}
