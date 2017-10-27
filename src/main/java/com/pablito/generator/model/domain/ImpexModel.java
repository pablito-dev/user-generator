package com.pablito.generator.model.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paweł Nowak on 15/10/2017.
 */
public class ImpexModel {
    private List<String> userImpex;
    private List<String> addressImpex;

    public ImpexModel() {
        this.userImpex = new ArrayList<>();
        this.addressImpex = new ArrayList<>();
    }

    public ImpexModel(final String userImpex, final String addressImpex) {
        this.userImpex = new ArrayList<>();
        this.addressImpex = new ArrayList<>();

        this.userImpex.add(userImpex);
        this.addressImpex.add(addressImpex);
    }

    public String getUserImpex() {
        return userImpex
                .stream()
                .reduce("", (i, j) -> i + j);
    }

    public String getAddressImpex() {
        return addressImpex
                .stream()
                .reduce("", (i, j) -> i + j);
    }

    public void addAddressImpexLine(final String impexLine) {
        addressImpex.add(impexLine);
    }

    public void addUserImpexLine(final String impexLine) {
        userImpex.add(impexLine);
    }
}
