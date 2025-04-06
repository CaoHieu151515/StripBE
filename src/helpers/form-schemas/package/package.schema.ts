import * as yup from 'yup';
import { allFormSchema } from '../AllFormSchema';

export const createPackageSchema = yup.object().shape({
  packageName: allFormSchema.packageName,
  packageTime: allFormSchema.packageTime,
  packageBonusTime: allFormSchema.packageBonusTime,
  packagePrice: allFormSchema.packagePrice,
});
