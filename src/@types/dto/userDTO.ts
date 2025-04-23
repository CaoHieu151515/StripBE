export interface UserGetAllDTO {
  userId: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  gender: string;
  phoneNumber: string;
  active: boolean;
  roles: string[];
}
export interface UserDetailDTO {
  userId?: string;
  driverId?: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  address?: string;
  gender?: string;
  dob?: Date;
  email?: string;
  driverLicenseUrl?: string;
  identityCardFaceUpUrl?: string;
  identityCardFaceDownUrl?: string;
  vehicles?: Vehicle[];
}

export interface Vehicle {
  vehicleId?: string;
  vehicleType?: string;
  vehicleImageUrl?: string;
  carRegistrationUrl?: string;
  vehicleInspectionCertificateUrl?: string;
  carInsuranceUrl?: string;
  vehicleNumber?: string;
  numberOfSeats?: number;
  vehicleColor?: string;
  vehicleBrand?: string;
  status?: string;
}

export interface UserGetMeDTO {
  login?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  gender?: string;
  address?: string;
  dob?: Date;
  userImageUrl?: string;
  roles?: string[];
}
