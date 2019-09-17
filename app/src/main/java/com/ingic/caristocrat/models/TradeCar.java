package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.models.WebRequestData;

import java.util.ArrayList;

/**
 */
public class TradeCar extends WebRequestData {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone")
    @Expose
    private Integer phone;
    @SerializedName("chassis")
    @Expose
    private String chassis;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("owner")
    @Expose
    private User user;
    @SerializedName("car_type")
    @Expose
    private CarType car_type;
    @SerializedName("engine_type")
    @Expose
    private EngineType engine_type;
    @SerializedName("media")
    @Expose
    private ArrayList<Media> media;
    @SerializedName("bids")
    @Expose
    private ArrayList<Bid> bids;
    @SerializedName("top_bids")
    @Expose
    private ArrayList<Bid> top_bids;
    @SerializedName("car_model")
    @Expose
    private Model model;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("average_mkp")
    @Expose
    private Double amount_mkp;
    @SerializedName("kilometer")
    @Expose
    private Double kilometre;
    @SerializedName("my_car_attributes")
    @Expose
    private ArrayList<MyCarAttributes> my_car_attributes;
    @SerializedName("specification")
    @Expose
    private ArrayList<MyCarAttributes> specification;
    @SerializedName("regional_specs")
    @Expose
    private RegionalSpecs regional_specs;
    @SerializedName("is_liked")
    @Expose
    private boolean is_liked;
    @SerializedName("is_viewed")
    @Expose
    private boolean is_viewed;
    @SerializedName("is_favorite")
    @Expose
    private boolean is_favorite;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("views_count")
    @Expose
    private int views_count;
    @SerializedName("limited_edition_specs_array")
    @Expose
    private LuxuryNewCarsSpecs limitedEditionSpecsArray;
    @SerializedName("car_regions")
    @Expose
    private ArrayList<RegionalPrice> regionalPrices;
    @SerializedName("life_cycle")
    @Expose
    private String life_cycle;
    @SerializedName("depreciation_trend")
    @Expose
    private ArrayList<DepricationAmount> depricationAmount = new ArrayList<>();
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("my_car_features")
    @Expose
    private ArrayList<CarFeature> myCarFeatures;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("is_featured")
    @Expose
    private int featured;
    @SerializedName("average_rating")
    @Expose
    private float average_rating;
    @SerializedName("isSelected")
    @Expose
    boolean isSelected;
    @SerializedName("is_reviewed")
    @Expose
    private int is_reviewed;
    @SerializedName("reviews")
    @Expose
    private ArrayList<ReviewDetail> reviewDetails;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("review_count")
    @Expose
    private int review_count;
    @SerializedName("version")
    @Expose
    private Version version;
    @SerializedName("dealers")
    @Expose
    private ArrayList<User> dealers;
    @SerializedName("version_app")
    @Expose
    private String versionApp;
    @SerializedName("link2")
    @Expose
    private String deeplink;

    public int getIs_reviewed() {
        return is_reviewed;
    }

    public void setIs_reviewed(int is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public int getFeatured() {
        return featured;
    }

    public void setFeatured(int featured) {
        this.featured = featured;
    }

    private boolean newlyAdded;

    public boolean isNewlyAdded() {
        return newlyAdded;
    }

    public void setNewlyAdded(boolean newlyAdded) {
        this.newlyAdded = newlyAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CarType getCar_type() {
        return car_type;
    }

    public void setCar_type(CarType car_type) {
        this.car_type = car_type;
    }

    public EngineType getEngine_type() {
        return engine_type;
    }

    public void setEngine_type(EngineType engine_type) {
        this.engine_type = engine_type;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ArrayList<Bid> getTop_bids() {
        return top_bids;
    }

    public void setTop_bids(ArrayList<Bid> top_bids) {
        this.top_bids = top_bids;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmount_mkp() {
        return amount_mkp;
    }

    public void setAmount_mkp(Double amount_mkp) {
        this.amount_mkp = amount_mkp;
    }

    public Double getKilometre() {
        return kilometre;
    }

    public void setKilometre(Double kilometre) {
        this.kilometre = kilometre;
    }

    public ArrayList<MyCarAttributes> getMy_car_attributes() {
        return my_car_attributes;
    }

    public void setMy_car_attributes(ArrayList<MyCarAttributes> my_car_attributes) {
        this.my_car_attributes = my_car_attributes;
    }

    public RegionalSpecs getRegional_specs() {
        return regional_specs;
    }

    public void setRegional_specs(RegionalSpecs regional_specs) {
        this.regional_specs = regional_specs;
    }

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public boolean isIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(boolean is_viewed) {
        this.is_viewed = is_viewed;
    }

    public boolean isIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public LuxuryNewCarsSpecs getLimitedEditionSpecsArray() {
        return limitedEditionSpecsArray;
    }

    public void setLimitedEditionSpecsArray(LuxuryNewCarsSpecs limitedEditionSpecsArray) {
        this.limitedEditionSpecsArray = limitedEditionSpecsArray;
    }

    public ArrayList<RegionalPrice> getRegionalPrices() {
        return regionalPrices;
    }

    public void setRegionalPrices(ArrayList<RegionalPrice> regionalPrices) {
        this.regionalPrices = regionalPrices;
    }

    public String getLife_cycle() {
        return life_cycle;
    }

    public void setLife_cycle(String life_cycle) {
        this.life_cycle = life_cycle;
    }

    public ArrayList<DepricationAmount> getDepricationAmount() {
        return depricationAmount;
    }

    public void setDepricationAmount(ArrayList<DepricationAmount> depricationAmount) {
        this.depricationAmount = depricationAmount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<CarFeature> getMyCarFeatures() {
        return myCarFeatures;
    }

    public void setMyCarFeatures(ArrayList<CarFeature> myCarFeatures) {
        this.myCarFeatures = myCarFeatures;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<ReviewDetail> getReviewDetails() {
        return reviewDetails;
    }

    public void setReviewDetails(ArrayList<ReviewDetail> reviewDetails) {
        this.reviewDetails = reviewDetails;
    }

    public ArrayList<MyCarAttributes> getSpecification() {
        return specification;
    }

    public void setSpecification(ArrayList<MyCarAttributes> specification) {
        this.specification = specification;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public ArrayList<User> getDealers() {
        return dealers;
    }

    public void setDealers(ArrayList<User> dealers) {
        this.dealers = dealers;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }
}
