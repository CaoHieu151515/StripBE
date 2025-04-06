import * as yup from 'yup';
import { createPackageSchema } from './package.schema';

export type CreatePackageInput = yup.InferType<typeof createPackageSchema>;
