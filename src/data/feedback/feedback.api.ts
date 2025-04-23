import { TAG_TYPES } from '@/utils/constants/shared.constant';
import { HTTP_METHOD } from '@/utils/enum/http-method.enum';
import { PaginationRESP } from '@/utils/types/response.type';
import { CRMApi } from '../CRMApi.api';
import { GetFeedbackREQ } from './request/feedback.request';
import { GetFeedbackRESP } from './response/feedback.response';

export const feedbackApi = CRMApi.enhanceEndpoints({ addTagTypes: [TAG_TYPES.FEEDBACK] }).injectEndpoints({
  endpoints: (build) => ({
    getFeedbackList: build.query<PaginationRESP<GetFeedbackRESP>, GetFeedbackREQ>({
      query: (filter: GetFeedbackREQ) => ({
        url: 'manager/feedbacks',
        method: HTTP_METHOD.GET,
        params: { ...filter },
      }),
      providesTags: [{ type: TAG_TYPES.FEEDBACK }],
    }),
  }),
});

export const { useGetFeedbackListQuery } = feedbackApi;
