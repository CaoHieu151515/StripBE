import { TFunction } from 'i18next';
import { FREQUENCY } from './enum/common/frequency.enum';

export const getFrequencyOptions = (tCommon: TFunction<'common', undefined>) => {
  return [
    { value: FREQUENCY.TOTAL, label: tCommon('enum.total') },
    { value: FREQUENCY.DAILY, label: tCommon('enum.frequency.daily') },
    { value: FREQUENCY.WEEKLY, label: tCommon('enum.frequency.weekly') },
    { value: FREQUENCY.MONTHLY, label: tCommon('enum.frequency.monthly') },
  ];
};
