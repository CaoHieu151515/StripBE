import { TRIP_STATUS } from '@/utils/enum/trip/trip-status.enum';

export type GetTripRESP = {
  index: number;
  stripID: string;
  startDay: string;
  endDay: string;
  startLocation: string;
  endlocation: string;
  price: number;
  status: TRIP_STATUS;
  totalTime: number;
  tripHandleId: string;
};

export type GetTripDetailRESP = {
  tripID: string;
  startLocation: string;
  endLocation: string;
  description: string;
  condition: string;
  startDate: string;
  endDate: string;
  pricePerSeat: number;
  maxSeat: number;
  currentSeat: number;
  tripStatus: TRIP_STATUS;
  cancelReason: string;
  totalTime: number;
  totalDistance: number;
  tripImgUrl: string;
  driver: DriverInfo;
  vehicle: VehicleInfo;
  stoplocation: StopLocation[];
  tripHandleId: string;
};

export type TripDetailDTO = GetTripDetailRESP;

export type StopLocation = {
  stopLocaID: string;
  stopLoca: string;
  tripPositon: number;
  stopLocaTime: string;
  estimatedTime: number;
  estimatedKM: number;
  stopLocaStatus: string;
};

export type DriverInfo = {
  userId?: string;
  driverId: string;
  firstName: string;
  lastName: string;
  phone: string;
  address: string;
  gender: string;
  dob: string;
  email: string;
  driverLicenseUrl: string;
  identityCardFaceUpUrl: string;
  identityCardFaceDownUrl: string;
  avatarUrl: string;
  rating: number;
};

export type VehicleInfo = {
  vehicleID: string;
  vehicleType: string;
  vehicleImageUrl: string;
  carregistrationUrl: string;
  vehicleInspectionCertificateUrl: string;
  carInsuranceUrl: string;
  vehicleNumber: string;
  numberOfSeats: number;
  vehicleColor: string;
  vehicleBrand: string;
  status: string;
};
