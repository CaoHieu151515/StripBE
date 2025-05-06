package strip.service.dto.dashboard;

public class SimpleStatDTO {

    private String dateTime;
    private Long value;

    public SimpleStatDTO(String dateTime, Long value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
