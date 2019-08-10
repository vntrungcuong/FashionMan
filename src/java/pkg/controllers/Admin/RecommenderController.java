// Decompiled using: fernflower
// Took: 181ms

package pkg.controllers.Admin;

import javax.servlet.http.HttpSession;
import pkg.entities.CustomersHelper;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import lib.RecommenderSystem;
import pkg.entities.Ratings;
import lib.RatingModel;
import java.util.Iterator;
import java.util.Map;
import pkg.entities.ItemsHelper;
import java.util.Collection;
import java.util.ArrayList;
import pkg.entities.CustomersFacadeLocal;
import pkg.entities.RatingsFacadeLocal;
import javax.ejb.EJB;
import pkg.entities.AverageRatingsFacadeLocal;
import lib.PreparedData;
import lib.Result;
import pkg.entities.Customers;
import pkg.entities.AverageRatings;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named("recommenderController")
@SessionScoped
public class RecommenderController implements Serializable {
    private double lambda;
    private int num_features;
    private int num_iters;
    private List<AverageRatings> items;
    private List<Customers> customers;
    private AverageRatings item;
    private Customers customer;
    private int num_items;
    private Result result;
    private List<AverageRatings> moviesForGuest;
    private List<AverageRatings> moviesForUser;
    private List<AverageRatings> relatedMovies;
    private PreparedData pd;
    @EJB
    private AverageRatingsFacadeLocal averageRatingsFacade;
    @EJB
    private RatingsFacadeLocal ratingsFacade;
    @EJB
    private CustomersFacadeLocal customersFacade;
    private List<Double> errorTraining;
    private List<Double> errorCV;
    
    public RecommenderController() {
        super();
        this.lambda = 0.0;
        this.num_features = 20;
        this.num_iters = 15;
        this.num_items = 0;
        this.errorTraining = new ArrayList<Double>();
        this.errorCV = new ArrayList<Double>();
    }
    
    public double getLambda() {
        return this.lambda;
    }
    
    public void setLambda(final double lambda) {
        this.lambda = lambda;
    }
    
    public int getNum_features() {
        return this.num_features;
    }
    
    public void setNum_features(final int num_features) {
        this.num_features = num_features;
    }
    
    public int getNum_iters() {
        return this.num_iters;
    }
    
    public void setNum_iters(final int num_iters) {
        this.num_iters = num_iters;
    }
    
    public Result getResult() {
        return this.result;
    }
    
    public int getNum_items() {
        return this.num_items;
    }
    
    public void setNum_items(final int num_items) {
        this.num_items = num_items;
    }
    
    public List<Customers> getCustomers() {
        if (this.customers == null) {
            this.customers = (List<Customers>)this.customersFacade.findAll();
        }
        return this.customers;
    }
    
    public Customers getCustomer() {
        return this.customer;
    }
    
    public void setCustomer(final Customers customer) {
        this.customer = customer;
    }
    
    public List<AverageRatings> getItems() {
        if (this.items == null) {
            this.items = (List<AverageRatings>)this.averageRatingsFacade.findAll();
        }
        return this.items;
    }
    
    public AverageRatings getItem() {
        return this.item;
    }
    
    public List<AverageRatings> getMoviesForGuest() {
        if (this.num_items == 0) {
            return null;
        }
        try {
            final Map<String, Double> topYmean = (Map<String, Double>)this.result.getMapOfItemsForGuest(this.num_items);
            final List<String> itemsID = new ArrayList<String>(topYmean.keySet());
            this.moviesForGuest = (List<AverageRatings>)ItemsHelper.findItems((List)this.getItems(), (List)itemsID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.moviesForGuest;
    }
    
    public List<AverageRatings> getMoviesForUser() {
        try {
            if (this.num_items <= 0 || this.customer == null) {
                return null;
            }
            final Map<String, Double> predicts = (Map<String, Double>)this.result.getItemsForUser(this.num_items, this.customer.getEmail());
            this.moviesForUser = new ArrayList<AverageRatings>();
            for (final String s : predicts.keySet()) {
                final AverageRatings ar = ItemsHelper.findItem((List)this.getItems(), s);
                ar.setAverageRating((double)predicts.get(s));
                this.moviesForUser.add(ar);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.moviesForUser;
    }
    
    public List<AverageRatings> getRelatedMovies() {
        try {
            if (this.num_items > 0 && this.item != null) {
                this.relatedMovies = (List<AverageRatings>)ItemsHelper.findItems((List)this.getItems(), this.result.getRelatedItems(this.num_items, this.item.getProductID()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.relatedMovies;
    }
    
    private void prepareDataFromDatabase() {
        final List<RatingModel> ratingModel = new ArrayList<RatingModel>();
        for (final Ratings rating : this.ratingsFacade.findAll()) {
            final String itemId = rating.getProductID().getProductID();
            final String userId = rating.getCustomerEmail().getEmail();
            final byte rate = (byte)rating.getRate();
            ratingModel.add(new RatingModel(itemId, userId, rate));
        }
        this.pd = new PreparedData((List)ratingModel, 0.7, 0.2);
    }
    
    private void prepareDataFromFiles() {
        this.pd = new PreparedData("d:/Aptech/Y.txt", "d:/Aptech/R.txt", "d:/Aptech/users.txt", "d:/Aptech/items.txt");
    }
    
    public String recommenderSystem(final boolean fromFile) {
        if (fromFile) {
            this.prepareDataFromFiles();
        }
        else {
            this.prepareDataFromDatabase();
        }
        final RecommenderSystem rs = new RecommenderSystem(this.pd);
        for (int i = 1; i <= this.num_features; ++i) {
            this.result = rs.train(this.lambda, this.num_iters, i);
            this.errorTraining.add(this.result.getTrainingError());
            this.errorCV.add(this.result.getCVError());
        }
        this.result.exportData("d:\\");
        final HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().setAttribute("trainingResult", this.result);
        request.getSession().setAttribute("lambda", this.lambda);
        request.getSession().setAttribute("num_iters", this.num_iters);
        request.getSession().setAttribute("num_features", this.num_features);
        return "RecommenderSystem";
    }
    
    public void exportTrainingData() {
        if (this.pd == null) {
            this.prepareDataFromDatabase();
        }
        this.pd.exportData("d:/Aptech/");
    }
    
    public List<Double> getCostHistory() {
        if (this.result != null) {
            return (List<Double>)this.result.getCostHistory();
        }
        return null;
    }
    
    public String onNumMoviesChanged(final ValueChangeEvent event) {
        this.num_items = Integer.parseInt(event.getNewValue().toString());
        this.getMoviesForGuest();
        this.getMoviesForUser();
        this.getRelatedMovies();
        return "index";
    }
    
    public String onSelectCustomer(final ValueChangeEvent event) {
        this.customer = CustomersHelper.findCustomer((List)this.getCustomers(), event.getNewValue().toString());
        this.getMoviesForUser();
        return "index";
    }
    
    public String onSelectMovie(final ValueChangeEvent event) {
        this.item = ItemsHelper.findItem((List)this.getItems(), event.getNewValue().toString());
        this.getRelatedMovies();
        return "index";
    }
    
    private HttpSession getSession() {
        return ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getSession();
    }
    
    public String toRS() {
        final HttpSession session = this.getSession();
        session.setAttribute("currentPage", "RS");
        return "RecommenderSystem";
    }
    
    public List<Double> getErrorTraining() {
        return this.errorTraining;
    }
    
    public List<Double> getErrorCV() {
        return this.errorCV;
    }
}
