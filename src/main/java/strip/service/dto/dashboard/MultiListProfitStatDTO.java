package strip.service.dto.dashboard;

import java.io.Serializable;
import java.util.List;

public class MultiListProfitStatDTO implements Serializable {

    private List<ProfitSingleItemDTO> fromtrip;
    private List<ProfitSingleItemDTO> frompackage;

    public MultiListProfitStatDTO() {
        // Empty constructor
    }

    public MultiListProfitStatDTO(List<ProfitSingleItemDTO> fromtrip, List<ProfitSingleItemDTO> frompackage) {
        this.fromtrip = fromtrip;
        this.frompackage = frompackage;
    }

    public List<ProfitSingleItemDTO> getFromtrip() {
        return fromtrip;
    }

    public void setFromtrip(List<ProfitSingleItemDTO> fromtrip) {
        this.fromtrip = fromtrip;
    }

    public List<ProfitSingleItemDTO> getFrompackage() {
        return frompackage;
    }

    public void setFrompackage(List<ProfitSingleItemDTO> frompackage) {
        this.frompackage = frompackage;
    }
}
