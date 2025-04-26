import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { initialPagingState } from '@/utils/types/paging.type';
import { ParsedQuery } from 'query-string';
import { GetTripListREQ } from './request/trip.request';

export const tripListRequestParamsToFilter = (searchParams: ParsedQuery<string>): GetTripListREQ => {
  const page = searchParams[PARAM_FIELD.PAGE] || initialPagingState.page;
  const size = searchParams[PARAM_FIELD.SIZE] || initialPagingState.size;
  const startLocation = searchParams[PARAM_FIELD.START_LOCATION] as string;
  const endLocation = searchParams[PARAM_FIELD.END_LOCATION] as string;
  const status = searchParams[PARAM_FIELD.STATUS] as string;
  const sort = searchParams[PARAM_FIELD.SORT] as string;
  const driverId = searchParams[PARAM_FIELD.DRIVER_ID] as string;
  const tripHandleId = searchParams[PARAM_FIELD.TRIP_HANDLE_ID] as string;

  return {
    ...(!!startLocation && { startLocation }),
    ...(!!endLocation && { endLocation }),
    ...(!!status && { status }),
    ...(!!sort && { sort: sort.split(',') }),
    ...(!!driverId && { driverId }),
    ...(!!tripHandleId && { tripHandleId }),
    page: page ? Number(page) - 1 : initialPagingState.page,
    size: Number(size),
  };
};
