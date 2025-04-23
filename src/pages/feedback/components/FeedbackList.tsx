import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { FeedbackDTO } from '@/data/feedback/dto/feedback.dto';
import { useGetFeedbackListQuery } from '@/data/feedback/feedback.api';
import { feedbackListRequestParamsToFilter } from '@/data/feedback/feedback.service';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { initialPagingState } from '@/utils/types/paging.type';
import queryString from 'query-string';
import { useMemo } from 'react';
import { useLocation } from 'react-router-dom';
import { FeedbackColumn } from './comlumn/FeedbackColumn';
import FeedbackFilter from './FeedbackFilter';

export default function FeedbackList() {
  const location = useLocation();
  const params = queryString.parse(location.search);

  const { feedbackListFilter } = useMemo(() => {
    const feedbackListFilter = feedbackListRequestParamsToFilter(params);
    return { feedbackListFilter };
  }, [params]);

  const page = useMemo(() => {
    const page = params[PARAM_FIELD.PAGE] || initialPagingState.page;
    if (page !== 0) return Number(page) - 1;
    return Number(page);
  }, [params]);

  const { data, isLoading, isFetching } = useGetFeedbackListQuery(feedbackListFilter);

  return (
    <div className='flex flex-col gap-5'>
      <FeedbackFilter />
      <TableBuilder<FeedbackDTO>
        rowKey='feedbackID'
        columns={FeedbackColumn()}
        data={data?.content.map((item, idx) => ({ ...item, index: page * feedbackListFilter.size + idx + 1 })) ?? []}
        isLoading={isLoading || isFetching}
      />
      <CustomTablePagination
        totalItems={data?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
