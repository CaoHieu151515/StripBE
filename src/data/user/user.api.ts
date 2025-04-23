import { TAG_TYPES } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { AuthResetPasswordREQ } from '../auth/request/auth-reset-password.request';
import { CRMApi } from '../CRMApi.api';
import { UserInfoDTO } from './dto/user-info.dto';
import { UpdateUserInfoREQ } from './request/user-info.request';
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

    putUpdateUserInfo: build.mutation({
      query: (body: UpdateUserInfoREQ) => ({
        url: 'account/update/profile',
        body,
        method: HTTP_METHOD.PUT,
      }),
      invalidatesTags: [TAG_TYPES.USER],
    }),

    putChangePassword: build.mutation({
      query: ({ currentPassword, newPassword }: AuthResetPasswordREQ) => ({
        url: 'account/change-password',
        body: { currentPassword, newPassword },
        method: HTTP_METHOD.POST,
      }),
    }),
  }),
});

export const { useGetUserInfoQuery, usePutChangePasswordMutation, usePutUpdateUserInfoMutation } = userApi;
