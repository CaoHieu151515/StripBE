import * as yup from 'yup';
import { allFormSchema } from '../AllFormSchema';

export const loginAdminSchema = yup.object().shape({
  username: allFormSchema.username,
  password: allFormSchema.password,
});
