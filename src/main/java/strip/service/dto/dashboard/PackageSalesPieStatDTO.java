package strip.service.dto.dashboard;

public class PackageSalesPieStatDTO {

    private String packageName;
    private long totalSold;

    public PackageSalesPieStatDTO(String packageName, long totalSold) {
        this.packageName = packageName;
        this.totalSold = totalSold;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(long totalSold) {
        this.totalSold = totalSold;
    }
    // Getters & Setters

}
