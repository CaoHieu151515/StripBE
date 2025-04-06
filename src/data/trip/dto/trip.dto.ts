import { CensorDriverDTO } from '@/data/censor-driver/dto/censor-driver.dto';
import { CensorVehicleDTO } from '@/data/censor-vehicle/dto/censor-vehicle.dto';
import { TRIP_STATUS } from '@/utils/enum/trip/trip-status.enum';

export interface TripDTO {
  id: number;
  driver: CensorDriverDTO;
  vehicle: CensorVehicleDTO;
  departureDate: string;
  departureTime: string;
  departureLocation: string;
  arrivalDate: string;
  arrivalTime: string;
  arrivalLocation: string;
  price: number;
  description: string;
  status: TRIP_STATUS;
}
