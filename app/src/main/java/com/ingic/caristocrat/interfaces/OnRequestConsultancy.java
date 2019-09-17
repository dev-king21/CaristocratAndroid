package com.ingic.caristocrat.interfaces;

public interface OnRequestConsultancy {
    void onRequested(String email, String name, String countryCode, String phone, int type, String message);
}
