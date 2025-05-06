package strip.service.dto.dashboard;

import java.io.Serializable;

public class TripCreateStatDTO implements Serializable {

    private String dateTime;
    private long count;

    public TripCreateStatDTO() {
        // Empty constructor
    }

    public TripCreateStatDTO(String dateTime, long count) {
        this.dateTime = dateTime;
        this.count = count;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
