// Decompiled using: fernflower
// Took: 178ms

package pkg.controllers.Admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.io.File;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import javax.servlet.http.Part;
import pkg.entities.Categories;
import javax.ejb.EJB;
import pkg.entities.CategoriesFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminCategoriesController")
@SessionScoped
public class adminCategoriesController implements Serializable {
    @EJB
    private CategoriesFacadeLocal categoriesFacade;
    private Categories newCat;
    private Categories selectedCat;
    private Part image;
    String oldName;
    
    public adminCategoriesController() {
        super();
        this.newCat = new Categories();
        this.selectedCat = null;
    }
    
    public List<Categories> getCategories() {
        this.getSession().setAttribute("currentPage", "categoriesAdmin");
        this.getSession().setAttribute("currentPageChild", "categoriesView");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Categories disabledCategories = this.categoriesFacade.find((Object)id);
            if (disabledCategories != null && disabledCategories.getCategoryState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledCategories.getCategoryState();
                disabledCategories.setCategoryState(!currentState);
                this.categoriesFacade.edit(disabledCategories);
            }
        }
        return (List<Categories>)this.categoriesFacade.findAll();
    }
    
    public String toCreate() {
        this.getSession().setAttribute("currentPage", "categoriesAdmin");
        this.getSession().setAttribute("currentPageChild", "categoriesCreate");
        return "categoriesCreate";
    }
    
    public String create() {
        for (final Categories c : this.categoriesFacade.findAll()) {
            if (c.getCategoryName().equalsIgnoreCase(this.newCat.getCategoryName())) {
                FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Category Name existed."));
                return "categoriesCreate";
            }
        }
        String catID = "";
        final Integer numOfCategories = this.categoriesFacade.count() + 1;
        switch (numOfCategories.toString().length()) {
            case 1: {
                catID = "CA00" + numOfCategories;
                break;
            }
            case 2: {
                catID = "CA0" + numOfCategories;
                break;
            }
            case 3: {
                catID = "CA" + numOfCategories;
                break;
            }
        }
        String images = "images/logo.jpg";
        if (this.image != null && this.image.getSize() > 0L) {
            images = "images/Categories/" + this.copyImage(catID, this.image);
        }
        this.newCat.setCategoryID(catID);
        this.newCat.setCategoryImage(images);
        this.newCat.setCategoryState(true);
        this.categoriesFacade.create(this.newCat);
        this.image = null;
        this.newCat = new Categories();
        return "categoriesView";
    }
    
    private String copyImage(final String catID, Part file) {
        final String dirPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/Categories").replace("build\\web", "web");
        final String fullFilename = file.getSubmittedFileName();
        final int lastIndex = fullFilename.lastIndexOf(46);
        final String extension = fullFilename.substring(lastIndex + 1, fullFilename.length());
        final String filename = catID + "." + extension;
        try (final InputStream input = file.getInputStream()) {
            Files.copy(input, new File(dirPath + "/" + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
            file = null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return filename;
    }
    
    public Categories getSelectedCategories() {
        if (this.getRequest().getParameter("id") != null) {
            this.selectedCat = this.categoriesFacade.find((Object)this.getRequest().getParameter("id"));
            this.oldName = new String(this.selectedCat.getCategoryName());
        }
        return this.selectedCat;
    }
    
    public String update() {
        if (!this.oldName.equalsIgnoreCase(this.selectedCat.getCategoryName())) {
            for (final Categories c : this.categoriesFacade.findAll()) {
                if (c.getCategoryName().equalsIgnoreCase(this.selectedCat.getCategoryName())) {
                    FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Category Name existed."));
                    return "categoriesUpdate";
                }
            }
        }
        if (this.image != null && this.image.getSize() > 0L) {
            this.selectedCat.setCategoryImage("images/Categories/" + this.copyImage(this.selectedCat.getCategoryID(), this.image));
        }
        this.categoriesFacade.edit(this.selectedCat);
        this.selectedCat = null;
        this.image = null;
        return "categoriesView";
    }
    
    private HttpSession getSession() {
        return this.getRequest().getSession();
    }
    
    private HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public Categories getNewCat() {
        return this.newCat;
    }
    
    public void setNewCat(final Categories newCat) {
        this.newCat = newCat;
    }
    
    public Part getImage() {
        return this.image;
    }
    
    public void setImage(final Part image) {
        this.image = image;
    }
}
