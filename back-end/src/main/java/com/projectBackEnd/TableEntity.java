package main.java.com.projectBackEnd;

import java.io.Serializable;

public interface TableEntity {
    public Serializable getPrimaryKey();
    public TableEntity imitate(TableEntity newCopy);
}
