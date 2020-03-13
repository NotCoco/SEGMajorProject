package main.java.com.projectBackEnd;

import java.io.Serializable;


public interface TableEntity {
    Serializable getPrimaryKey();
    TableEntity copy(TableEntity newCopy);
}
