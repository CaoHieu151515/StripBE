import dayjs from 'dayjs';
import { ITrip } from 'app/shared/model/trip.model';
import { IDriver } from 'app/shared/model/driver.model';
import { IUser } from 'app/shared/model/user.model';
import { RatingType } from 'app/shared/model/enumerations/rating-type.model';

export interface IRating {
  id?: number;
  ratingID?: string | null;
  ratingTime?: dayjs.Dayjs | null;
  ratingDriver?: number | null;
  ratingType?: keyof typeof RatingType | null;
  trip?: ITrip | null;
  driver?: IDriver | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IRating> = {};
