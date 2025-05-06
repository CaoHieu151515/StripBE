package strip.service.dto.dashboard;

public class RegistrationStatDTO {

    private String dateTime;
    private long value;

    public RegistrationStatDTO(String dateTime, long value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
