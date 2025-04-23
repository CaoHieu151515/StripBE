import * as yup from 'yup';
import { updateProfileSchema } from './user.schema';

export type UpdateProfileInput = yup.InferType<typeof updateProfileSchema>;
