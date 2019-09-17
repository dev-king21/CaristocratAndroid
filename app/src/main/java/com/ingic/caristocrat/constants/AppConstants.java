package com.ingic.caristocrat.constants;

import android.Manifest;

public class AppConstants {
    public static final int SPLASH_DURATION = 5000;
    //public static final String BASE_URL = "http://caristocrat.me/admin/api/v1/";
    public static final String BASE_URL = "http://globalconsultingpk.com/caristocrat/admin.caristocrat.me/api/v1/";
    public static final String VIDEO_URL = "";
    public static final String WEB_URL = "https://www.caristocrat.me/";
    public static final int SELECT_IMAGE_COUNT = 1;
    public static final int SELECT_MEDIA_COUNT = 6;
    public static final int PIN_CODE_LENGTH = 4;
    public static final String OS_TYPE = "android";
    public static final String newsImageThumb = "?w=360&h=300";
    public static final String defaultCountrycode = "+971";
    public static final int FeaturedNewsTitleLimit = 60;
    public static final int NewsTitleLimit = 60;
    public static final int ENGLISH_ALPHABETS_LENGTH = 26;
    public static final int MINIMUM_AGE = 1;

    public static final long CAR_PRICE_MIN = 10000;
    public static final long CAR_PRICE_MAX = 5000000;
    public static final long CAR_YEAR_MIN = 1901;
    public static final long CAR_YEAR_MIN_RANGE = 2010;
    public static final long CAR_MIN_MILEAGE = 0;
    public static final long CAR_MAX_MILEAGE = 100000;
    public static final long CAR_APPROVED_YEAR_MIN = 2010;
    public static final long CAR_APPROVED_YEAR_MIN_RANGE = 2019;

    public static final int NOT_MOST_VIEWED = 0;
    public static final int MOST_VIEWED = 1;

    public static final int VIRTUAL_BUY_MIN_MONTHS = 12;
    public static final int VIRTUAL_BUY_MAX_MONTHS = 60;
    public static final int VIRTUAL_BUY_MIN_PERCENTAGE = 0;
    public static final int VIRTUAL_BUY_75_PERCENTAGE = 75;
    public static final int VIRTUAL_BUY_30_PERCENTAGE = 30;
    public static final int VIRTUAL_BUY_10_PERCENTAGE = 10;

    public static final String VIRTUAL_PERCENTAGE = "%";

    public static final String YES = "Yes";
    public static final String NO = "No";

    public static final String DOB_FORMAT = "yyyy-MM-dd";

    public static final String FILTER_OPEN = "filterOpen";
    public static final String FILTER_OPENED_SCREEN = "filterOpenedScreen";
    public static final int FILTER_OPENED_FILTER = 1;
    public static final int FILTER_OPENED_BRANDS = 2;
    public static final int FILTER_OPENED_MODELS = 3;

    public static final String IN_APP = "in_app_key";
    public static final String ON_ARTICLE_DETAIL = "on_article_detail";

    public static final String FROM_GUEST = "from_guest";

    public static final int REG_FEE = 565;

    public static String[] AppPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return permissions;
    }

    public static class ProfileOptions {
        public static final String COMPLETE_NOW = "COMPLETE_NOW";
        public static final String JOIN_THE_CLUB = "JOIN_THE_CLUB";
        public static final String MY_TRADE_INS = "MY_TRADES_INS";
        public static final String MY_EVALUATIONS = "MY_EVALUATIONS";
    }

    public class WebServices {
        public static final String REGISTER = "register";
        public static final String LOGIN = "login";
        public static final String REFRESH = "refresh";
        public static final String FORGET_PASSWORD = "forget-password";
        public static final String CATEGORIES = "categories";
        public static final String VERIFY_RESET_CODE = "verify-reset-code";
        public static final String RESET_PASSWORD = "reset-password";
        public static final String NEWS = "news";
        public static final String NEWS_DETAIL = "news/{id}";
        public static final String LOGOUT = "logout";
        public static final String NEWS_INTERACTIONS = "newsInteractions";
        public static final String GET_COMMENTS = "comments";
        public static final String POST_COMMENTS = "comments";
        public static final String PROFILE = "me";
        public static final String CHANGE_PASSWORD = "change-password";
        public static final String UPDATE_PROFILE = "update-profile";
        public static final String SOCIAL_LOGIN = "social-login";
        public static final String FAVORITE_NEWS = "favorite-news";
        public static final String MAKE = "carBrands";
        public static final String MODEL = "carModels";
        public static final String REGIONAL_SPECS = "regionalSpecifications";
        public static final String ATTRIBUTES = "carAttributes/{id}";
        public static final String POST_TRADE_CAR = "myCars";
        public static final String GET_CAR_BODY_TYPES = "carTypes";
        public static final String MY_TRADE_INS = "myCars";
        public static final String MY_TRADE_INS_DETAIL = "myCars/{id}";
        public static final String GET_CAR_BRANDS = "carBrands";
        public static final String LUXURY_MARKET_CATEGORIES = "makeBids";
        public static final String LUXURY_MARKET_CATEGORY_DETAIL = "makeBids/{id}";
        public static final String REGIONS = "regions";
        public static final String POST_REGIONS = "users-regions";
        public static final String CAR_INTERACTIONS = "carInteractions";
        public static final String REPORT_REQUESTS = "reportRequests";
        public static final String REQUEST_CONSULTANCY = "contactus";
        public static final String FAVORITE_CARS = "makeBids";
        public static final String GET_NOTIFICATIONS = "notifications";
        public static final String UPDATE_PUSH_NOTIFICATIION = "update-push-notification";
        public static final String POST_TRADE_IN_CAR = "tradeInCars";
        public static final String GET_TRADE_IN_CAR = "tradeInCars";
        public static final String PUT_MY_CARS = "myCars/{id}";
        public static final String CAR_FEATURES = "carFeatures";
        public static final String CAR_INTRECTION = "carInteractions";
        public static final String BANKS_RATES = "banksRates";
        public static final String ENGINE_TYPES = "engineTypes";
        public static final String GET_TRADED_IN_CAR = "tradeInCars/{id}";
        public static final String EDIT_COMMENT = "comments/{id}";
        public static final String GET_REVIEW_ASPECTS = "reviewAspects";
        public static final String REVIEWS = "reviews";
        public static final String REQUEST_CONSULTANCY_DETAIL = "settings";
        public static final String WALKTHROUGH_DETAIIL = "walkThroughs";
        public static final String NOTIFICATION_DELETE = "notifications/{id}";
        public static final String CAR_VERSIONS = "carVersions";
        public static final String PAGES = "pages";
        public static final String COMMENT_EDIT = "updatecomment/{id}";
        public static final String COMMENT_DELETE = "deletecomment/{id}";
        public static final String NEWS_V2 = "newsV2";
        public static final String GET_CARS = "makeBidsV2";
        public static final String GET_TYPE = "getType/{slug}";
        public static final String CHECK_REPORT_PAYMENT = "checkreportPayment";
        public static final String ONE_REPORT_PAYMENT = "onereportPayment";
        public static final String ALL_REPORT_PAYMENT = "allreportPayment";
        public static final String CHECK_PRO_COMPARISON_SUB = "checkprocomparisonsubsAnd/";
        public static final String PRO_COMPARISION_SUBS = "procomparisonsubs";
        public static final String GET_ALL_SUBSCRIPTION = "getSubscriptionsDotNet/{id}";
    }

    public class WebServicesKeys {
        public static final int REGISTER = 1;
        public static final int LOGIN = 2;
        public static final int REFRESH = 3;
        public static final int FORGET_PASSWORD = 4;
        public static final int CATEGORIES = 5;
        public static final int VERIFY_RESET_CODE = 6;
        public static final int NEWS = 7;
        public static final int RESET_PASSWORD = 8;
        public static final int NEWS_INTERACTIONS = 9;
        public static final int NEWS_DETAIL = 10;
        public static final int LOGOUT = 11;
        public static final int GET_COMMENTS = 12;
        public static final int POST_COMMENTS = 13;
        public static final int PROFILE = 14;
        public static final int CHANGE_PASSWORD = 15;
        public static final int UPDATE_PROFILE = 16;
        public static final int SOCIAL_LOGIN = 17;
        public static final int FAVORITE_NEWS = 18;
        public static final int MAKE = 19;
        public static final int MODEL = 20;
        public static final int REGIONAL_SPECS = 21;
        public static final int ATTRIBUTES = 22;
        public static final int POST_TRADE_CAR = 23;
        public static final int GET_CAR_BODY_TYPES = 24;
        public static final int MY_TRADE_INS = 25;
        public static final int MY_TRADE_INS_DETAIL = 26;
        public static final int GET_CAR_BRANDS = 27;
        public static final int LUXURY_MARKET_CATEGORIES = 28;
        public static final int LUXURY_MARKET_CATEGORY_DETAIL = 29;
        public static final int REGIONS = 30;
        public static final int POST_REGIONS = 31;
        public static final int CAR_INTERACTIONS = 32;
        public static final int REPORT_REQUESTS = 33;
        public static final int REQUEST_CONSULTANCY = 34;
        public static final int FAVORITE_CARS = 35;
        public static final int GET_NOTIFICATIONS = 36;
        public static final int UPDATE_PUSH_NOTIFICATIION = 37;
        public static final int POST_TRADE_IN_CAR = 38;
        public static final int GET_TRADE_IN_CAR = 39;
        public static final int PUT_MY_CARS = 40;
        public static final int CAR_FEATURES = 41;
        public static final int CAR_INTRECTION = 42;
        public static final int BANKS_RATES = 43;
        public static final int ENGINE_TYPES = 44;
        public static final int GET_TRADED_IN_CAR = 45;
        public static final int EDIT_COMMENT = 46;
        public static final int DELETE_COMMENT = 47;
        public static final int GET_REVIEW_ASPECTS = 48;
        public static final int GET_REVIEWS = 49;
        public static final int POST_REVIEWS = 50;
        public static final int REQUEST_CONSULTANCY_DETAIL = 51;
        public static final int WALKTHROUGH_DETAIIL = 52;
        public static final int NOTIFICATION_DELETE = 53;
        public static final int CAR_VERSIONS = 54;
        public static final int PAGES = 55;
        public static final int COMMENT_EDIT = 56;
        public static final int COMMENT_DELETE = 57;
        public static final int NEWS_V2 = 58;
        public static final int GET_CARS = 59;
        public static final int GET_TYPE = 60;
        public static final int CHECK_REPORT_PAYMENT = 61;
        public static final int ONE_REPORT_PAYMENT = 62;
        public static final int ALL_REPORT_PAYMENT = 63;
        public static final int PRO_COMPARISION_SUBS = 64;
        public static final int GET_ALL_SUBSCRIPTION = 65;
    }

    public class HttpStatusCodes {
        public static final int OK = 200;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int INTERNAL_SERVER = 500;
        public static final int UNVERIFIED_SIGNIN = 300;
    }

    public class NewsInteractions {
        public static final int FAVORITE = 30;
        public static final int LIKE = 20;
        public static final int VIEW = 10;
    }

    public class SocialLogin {
        public static final String FACEBOOK = "facebook";
        public static final String GOOGLE = "google";
    }

    public class ErrorsKeys {
        public static final int EMAIL = 1;
        public static final int PASSWORDS = 2;
        public static final int CONFIRM_PASSWORDS = 3;
    }

    public class TradeAttributes {
        public static final String EXTERIOR_COLOR = "Exterior Color";
        public static final String INTERIOR_COLOR = "Interior Color";
    }

    public class MainCategoriesType {
        public static final int NEWS = 10;
        public static final int COMPARISION = 20;
        public static final int LUXURY_MARKET = 30;
        public static final int CONSULTANT = 40;

        public static final String THE_OUTLET_MALL = "the-outlet-mall";
        public static final String APPROVED_PRE_OWNED = "approved-pre-owned";
        public static final String CLASSIC_CARS = "classic-cars";
        public static final String LUXURY_NEW_CARS = "luxury-new-cars";
        public static final String THE_CONSULTANT = "the-consultant";
        public static final String COMPARE = "compare";
        public static final String ASK_FOR_CONSULTANCY = "ask-for-consultancy";

        public static final String AUTO_LIFE = "auto-life";
        public static final String CAREDUCATION = "careducation";
        public static final String FOR_WOMEN_ONLY = "for-women-only";
        public static final String EVENTS = "events";

        public static final String REVIEWS = "reviews";
        public static final String EVALUATE_MY_CAR = "evaluate-my-car";
        public static final String VIN_CHECK = "vin-check";
    }

    public class TransmissionTypes {
        public static final int MANUAL = 10;
        public static final int AUTOMATIC = 20;
    }

    public class CarAttributes {
        public static final int INTERIOR_COLOR = 3;
        public static final int EXTERIOR_COLOR = 4;
        public static final int WARRANTY_REMAINING = 5;
        public static final int SERVICE_CONTRACT = 6;
        public static final int RMNG_WARRANTY = 7;
        public static final int ACCIDENT = 23;
        public static final int TRIM = 24;
    }

    public class BodyStyles {
        public static final String SALOON_4_DOORS = "Saloon 4 doors";
        public static final String SNORT_2_DOORS = "Snort 2 doors";
        public static final String SUV = "SUV";
        public static final String CONVERTABLE = "Convertable";
    }

    public class CarInteractions {
        public static final int FAVORITE = 30;
        public static final int LIKE = 20;
        public static final int VIEW = 10;
    }

    public class MediaType {
        public static final int IMAGE = 10;
        public static final int VIDEO = 20;
        public static final int WALKTHROUGH_VIDEO = 30;
    }

    public class ContactType {
        public static final int CONSULTANCY = 10;
        public static final int MY_SHOPPER = 20;
        public static final int VIRTUAL_BUY = 30;
    }

    public class CarSpecsUnits {
        public static final String MM = "MM";
        public static final String L = "L";
        public static final String KG = "KG";
        public static final String CC = "CC";
        public static final String KMH = "KM/H";
        public static final String SEC = "SEC";
        public static final String NM = "NM";
        public static final String L100 = "L/100 KM";
        public static final String GMCO = "gmCO2 / KM";
        public static final String YEARSKM = "Years / KM";
        public static final String PERCENTAGE = "%";
    }

    public class CarSpecsLuxuryNew {
        public static final String HEIGHT = "HEIGHT";
        public static final String WIDTH = "WIDTH";
        public static final String LENGTH = "LENGTH";

        public static final String TRUNK = "TRUNK";

        public static final String WEIGHT = "WEIGHT";

        public static final String DISPLACEMENT = "DISPLACEMENT";

        public static final String MAX_SPEED = "MAX SPEED";

        public static final String ACCELERATION = "Acceleration 0-100 Km";

        public static final String TORQUE = "TORQUE";

        public static final String FUEL_CONSUMBSION = "FUEL CONSUMPTION";

        public static final String EMISSION = "EMISSION";
        public static final String WARRANTY = "WARRANTY";
        public static final String MAINTENANCE_PROGRAM = "MAINTENANCE PROGRAM ";
    }

    public class Interaction {
        public static final int MAIN_CAT = 40;
        public static final int PHONE = 45;
        public static final int REQUEST = 50;
    }

    public class DealerType {
        public static final int OFFICIAL = 10;
        public static final int MARKET = 20;
    }

    public class BankRatesTypes {
        public static final int BANK = 10;
        public static final int INSURANCE = 20;
    }

    public class FcmHelper {
        public static final String FROM_SYSTEM_TRAY = "fromSystemTray";
        public static final String FCM_DATA_PAYLOAD = "fcmDataPayload";
        public static final String ACTION_TYPE_JOB = "Job";
        public static final String ACTION_TYPE_PRODUCT = "Product";
        public static final String CANCELLED = "Cancelled";
        public static final String PENDING = "Pending";
        public static final String ACCEPTED = "Accepted";
        public static final String COMPLETED = "Completed";
    }

    public class Gender {
        public static final int MALE = 10;
        public static final int FEMALE = 20;
        public static final int NOGENDER = 0;
    }

    public class MyCarThumbnailsKeys {
        public static final String FRONT = "front";
        public static final String BACK = "back";
        public static final String RIGHT = "right";
        public static final String LEFT = "left";
        public static final String INTERIOR = "interior";
        public static final String REGISTRATION_CARD = "registration_card";
    }

    public class MyCarThumnailsIds {
        public static final int FRONT = 101;
        public static final int BACK = 102;
        public static final int RIGHT = 103;
        public static final int LEFT = 104;
        public static final int INTERIOR = 105;
        public static final int REGISTRATION_BOOK = 106;
    }

    public class MyTradeInScreenTypes {
        public static final String TRADE_INS = "trade_ins";
        public static final String EVALUATION = "evaluation";
    }

    public class MyCarActions {
        public static final int TRADE = 10;
        public static final int EVALUATE = 20;
    }

    public class SortingOptions {
        public static final int LATEST_REVIEWS = 1;
        public static final int HIGHEST_REVIEWS = 2;
        public static final int LOWEST_REVIEWS = 3;
        public static final int NUMBER_REVIEWS = 4;
        public static final int NEWEST = 5;
        public static final int OLDEST = 6;
        public static final int LOWEST_PRICE = 7;
        public static final int HIGHEST_PRICE = 8;
    }

    public class PagesSlug {
        public static final String TERMS = "terms";
    }

    public class WalkthroughTypes {
        public static final int TEXT_ONLY = 10;
        public static final int IMAGE_ONLY = 20;
        public static final int VIDEO_ONLY = 30;
        public static final int TEXT_IMAGE = 40;
        public static final int TEXT_IMAGE_URL = 50;
        public static final int TEXT_VIDEO = 60;
        public static final int TEXT_VIDEO_URL = 70;
    }
}

