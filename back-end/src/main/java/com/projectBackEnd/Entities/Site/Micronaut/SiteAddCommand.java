package main.java.com.projectBackEnd.Entities.Site.Micronaut;

import javax.validation.constraints.NotNull;
//import io.micronaut.core.annotation.Introspected;

/**
 * SiteAddComment is an implementation of the Command design pattern.
 * It creates mock Site objects and reduced memory use.
 * It is used by the controller to insert a Site object into the database
 */
//TODO Reinsert tags as I couldn't get them to compile on IntelliJ Jars.
//@Introspected
public class SiteAddCommand {
	
	// Name of the site
    @NotNull
    private String name;
	
	// Slug of the site for identification
    @NotNull
    private String slug;

    /**
     * Default constructor
     */
    public SiteAddCommand(){}

    /**
     * Main constructor for SiteAddCommand objects creation
     * @param slug  Slug of the mock Site
     * @param name  Name of the mock Site
     */
    public SiteAddCommand(String slug, String site){
        this.slug = slug;
        this.name = site;
    }

    /**
     * Get the name of the Site
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Set the name of the site as given name
     * @param name New name value
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the slug of the Site
     * @return slug
     */
    public String getSlug() { return slug; }

    /**
     * Set the slug of the Site as the given slug
     * @param slug  New slug value
     */
    public void setSlug(String slug) { this.slug = slug; }
}
