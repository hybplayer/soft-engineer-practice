package backend;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String departure;
    private String destination;
    private String departureDate;
    @ElementCollection
    private List<String> checkboxValues;
    private String priceRange;
    private String companionRequirements;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public List<String> getCheckboxValues() {
        return checkboxValues;
    }

    public void setCheckboxValues(List<String> checkboxValues) {
        this.checkboxValues = checkboxValues;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getCompanionRequirements() {
        return companionRequirements;
    }

    public void setCompanionRequirements(String companionRequirements) {
        this.companionRequirements = companionRequirements;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // getters and setters
}
