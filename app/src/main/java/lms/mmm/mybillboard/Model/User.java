package lms.mmm.mybillboard.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    String name;
    String firstName;
    String email;
    String adresse;
    String telephone;

    public User(String name, String firstName, String email, String adresse, String telephone) {
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public User()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("firstName", firstName);
        result.put("email", email);
        result.put("adresse", adresse);
        result.put("telephone", telephone);

        return result;
    }
}
