export interface CensorVehicleDTO {
  index?: number;
  userId?: string;
  driverId?: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  email?: string;
  identityCardFaceUpUrl?: string;
  identityCardFaceDownUrl?: string;
  driverLicenseUrl?: string;
  vehicle?: Vehicle;
}

export interface Vehicle {
  id?: number;
  vehicleID?: string;
  vehicleType?: string;
  vehicleImageUrl?: string;
  carregistrationUrl?: string;
  vehicleInspectionCertificateUrl?: string;
  carInsuranceUrl?: string;
  vehicleNumber?: string;
  numberOfSeats?: number;
  vehicleColor?: string;
  vehicleBrand?: string;
  status?: string;
}
