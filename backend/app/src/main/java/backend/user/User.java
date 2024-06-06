package backend.user;

import java.util.List;

import backend.trip.Trip;

import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String tripPreference;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Trip> trips = new ArrayList<Trip>();

    // @ManyToMany(mappedBy = "participants")
    // private List<Trip> participatedTrips = new ArrayList<Trip>();

    // constructor 方法由 lombok 自动生成

    // getter和setter方法由 lombok 自动生成

    // public void addTripPreference(String tripPrefference) {
    //     this.tripPreferences.add(tripPrefference);
    // }

    // public void removeTripPreference(String tripPrefference) {
    //     this.tripPreferences.remove(tripPrefference);
    // }
}
