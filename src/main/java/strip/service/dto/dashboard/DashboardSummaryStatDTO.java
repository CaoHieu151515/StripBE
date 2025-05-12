package strip.service.dto.dashboard;

public class DashboardSummaryStatDTO {

    private long totalUsers;
    private double totalRevenue;
    private long totalPackagesSold;

    public DashboardSummaryStatDTO(long totalUsers, double totalRevenue, long totalPackagesSold) {
        this.totalUsers = totalUsers;
        this.totalRevenue = totalRevenue;
        this.totalPackagesSold = totalPackagesSold;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public long getTotalPackagesSold() {
        return totalPackagesSold;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setTotalPackagesSold(long totalPackagesSold) {
        this.totalPackagesSold = totalPackagesSold;
    }
}
