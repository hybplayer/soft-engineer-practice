package backend.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.trip.requets.*;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/create")
    public ResponseEntity<Trip> createTrip(@RequestBody TripCreateRequest request) {
        Trip newTrip = new Trip();
        newTrip.setTitle(request.getTitle());
        newTrip.setOrganizer(request.getOrganizer());
        newTrip.setDestinations(request.getDestinations());
        newTrip.setStartDate(request.getStartDate());
        newTrip.setEndDate(request.getEndDate());
        newTrip.setPrice(request.getPrice());
        newTrip.setDescription(request.getDescription());
        return ResponseEntity.ok(tripService.createTrip(newTrip));
    }

    @GetMapping("/{tripID}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long tripID) {
        Trip trip = tripService.getTripByID(tripID);
        if (trip == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(trip);
        }
    }

    @DeleteMapping("/{tripID}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripID) {
        Trip trip = tripService.getTripByID(tripID);
        if (trip == null) {
            return ResponseEntity.notFound().build();
        }
        tripService.deleteTrip(trip);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tripID}")
    public ResponseEntity<Trip> updateTrip(@PathVariable Long tripID, @RequestBody Trip trip) {
        Trip existingTripByID = tripService.getTripByID(tripID);
        Trip existingTripByTitle = tripService.getTripByTitle(trip.getTitle());

        if (existingTripByID == null) {
            return ResponseEntity.notFound().build();
        }

        if (existingTripByTitle != null && existingTripByTitle.getId().equals(tripID)) {
            trip.setId(tripID);
            Trip updatedTrip = tripService.updateTrip(trip);
            return ResponseEntity.ok(updatedTrip);
        }

        return ResponseEntity.badRequest().build();
    }

    // TODO: 自动组队, 推荐队友, 路线规划 (这个可以部署大模型完成)
}
