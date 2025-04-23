import * as yup from 'yup';
import { allFormSchema, baseSchema } from '../AllFormSchema';

export const staffSchema = yup.object().shape({
  firstName: allFormSchema.firstName,
  lastName: allFormSchema.lastName,
  email: allFormSchema.email,
  phone: allFormSchema.phoneNumber,
  address: baseSchema.yupString,
  dob: allFormSchema.dob,
  gender: allFormSchema.gender,
});

export const staffCreateSchema = yup.object().shape({
  firstName: allFormSchema.firstName,
  lastName: allFormSchema.lastName,
  email: allFormSchema.email,
  phone: allFormSchema.phoneNumber,
  address: baseSchema.yupString,
  dob: allFormSchema.dob,
  username: allFormSchema.username,
  newPassword: allFormSchema.newPassword,
  confirmPassword: allFormSchema.confirmPassword,
  gender: allFormSchema.gender,
});
