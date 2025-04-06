import { SelectOptions } from '@/components/types/Selects.type';
import { TFunction } from 'i18next';
import {
  DASHBOARD_INTERVAL_OPTIONS,
  DASHBOARD_MODE_OPTIONS,
  DASHBOARD_STATUS_OPTIONS,
} from './constants/form.constant';

export const getDashboardModeOptions = (tForm: TFunction<'common', 'common'>) => {
  const res: SelectOptions[] = [];
  DASHBOARD_MODE_OPTIONS.forEach((value) => {
    res.push({ value, label: tForm(`enum.DASHBOARD_MODE.${value}` as any) });
  });
  return res;
};

export const getDashboardStatusOptions = (tForm: TFunction<'common', 'common'>) => {
  const res: SelectOptions[] = [];
  DASHBOARD_STATUS_OPTIONS.forEach((value) => {
    res.push({ value, label: tForm(`enum.DASHBOARD_STATUS.${value}` as any) });
  });
  return res;
};

export const getDashboardIntervalOptions = (tForm: TFunction<'common', 'common'>) => {
  const res: SelectOptions[] = [];
  DASHBOARD_INTERVAL_OPTIONS.forEach((value) => {
    res.push({ value, label: tForm(`enum.DASHBOARD_INTERVAL.${value}` as any) });
  });
  return res;
};
