package strip.service.dto.dashboard;

public class PackageSalesSimpleStatDTO {

    private String dateTime;
    private String packageName;
    private long value;

    public PackageSalesSimpleStatDTO(String dateTime, String packageName, long value) {
        this.dateTime = dateTime;
        this.packageName = packageName;
        this.value = value;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
    // Getter/Setter

}
