package org.unibl.eurobasket.dto;

import org.unibl.eurobasket.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ValidPassword
public class MatchingPasswordDTO {

    @NotNull
    @Size(min = 3, max = 40)
    private String password;

    @NotNull
    private String matchingPassword;

    public String getPassword() {
        return password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}