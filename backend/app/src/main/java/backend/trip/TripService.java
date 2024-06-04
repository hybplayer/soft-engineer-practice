package backend.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip createTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Trip getTripByID(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    public Trip getTripByTitle(String title) {
        return tripRepository.findByTitle(title);
    }

    public void deleteTrip(Trip trip) {
        tripRepository.delete(trip);
    }

    public Trip updateTrip(Trip trip) {
        return tripRepository.save(trip);
    }
}
