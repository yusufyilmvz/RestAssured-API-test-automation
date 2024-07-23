package testWithToken;

public class NameLastnameResponse {
    private String name;
    private String lastname;
    private String username;

//    public NameLastnameResponse(String name, String lastname, String username) {
//        this.name = name;
//        this.lastname = lastname;
//        this.username = username;
//    }



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
}
