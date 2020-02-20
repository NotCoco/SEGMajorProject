package main.java.com.projectBackEnd.Entities.Page;


import main.java.com.projectBackEnd.TableEntity;

import java.io.Serializable;
import java.util.List;

/**
 * All Page related functions for querying the database that won't be recognised by REST
 * //TODO Filter the interfaces!
 */
public interface PageManagerInterface {
    public Page addPage(String slug, Integer index, String title, String content);

    public void deleteAll();

    //TODO Explicit cast these so that they always come out correct?
    public List<Page> getAllPages();
    public Page addPage(Page newPage);
    public Page getByPrimaryKey(Serializable pk);
    public void delete(Page object);
    public Page update(Page updatedCopy);
    //TODO Ask L2, because they can technically move down.
}
