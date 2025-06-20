
package ladybugdevs.mztddp;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class EventClass {
    
    private String eventName;
    private LocalDate eventDate;
    private String eventCategory;
    private String eventLocation;

    // Constructor
    public EventClass(String eventName, LocalDate eventDate, String eventCategory,String eventLocation) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventCategory = eventCategory;
        this.eventLocation = eventLocation;
    }

    // Getters and Setters
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    // Helper method to format the event date
    public String getFormattedEventDate() {
    return eventDate.format(DateTimeFormatter.ISO_DATE);
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
    
    @Override
    public String toString() {
        return eventName + " | " + getFormattedEventDate() + " | " + eventCategory + " | " + eventLocation;
    }
}
