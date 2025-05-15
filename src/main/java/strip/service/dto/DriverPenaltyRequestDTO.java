package strip.service.dto;

import java.io.Serializable;

public class DriverPenaltyRequestDTO implements Serializable {

    private String reason; // Lý do xử phạt

    public DriverPenaltyRequestDTO() {}

    public DriverPenaltyRequestDTO(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
