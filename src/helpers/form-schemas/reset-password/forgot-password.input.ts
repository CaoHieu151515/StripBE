import * as yup from 'yup';
import { forgotPasswordSchema } from './forgot-password.schema';

export type ForgotPasswordInput = yup.InferType<typeof forgotPasswordSchema>;
