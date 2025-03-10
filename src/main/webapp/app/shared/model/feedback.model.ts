import { ITrip } from 'app/shared/model/trip.model';
import { IDriver } from 'app/shared/model/driver.model';
import { IUser } from 'app/shared/model/user.model';
import { FeedbackType } from 'app/shared/model/enumerations/feedback-type.model';
import { FeedbackStatus } from 'app/shared/model/enumerations/feedback-status.model';

export interface IFeedback {
  id?: number;
  feedbackID?: string | null;
  feedbackType?: keyof typeof FeedbackType | null;
  feedbackDescription?: string | null;
  feedbackRating?: number | null;
  feedbackStatus?: keyof typeof FeedbackStatus | null;
  trip?: ITrip | null;
  driver?: IDriver | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IFeedback> = {};
