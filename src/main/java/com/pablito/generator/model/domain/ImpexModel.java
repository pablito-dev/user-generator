package com.pablito.generator.model.domain;

/**
 * Created by Paweł Nowak on 15/10/2017.
 */
public class ImpexModel {
    private String userImpex;
    private String addressImpex;

    public ImpexModel(final String userImpex, final String addressImpex) {
        this.userImpex = userImpex;
        this.addressImpex = addressImpex;
    }

    public String getUserImpex() {
        return userImpex;
    }

    public void setUserImpex(final String userImpex) {
        this.userImpex = userImpex;
    }

    public String getAddressImpex() {
        return addressImpex;
    }

    public void setAddressImpex(final String addressImpex) {
        this.addressImpex = addressImpex;
    }
}
