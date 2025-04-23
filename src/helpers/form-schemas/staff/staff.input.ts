import * as yup from 'yup';
import { staffCreateSchema, staffSchema } from './staff.schema';

export type StaffInput = yup.InferType<typeof staffSchema>;

export type StaffCreateInput = yup.InferType<typeof staffCreateSchema>;
