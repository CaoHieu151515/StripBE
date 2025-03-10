import dayjs from 'dayjs';
import { ITrip } from 'app/shared/model/trip.model';
import { IUser } from 'app/shared/model/user.model';
import { PassengerType } from 'app/shared/model/enumerations/passenger-type.model';
import { PassengerStatus } from 'app/shared/model/enumerations/passenger-status.model';

export interface IPassenger {
  id?: number;
  passengerID?: string | null;
  startLoca?: string | null;
  endLoca?: string | null;
  amountApproveFee?: number | null;
  luggageImgContentType?: string | null;
  luggageImg?: string | null;
  luggageDescription?: string | null;
  type?: keyof typeof PassengerType | null;
  status?: keyof typeof PassengerStatus | null;
  pickUpTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  checkIn?: boolean | null;
  checkInTime?: dayjs.Dayjs | null;
  checkOut?: boolean | null;
  checkOutTIme?: dayjs.Dayjs | null;
  appliedAt?: dayjs.Dayjs | null;
  trip?: ITrip | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPassenger> = {
  checkIn: false,
  checkOut: false,
};
