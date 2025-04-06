import * as yup from 'yup';
import { loginAdminSchema } from './login.schema';

export type LoginAdminInput = yup.InferType<typeof loginAdminSchema>;
