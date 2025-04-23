import * as yup from 'yup';
import { allFormSchema, baseSchema } from '../AllFormSchema';

export const updateProfileSchema = yup.object().shape({
  firstName: allFormSchema.firstName,
  lastName: allFormSchema.lastName,
  phone: allFormSchema.phoneNumber,
  email: allFormSchema.email,
  address: baseSchema.yupString,
  dob: allFormSchema.dob,
  gender: allFormSchema.gender,
});
