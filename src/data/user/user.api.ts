import { TAG_TYPES } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { CRMApi } from '../CRMApi.api';
import { UserInfoDTO } from './dto/user-info.dto';
import { UserInfoRESP } from './response/user-info.response';

const userApi = CRMApi.enhanceEndpoints({ addTagTypes: [TAG_TYPES.USER] }).injectEndpoints({
  endpoints: (build) => ({
    getUserInfo: build.query<UserInfoDTO, void>({
      query: () => ({
        url: 'getme',
        method: HTTP_METHOD.GET,
      }),
      transformResponse: (data: UserInfoRESP) => data,
      providesTags: [TAG_TYPES.USER],
    }),
  }),
});

export const { useGetUserInfoQuery } = userApi;
