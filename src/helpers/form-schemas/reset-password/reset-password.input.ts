import * as yup from 'yup';
import { resetPasswordSchema } from './reset-password.schema';

export type ResetPasswordInput = yup.InferType<typeof resetPasswordSchema>;
