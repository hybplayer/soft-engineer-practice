package backend.trip.requets;

import java.util.List;

import backend.user.User;
import lombok.Data;

@Data
public class TripCreateRequest {
    // private String title;
    private User organizer;
    private String destinations;
    private String departurePlace;
    private String startDate;
    // private String endDate;
    List<TripOption> options;
    private int price;
    private String descriptionParticipants;
    private String remark;
}
