package lms.mmm.mybillboard.Model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Billboard {
    String address;
    List<String> images ;
    String code ;
    String latitude;
    String longitude;
    String prix ;
    String taille ;
    String type ;
    Boolean disponible ;
    List<Boolean> moisDisponible ;
    int vues ;

    public Billboard() {
    }

    public Billboard(String address, List<String> images, String code, String latitude, String longitude, String prix, String taille, String type, Boolean disponible, List<Boolean> moisDisponible, int vues ) {
        this.address = address;
        this.images = images;
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude ;
        this.prix = prix;
        this.taille = taille;
        this.type = type;
        this.disponible = disponible;
        this.moisDisponible = moisDisponible;
        this.vues = vues;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("address", address);
        result.put("images", images);
        result.put("code", code);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("prix", prix);
        result.put("taille", taille);
        result.put("type", type);
        result.put("disponible", disponible);
        result.put("moisDisponible", moisDisponible);
        result.put("vues", vues);
        return result;
    }


    public int getVues() {
        return vues;
    }

    public void setVues(int vues) {
        this.vues = vues;
    }

    public List<Boolean> getMoisDisponible() {
        return moisDisponible;
    }

    public void setMoisDisponible(List<Boolean> moisDisponible) {
        this.moisDisponible = moisDisponible;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
