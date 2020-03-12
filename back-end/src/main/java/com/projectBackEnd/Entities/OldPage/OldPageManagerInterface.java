package main.java.com.projectBackEnd.Entities.OldPage;


import java.io.Serializable;
import java.util.List;

/**
 * All OldPage related functions for querying the database that won't be recognised by REST
 * //TODO Filter the interfaces!
 */
public interface OldPageManagerInterface {
    //TODO Technically these are shared two methods that could come from a super Interface.
    public void delete(Serializable primaryKey);
    public void deleteAll();

    public List<OldPage> getAllPages();
    public OldPage addOldPage(OldPage newOldPage);
    public OldPage getByPrimaryKey(Serializable pk);
    public void delete(OldPage object); //TODO Is unnecessary now, can be removed
    public OldPage update(OldPage updatedCopy);
    public OldPage addOldPage(String slug, Integer index, String title, String content);

}

