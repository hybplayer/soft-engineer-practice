package backend.trip;

import java.util.List;

import backend.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column(nullable = false)
    // private String title;

    @ManyToOne
    @JoinColumn(name = "organizer", nullable = false)
    private User organizer;

    @Column(nullable = false)
    private String destinations;

    @Column(nullable = false)
    private String departurePlace;

    @Column(nullable = false)
    private LocalDate startDate;

    // @Column(nullable = false)
    // private LocalDate endDate;

    @ElementCollection
    List<TripOption> options;

    @Column(nullable = false)
    private int price; // this is an option.

    @Column(nullable = false)
    private String descriptionParticipants;

    @Column(nullable = false)
    private String remark;

    // @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    // @JoinTable(name = "trip_participants", joinColumns = @JoinColumn(name = "trip_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    // private List<User> participants;

    // constructor 方法由 lombok 自动生成

    // getter和setter方法由 lombok 自动生成

    // public void addParticipant(User user) {
    //     this.participants.add(user);
    // }

    // public void removeParticipant(User user) {
    //     this.participants.remove(user);
    // }

    // public boolean isParticipant(User user) {
    //     return this.participants.contains(user);
    // }

}
