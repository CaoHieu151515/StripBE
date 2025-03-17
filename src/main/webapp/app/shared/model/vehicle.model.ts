import { IDriver } from 'app/shared/model/driver.model';
import { VehicleType } from 'app/shared/model/enumerations/vehicle-type.model';
import { VehicleStatus } from 'app/shared/model/enumerations/vehicle-status.model';

export interface IVehicle {
  id?: number;
  vehicleID?: string | null;
  vehicleType?: keyof typeof VehicleType | null;
  vehicleImageContentType?: string | null;
  vehicleImage?: string | null;
  carregistrationContentType?: string | null;
  carregistration?: string | null;
  vehicleInspectionCertificateContentType?: string | null;
  vehicleInspectionCertificate?: string | null;
  carInsuranceContentType?: string | null;
  carInsurance?: string | null;
  vehicleNumber?: string | null;
  numberOfSeats?: number | null;
  vehicleColor?: string | null;
  vehicleBrand?: string | null;
  status?: keyof typeof VehicleStatus | null;
  driver?: IDriver | null;
}

export const defaultValue: Readonly<IVehicle> = {};
