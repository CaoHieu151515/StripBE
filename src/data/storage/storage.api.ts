import { BaseResponse } from '@/utils/types/response.type';
import { CRMApi } from '../CRMApi.api';
import { GeneratePresignedURLREQ, GetPresignedURLREQ } from './storage.request';
import { GeneratePresignedURLRESP, GetPresignedURLRESP } from './storage.response';

const storageApi = CRMApi.injectEndpoints({
  endpoints: (build) => ({
    generatePresignedURL: build.mutation<GeneratePresignedURLRESP, GeneratePresignedURLREQ>({
      query: ({ storageContentType, storageDomain }) => ({
        url: 'storages/generate-presigned-url',
        params: { storageContentType, storageDomain },
      }),
      transformResponse: (response: BaseResponse<GeneratePresignedURLRESP>) => response.data,
    }),
    getPresignedURL: build.query<GetPresignedURLRESP, GetPresignedURLREQ>({
      query: ({ key }) => ({
        url: 'storages/get-presigned-url',
        params: { key },
      }),
      transformResponse: (response: BaseResponse<GetPresignedURLRESP>) => response.data,
    }),
  }),
  overrideExisting: false,
});

export const { useGeneratePresignedURLMutation, useLazyGetPresignedURLQuery } = storageApi;
