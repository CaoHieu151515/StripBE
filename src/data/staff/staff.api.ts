import { TAG_TYPES } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { PaginationRESP } from '@/utils/types/response.type';
import { CRMApi } from '../CRMApi.api';
import { GetStaffListREQ, StaffCreateREQ, StaffUpdateREQ } from './request/staff.request';
import { GetStaffRESP } from './response/staff.response';

export const staffApi = CRMApi.enhanceEndpoints({ addTagTypes: [TAG_TYPES.STAFF] }).injectEndpoints({
  endpoints: (build) => ({
    getStaffList: build.query<PaginationRESP<GetStaffRESP>, GetStaffListREQ>({
      query: (filter: GetStaffListREQ) => ({
        url: 'admin/staff',
        method: HTTP_METHOD.GET,
        params: { ...filter },
      }),
      providesTags: [{ type: TAG_TYPES.STAFF }],
    }),

    postCreateStaff: build.mutation<void, StaffCreateREQ>({
      query: (data: StaffCreateREQ) => ({
        url: 'admin/staff',
        method: HTTP_METHOD.POST,
        body: data,
      }),
      invalidatesTags: [{ type: TAG_TYPES.STAFF }],
    }),

    putUpdateStaff: build.mutation<void, StaffUpdateREQ>({
      query: (data: StaffUpdateREQ) => ({
        url: 'admin/staff/update-by-detail',
        method: HTTP_METHOD.PUT,
        body: data,
      }),
      invalidatesTags: [{ type: TAG_TYPES.STAFF }],
    }),

    putToggleStaffStatus: build.mutation<void, string>({
      query: (staffId: string) => ({
        url: `admin/staff/toggle-activation/${staffId}`,
        method: HTTP_METHOD.PUT,
      }),
      invalidatesTags: [{ type: TAG_TYPES.STAFF }],
    }),
  }),
});

export const {
  useGetStaffListQuery,
  usePutUpdateStaffMutation,
  usePostCreateStaffMutation,
  usePutToggleStaffStatusMutation,
} = staffApi;
