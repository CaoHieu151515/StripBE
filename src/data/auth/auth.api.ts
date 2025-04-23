import { apiBaseUrl, TAG_TYPES } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { CRMApi } from '../CRMApi.api';
import { AuthLoginDTO } from './dto/auth-login.dto';
import { AuthDoubleCheckREQ } from './request/auth-double-check.request';
import { AuthLoginREQ } from './request/auth-login.request';
import { AuthRegisterREQ } from './request/auth-register.request';
import { AuthResetPasswordREQ } from './request/auth-reset-password.request';
import { AuthSendCodeREQ } from './request/auth-send-code.request';
import { AuthLoginRESP } from './response/auth-login.response';

export const authApi = createApi({
  reducerPath: 'authApi',
  tagTypes: [TAG_TYPES.USER],
  baseQuery: fetchBaseQuery({ baseUrl: `${apiBaseUrl}/` }),
  endpoints: (build) => ({
    login: build.mutation<AuthLoginDTO, AuthLoginREQ>({
      query: (body) => ({
        url: 'authenticate',
        method: HTTP_METHOD.POST,
        body,
      }),
      transformResponse: ({ id_token }: AuthLoginRESP) => {
        return {
          accessToken: id_token,
        } as AuthLoginDTO;
      },
      async onQueryStarted(_, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        dispatch(CRMApi.util.invalidateTags([TAG_TYPES.USER]));
      },
      invalidatesTags: [TAG_TYPES.USER],
    }),

    register: build.mutation<void, AuthRegisterREQ>({
      query: (body) => ({
        url: 'admin/public/register',
        method: HTTP_METHOD.POST,
        body,
      }),
      async onQueryStarted(_, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        dispatch(CRMApi.util.invalidateTags([TAG_TYPES.USER]));
      },
    }),

    confirm: build.query<void, string>({
      query: (token) => ({
        url: `/confirm`,
        method: HTTP_METHOD.GET,
        params: { token },
      }),
    }),

    doubleCheck: build.mutation<void, AuthDoubleCheckREQ>({
      query: (body) => ({
        url: `/exists`,
        method: HTTP_METHOD.POST,
        body,
      }),
    }),

    forgotPassword: build.mutation<void, string>({
      query: (email) => ({
        url: 'admin/public/forgot-password',
        method: HTTP_METHOD.GET,
        params: { email },
      }),
    }),

    logout: build.mutation<void, void>({
      query: () => ({
        url: '/logout',
        method: HTTP_METHOD.POST,
      }),
      async onQueryStarted(_, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        dispatch(CRMApi.util.invalidateTags([TAG_TYPES.USER]));
      },
      invalidatesTags: [TAG_TYPES.USER],
    }),

    sendAuthCode: build.mutation({
      query: ({ email, authCodeType }: AuthSendCodeREQ) => ({
        url: 'admin/public/send-auth-code',
        body: { email, authCodeType },
        method: HTTP_METHOD.POST,
      }),
    }),

    resetPassword: build.mutation({
      query: ({ currentPassword, newPassword }: AuthResetPasswordREQ) => ({
        url: 'account/change-password',
        body: { currentPassword, newPassword },
        method: HTTP_METHOD.POST,
      }),
    }),
  }),
});

export const {
  useLoginMutation,
  useLogoutMutation,
  useRegisterMutation,
  useConfirmQuery,
  useDoubleCheckMutation,
  useForgotPasswordMutation,
  useSendAuthCodeMutation,
  useResetPasswordMutation,
} = authApi;
