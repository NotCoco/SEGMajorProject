package main.java.com.projectBackEnd.Entities.Page;


import main.java.com.projectBackEnd.TableEntity;

import java.io.Serializable;
import java.util.List;

/**
 * All Page related functions for querying the database that won't be recognised by REST
 * //TODO Filter the interfaces!
 */
public interface PageManagerInterface {
    //TODO Technically these are shared two methods that could come from a super Interface.
    public void delete(Serializable primaryKey);
    public void deleteAll();

    public List<Page> getAllPages();
    public Page addPage(Page newPage);
    public Page getByPrimaryKey(Serializable pk);
    public void delete(Page object);
    public Page update(Page updatedCopy);
    public Page addPage(String slug, Integer index, String title, String content);

}

