import { apiBaseUrl, STORAGE, TAG_TYPES } from '@/utils/constants/shared.constant';
import { BaseQueryFn, createApi, FetchArgs, fetchBaseQuery, FetchBaseQueryError } from '@reduxjs/toolkit/query/react';
import Cookie from 'js-cookie';
import { logoutThunk } from './auth/auth.thunk';

const baseQuery = fetchBaseQuery({
  baseUrl: apiBaseUrl,
  prepareHeaders: (headers) => {
    const accessToken = Cookie.get(STORAGE.ACCESS_TOKEN);

    if (accessToken && !headers.has('Authorization')) {
      headers.set('Authorization', `Bearer ${accessToken}`);
    }
    return headers;
  },
});

const baseQueryWithReAuth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
  args,
  api,
  extraOptions,
) => {
  const result = await baseQuery(args, api, extraOptions);
  if (result.error?.status === 401) {
    api.dispatch(logoutThunk());
  }
  return result;
};

// initialize an empty api service that we'll inject endpoints into later as needed
export const CRMApi = createApi({
  baseQuery: baseQueryWithReAuth,
  endpoints: () => ({}),
  tagTypes: [TAG_TYPES.USER],
});
