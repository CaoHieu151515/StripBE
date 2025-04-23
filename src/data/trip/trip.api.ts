import { TAG_TYPES } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { PaginationRESP } from '@/utils/types/response.type';
import { CRMApi } from '../CRMApi.api';
import { GetTripListREQ, PutRejectTripREQ } from './request/trip.request';
import { GetTripDetailRESP, GetTripRESP } from './response/trip.response';

export const tripApi = CRMApi.enhanceEndpoints({ addTagTypes: [TAG_TYPES.TRIP] }).injectEndpoints({
  endpoints: (build) => ({
    getTripList: build.query<PaginationRESP<GetTripRESP>, GetTripListREQ>({
      query: (filter: GetTripListREQ) => ({
        url: 'manager/trips/getall',
        method: HTTP_METHOD.GET,
        params: { ...filter },
      }),
      providesTags: [{ type: TAG_TYPES.TRIP }],
    }),

    getTripDetail: build.query<GetTripDetailRESP, string>({
      query: (tripID: string) => ({
        url: `manager/trips/${tripID}`,
        method: HTTP_METHOD.GET,
      }),
      providesTags: [{ type: TAG_TYPES.TRIP_DETAIL }],
    }),

    putAcceptTrip: build.mutation<void, string>({
      query: (tripID: string) => ({
        url: `manager/trips/${tripID}/approve`,
        method: HTTP_METHOD.PUT,
      }),
      invalidatesTags: [{ type: TAG_TYPES.TRIP_DETAIL }],
    }),

    putRejectTrip: build.mutation<void, PutRejectTripREQ>({
      query: (data: PutRejectTripREQ) => ({
        url: `manager/trips/${data.tripId}/reject`,
        method: HTTP_METHOD.PUT,
        body: { reason: data.reason },
      }),
      invalidatesTags: [{ type: TAG_TYPES.TRIP_DETAIL }],
    }),
  }),
});

export const { useGetTripListQuery, useGetTripDetailQuery, usePutAcceptTripMutation, usePutRejectTripMutation } =
  tripApi;
