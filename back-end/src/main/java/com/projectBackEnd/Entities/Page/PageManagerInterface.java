package main.java.com.projectBackEnd.Entities.Page;


import main.java.com.projectBackEnd.Page;
import main.java.com.projectBackEnd.TableEntity;

import java.io.Serializable;
import java.util.List;

/**
 * All Page related functions for querying the database that won't be recognised by REST
 * //TODO Filter the interfaces!
 */
public interface PageManagerInterface <T extends TableEntity>  {
    public Page createPage(String slug, Integer index, String title, String content);
    public Page createAndSavePage(String slug, Integer index, String title, String content);

    public void deleteAll();

    //TODO Explicit cast these so that they always come out correct?
    public List<T> getAll();
    public T insertTuple(T newObject);
    public T getByPrimaryKey(Serializable pk);
    public void delete(T object);
    public T update(T updatedCopy);
    //TODO Ask L2, because they can technically move down.
}
