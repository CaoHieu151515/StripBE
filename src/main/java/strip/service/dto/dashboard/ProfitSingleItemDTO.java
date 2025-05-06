package strip.service.dto.dashboard;

public class ProfitSingleItemDTO {

    private String dateTime;
    private double value;

    public ProfitSingleItemDTO() {
        // Empty constructor for serialization/deserialization
    }

    public ProfitSingleItemDTO(String dateTime, double value) {
        this.dateTime = dateTime;
        this.value = value;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
