package main.java.com.projectBackEnd;

import java.io.Serializable;

/**
 * Interface implemented by all database entity table classes
 */
public interface TableEntity {

    Serializable getPrimaryKey();
    TableEntity copy(TableEntity newCopy);

}
