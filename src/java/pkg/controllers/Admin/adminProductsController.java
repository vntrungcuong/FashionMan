// Decompiled using: fernflower
// Took: 498ms

package pkg.controllers.Admin;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.CopyOption;
import java.io.File;
import pkg.entities.Brands;
import pkg.entities.Types;
import pkg.entities.Categories;
import java.util.Iterator;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;
import javax.servlet.http.Part;
import pkg.entities.Products;
import pkg.entities.ProductsFacadeLocal;
import pkg.entities.CategoriesFacadeLocal;
import pkg.entities.BrandsFacadeLocal;
import javax.ejb.EJB;
import pkg.entities.TypesFacadeLocal;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("adminProductsController")
@SessionScoped
public class adminProductsController implements Serializable {
    @EJB
    private TypesFacadeLocal typesFacade;
    @EJB
    private BrandsFacadeLocal brandsFacade;
    @EJB
    private CategoriesFacadeLocal categoriesFacade;
    @EJB
    private ProductsFacadeLocal productsFacade;
    private Products newPro;
    private Products selectedPro;
    private String categoryID;
    private String typeID;
    private String brandID;
    private Part[] image;
    String oldName;
    
    public adminProductsController() {
        super();
        this.newPro = new Products();
        this.selectedPro = null;
        this.image = new Part[4];
    }
    
    public String toCreate() {
        this.getSession().setAttribute("currentPage", "productsAdmin");
        this.getSession().setAttribute("currentPageChild", "productsCreate");
        this.resetProducts();
        return "productsCreate";
    }
    
    public List<Products> getProducts() {
        this.getSession().setAttribute("currentPage", "productsAdmin");
        this.getSession().setAttribute("currentPageChild", "productsView");
        final String action = this.getRequest().getParameter("action");
        final String id = this.getRequest().getParameter("id");
        final String state = this.getRequest().getParameter("state");
        if (action != null && id != null && state != null && action.equals("disable")) {
            final Products disabledProducts = this.productsFacade.find((Object)id);
            if (disabledProducts != null && disabledProducts.getProductState().toString().toLowerCase().equals(state.toLowerCase())) {
                final boolean currentState = disabledProducts.getProductState();
                disabledProducts.setProductState(!currentState);
                this.productsFacade.edit(disabledProducts);
            }
        }
        return (List<Products>)this.productsFacade.findAll();
    }
    
    public String create() {
        for (final Products p : this.productsFacade.findAll()) {
            if (p.getProductName().equalsIgnoreCase(this.newPro.getProductName())) {
                FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Name existed."));
                return "productsCreate";
            }
        }
        String productID = "";
        final Integer numOfProducts = this.productsFacade.count() + 1;
        switch (numOfProducts.toString().length()) {
            case 1: {
                productID = "PR00" + numOfProducts;
                break;
            }
            case 2: {
                productID = "PR0" + numOfProducts;
                break;
            }
            case 3: {
                productID = "PR" + numOfProducts;
                break;
            }
        }
        final Categories cat = this.categoriesFacade.find((Object)this.categoryID);
        final Types type = this.typesFacade.find((Object)this.typeID);
        final Brands brand = this.brandsFacade.find((Object)this.brandID);
        final StringBuilder images = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            String filename = "defaultProduct.jpg" + ((i == 3) ? "" : ";");
            if (this.image[i] != null && this.image[i].getSize() > 0L) {
                filename = this.copyImage(productID.toLowerCase() + "-" + (i + 1), this.image[i]) + ((i == 3) ? "" : ";");
            }
            images.append(filename);
        }
        this.newPro.setProductID(productID);
        this.newPro.setCategoryID(cat);
        this.newPro.setTypeID(type);
        this.newPro.setBrandID(brand);
        this.newPro.setProductImages(new String(images));
        this.newPro.setProductState(true);
        this.productsFacade.create(this.newPro);
        this.resetProducts();
        return "productsView";
    }
    
    public Products getSelectedProduct() {
        if (this.getRequest().getParameter("id") != null) {
            this.selectedPro = this.productsFacade.find((Object)this.getRequest().getParameter("id"));
            if (this.selectedPro != null) {
                this.oldName = new String(this.selectedPro.getProductName());
                this.categoryID = this.selectedPro.getCategoryID().getCategoryID();
                this.typeID = this.selectedPro.getTypeID().getTypeID();
                this.brandID = this.selectedPro.getBrandID().getBrandID();
            }
        }
        return this.selectedPro;
    }
    
    public String update() {
        if (!this.oldName.equalsIgnoreCase(this.selectedPro.getProductName())) {
            for (final Products p : this.productsFacade.findAll()) {
                if (p.getProductName().equalsIgnoreCase(this.selectedPro.getProductName())) {
                    FacesContext.getCurrentInstance().addMessage("profileForm", new FacesMessage("Name existed."));
                    return "productsUpdate";
                }
            }
        }
        final Categories cat = this.categoriesFacade.find((Object)this.categoryID);
        final Types type = this.typesFacade.find((Object)this.typeID);
        final Brands brand = this.brandsFacade.find((Object)this.brandID);
        final StringBuilder images = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            String filename = this.selectedPro.getProductImages().split(";")[i] + ((i == 3) ? "" : ";");
            if (this.image[i] != null && this.image[i].getSize() > 0L) {
                filename = this.copyImage(this.selectedPro.getProductID().toLowerCase() + "-" + (i + 1), this.image[i]) + ((i == 3) ? "" : ";");
            }
            images.append(filename);
        }
        this.selectedPro.setCategoryID(cat);
        this.selectedPro.setTypeID(type);
        this.selectedPro.setBrandID(brand);
        if (images.length() > 0) {
            this.selectedPro.setProductImages(new String(images));
        }
        this.productsFacade.edit(this.selectedPro);
        this.resetProducts();
        return "productsView";
    }
    
    private String copyImage(final String productID, Part file) {
        final String dirPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/Products").replace("build\\web", "web");
        final String fullFilename = file.getSubmittedFileName();
        final int lastIndex = fullFilename.lastIndexOf(46);
        final String extension = fullFilename.substring(lastIndex + 1, fullFilename.length());
        final String filename = productID + "." + extension;
        try (final InputStream input = file.getInputStream()) {
            Files.copy(input, new File(dirPath + "/" + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
            file = null;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return filename;
    }
    
    private void resetProducts() {
        this.newPro = new Products();
        this.selectedPro = new Products();
        this.categoryID = "";
        this.typeID = "";
        this.brandID = "";
        this.image = new Part[4];
    }
    
    private HttpSession getSession() {
        return this.getRequest().getSession();
    }
    
    private HttpServletRequest getRequest() {
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
    
    public List<Categories> getCategories() {
        return (List<Categories>)this.categoriesFacade.findAll();
    }
    
    public List<Types> getTypes() {
        return (List<Types>)this.typesFacade.findAll();
    }
    
    public List<Brands> getBrands() {
        return (List<Brands>)this.brandsFacade.findAll();
    }
    
    public Products getNewPro() {
        return this.newPro;
    }
    
    public void setNewPro(final Products newPro) {
        this.newPro = newPro;
    }
    
    public String getCategoryID() {
        return this.categoryID;
    }
    
    public void setCategoryID(final String categoryID) {
        this.categoryID = categoryID;
    }
    
    public String getTypeID() {
        return this.typeID;
    }
    
    public void setTypeID(final String typeID) {
        this.typeID = typeID;
    }
    
    public String getBrandID() {
        return this.brandID;
    }
    
    public void setBrandID(final String brandID) {
        this.brandID = brandID;
    }
    
    public Part[] getImage() {
        return this.image;
    }
    
    public void setImage(final Part[] image) {
        this.image = image;
    }
}
