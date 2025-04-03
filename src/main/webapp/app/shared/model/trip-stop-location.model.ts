import dayjs from 'dayjs';
import { ITrip } from 'app/shared/model/trip.model';

export interface ITripStopLocation {
  id?: number;
  stopLocaID?: string | null;
  stopLoca?: string | null;
  stoplocaPosition?: number | null;
  estimatedTime?: number | null;
  estimatedKM?: number | null;
  stopLocaTime?: dayjs.Dayjs | null;
  stopLocaStatus?: string | null;
  trip?: ITrip | null;
}

export const defaultValue: Readonly<ITripStopLocation> = {};
