package strip.service.dto.dashboard;

import java.util.List;

public class RegistrationStatResponseDTO {

    private List<RegistrationStatDTO> dataUser;
    private List<RegistrationStatDTO> dataDriver;

    public RegistrationStatResponseDTO(List<RegistrationStatDTO> dataUser, List<RegistrationStatDTO> dataDriver) {
        this.dataUser = dataUser;
        this.dataDriver = dataDriver;
    }

    public List<RegistrationStatDTO> getDataUser() {
        return dataUser;
    }

    public void setDataUser(List<RegistrationStatDTO> dataUser) {
        this.dataUser = dataUser;
    }

    public List<RegistrationStatDTO> getDataDriver() {
        return dataDriver;
    }

    public void setDataDriver(List<RegistrationStatDTO> dataDriver) {
        this.dataDriver = dataDriver;
    }
}
