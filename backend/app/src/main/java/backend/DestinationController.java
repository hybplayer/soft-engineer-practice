package backend;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class DestinationController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DestinationController.class);
    @Autowired
    private DestinationRepository destinationRepository;

    @GetMapping("/{username}/destinations")
    public List<Destination> getUserDestinations(@PathVariable String username) {
        return destinationRepository.findByUsername(username);
    }
    @PostMapping("/destinations")
    public ResponseEntity<Destination> addDestination(@RequestBody Destination destination) {
        logger.info("Adding destination: " + destination);
        Destination savedDestination = destinationRepository.save(destination);
        return ResponseEntity.ok(savedDestination);
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<Destination>> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/destinations/{username}")
    public ResponseEntity<List<Destination>> getDestinationsByUsername(@PathVariable String username) {
        List<Destination> destinations = destinationRepository.findByUsername(username);
        return ResponseEntity.ok(destinations);
    }

    @PutMapping("/destinations/{id}")
    public ResponseEntity<Destination> updateDestination(@PathVariable Long id, @RequestBody Destination destinationDetails) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id " + id));

        destination.setUsername(destinationDetails.getUsername());
        destination.setDeparture(destinationDetails.getDeparture());
        destination.setDestination(destinationDetails.getDestination());
        destination.setDepartureDate(destinationDetails.getDepartureDate());
        destination.setCheckboxValues(destinationDetails.getCheckboxValues());
        destination.setPriceRange(destinationDetails.getPriceRange());
        destination.setCompanionRequirements(destinationDetails.getCompanionRequirements());
        destination.setRemark(destinationDetails.getRemark());

        Destination updatedDestination = destinationRepository.save(destination);
        return ResponseEntity.ok(updatedDestination);
    }

    @DeleteMapping("/destinations/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id " + id));

        destinationRepository.delete(destination);
        return ResponseEntity.noContent().build();
    }
}
