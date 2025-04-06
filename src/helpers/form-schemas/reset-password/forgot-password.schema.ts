import * as yup from 'yup';
import { allFormSchema } from '../AllFormSchema';

export const forgotPasswordSchema = yup.object().shape({
  email: allFormSchema.email,
});
