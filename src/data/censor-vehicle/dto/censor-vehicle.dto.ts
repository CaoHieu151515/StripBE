import { CENSOR_DRIVER_STATUS } from '@/utils/enum/censor-driver/censor-driver.enum';

export interface CensorVehicleDTO {
  id: number;
  vehicleName: string;
  vehicleType: string;
  brand: string;
  numberInsead: string;
  status: CENSOR_DRIVER_STATUS;
}
