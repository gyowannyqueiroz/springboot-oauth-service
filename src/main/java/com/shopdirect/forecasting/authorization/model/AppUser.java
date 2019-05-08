package com.shopdirect.forecasting.authorization.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column
    private String userEmail;

    @Column
    private String userPass;

    @Column
    private String userRole;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(userEmail, appUser.userEmail) &&
                Objects.equals(userPass, appUser.userPass) &&
                Objects.equals(userRole, appUser.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, userPass, userRole);
    }
}
