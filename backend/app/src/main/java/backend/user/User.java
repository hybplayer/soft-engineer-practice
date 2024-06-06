//package backend.user;
//
//import java.util.List;
//import java.util.ArrayList;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "users")
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String username;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(nullable = true)
//    private String email = null;
//
//    @Column(nullable = true)
//    private List<String> tripPreferences = new ArrayList<String>();
//
//    // constructor
//    public User() {
//    }
//
//    public User(String username, String password, String email, List<String> tripPrefferences) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.tripPreferences = tripPrefferences;
//    }
//
//    // getter和setter方法
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public List<String> getTripPreferences() {
//        return tripPreferences;
//    }
//
//    public void setTripPreferences(List<String> tripPrefferences) {
//        this.tripPreferences = tripPrefferences;
//    }
//
//    public void addTripPreference(String tripPrefference) {
//        this.tripPreferences.add(tripPrefference);
//    }
//
//    public void removeTripPreference(String tripPrefference) {
//        this.tripPreferences.remove(tripPrefference);
//    }
//}
