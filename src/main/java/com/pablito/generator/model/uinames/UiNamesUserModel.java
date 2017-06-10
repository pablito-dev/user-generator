package com.pablito.generator.model.uinames;

/**
 * Created by pavel_000 on 31/05/2017.
 */
public class UiNamesUserModel {
    private String name;
    private String surname;
    private String gender;
    private String region;
    private String phone;
    private String title;
    private UiNamesUserBirthdayModel birthday;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public UiNamesUserBirthdayModel getBirthday() {
        return birthday;
    }

    public void setBirthday(final UiNamesUserBirthdayModel birthday) {
        this.birthday = birthday;
    }
}
