import dayjs from 'dayjs';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { IDriver } from 'app/shared/model/driver.model';
import { TripStatus } from 'app/shared/model/enumerations/trip-status.model';

export interface ITrip {
  id?: number;
  tripID?: string | null;
  tripImgContentType?: string | null;
  tripImg?: string | null;
  pricePerSeat?: number | null;
  maxSeat?: number | null;
  totalTime?: number | null;
  totalDistance?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  currentSeat?: number | null;
  startLocation?: string | null;
  endLocation?: string | null;
  description?: string | null;
  condition?: string | null;
  cancelReason?: string | null;
  tripStatus?: keyof typeof TripStatus | null;
  vehicle?: IVehicle | null;
  driver?: IDriver | null;
}

export const defaultValue: Readonly<ITrip> = {};
