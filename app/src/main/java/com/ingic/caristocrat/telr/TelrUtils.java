package com.ingic.caristocrat.telr;

import android.content.Context;
import android.content.Intent;

import com.ingic.caristocrat.webhelpers.models.User;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.request.payment.Address;
import com.telr.mobile.sdk.entity.request.payment.App;
import com.telr.mobile.sdk.entity.request.payment.Billing;
import com.telr.mobile.sdk.entity.request.payment.MobileRequest;
import com.telr.mobile.sdk.entity.request.payment.Name;
import com.telr.mobile.sdk.entity.request.payment.Tran;

import java.math.BigInteger;
import java.util.Random;

public class TelrUtils {
    public static final String KEY = "MPz3Z#HWX6~84CgP";        // TODO: Insert your Key here
    public static final String STORE_ID = "22120";    // TODO: Insert your Store ID here

    public static void IntentTelr(String amount, Context cxt, User user) {
        Intent intent = new Intent(cxt, WebviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest(amount,user));
        intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.ingic.caristocrat.telr.FailedTransaction");
        intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.ingic.caristocrat.telr.SuccessTransaction");
        intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, true);
        cxt.startActivity(intent);
    }

    static MobileRequest getMobileRequest(String amount,User user) {
        MobileRequest mobile = new MobileRequest();
        mobile.setStore(STORE_ID);                       // Store ID
        mobile.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("123456789");                          // Application installation ID
        app.setName("Caristocrat");                    // Application name
        app.setUser("" + user.getId());                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("1.0");                         // Application version
        app.setSdk("123");
        mobile.setApp(app);
        Tran tran = new Tran();
        tran.setTest("1");                              // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("sale");                           /* Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */
        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Transaction from android device");         // Transaction description
        tran.setCurrency(user.getDetails().getUserRegionDetail().getCurrency());                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(amount);                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //tran.setRef(???);                           // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
        tran.setLangauge("en");                        // (Optinal) default is en -> English
        mobile.setTran(tran);
        Billing billing = new Billing();
        Address address = new Address();
        address.setCity("Dubai");                       // City : the minimum required details for a transaction to be processed
        address.setCountry("AE");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion(user.getDetails().getUserRegionDetail().getName());                     // Region

        address.setLine1(user.getDetails().getAddress() != null ? user.getDetails().getAddress() : "No Address");                 // Street address – line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address);
        Name name = new Name();
        name.setFirst(user.getDetails().getFirstName() != null ? user.getDetails().getFirstName() : "No First Name");                          // Forename : the minimum required details for a transaction to be processed
        name.setLast(user.getDetails().getLastName() != null ? user.getDetails().getLastName() : "No Last Name");                          // Surname : the minimum required details for a transaction to be processed
        name.setTitle("Mr");                           // Title
        billing.setName(name);
        billing.setEmail(user.getEmail());
        billing.setPhone(user.getDetails().getPhone() != null ? user.getDetails().getPhone() : "No Phone Number");                // Phone number, required if enabled in your merchant dashboard.
        mobile.setBilling(billing);
        return mobile;


    }

}
