package testWithToken.model;

import java.util.Set;

public class CreateUserRequest {
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String gender;
    private Set<String> authorities;

    public CreateUserRequest(String name, String lastname, String username, String password, String gender, Set<String> authorities) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.authorities = authorities;
    }

    public CreateUserRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
