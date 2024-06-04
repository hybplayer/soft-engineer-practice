package backend.trip.requets;

import java.util.List;

import backend.user.User;
import lombok.Data;

@Data
public class TripCreateRequest {
    private String title;
    private User organizer;
    private List<String> destinations;
    private String startDate;
    private String endDate;
    private int price;
    private String description;
}
