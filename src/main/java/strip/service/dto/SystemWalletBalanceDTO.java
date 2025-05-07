package strip.service.dto;

public class SystemWalletBalanceDTO {

    private double totalSystemBalance;

    public SystemWalletBalanceDTO(double totalSystemBalance) {
        this.totalSystemBalance = totalSystemBalance;
    }

    public double getTotalSystemBalance() {
        return totalSystemBalance;
    }

    public void setTotalSystemBalance(double totalSystemBalance) {
        this.totalSystemBalance = totalSystemBalance;
    }
}
