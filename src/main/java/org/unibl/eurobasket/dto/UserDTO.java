package org.unibl.eurobasket.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {

    @NotNull
    @Size(min = 3, max = 40)
    private String username;

    @NotNull
    @Size(min = 3, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
