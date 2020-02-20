package main.java.com.projectBackEnd;

import java.io.Serializable;

public interface TableEntity {
    Serializable getPrimaryKey();
    TableEntity imitate(TableEntity newCopy);
}
