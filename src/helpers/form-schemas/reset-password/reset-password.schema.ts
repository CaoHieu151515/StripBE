import * as yup from 'yup';
import { allFormSchema } from '../AllFormSchema';

export const resetPasswordSchema = yup.object().shape({
  password: allFormSchema.password,
  confirmPassword: allFormSchema.confirmPassword,
});
