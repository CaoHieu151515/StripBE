import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { FeedbackDTO } from '@/data/feedback/dto/feedback.dto';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { FeedbackData } from '../mocks/FeedbackData';
import { FeedbackColumn } from './comlumn/FeedbackColumn';
import FeedbackFilter from './FeedbackFilter';

export default function FeedbackList() {
  return (
    <div className='flex flex-col gap-5'>
      <FeedbackFilter />
      <TableBuilder<FeedbackDTO> rowKey='id' columns={FeedbackColumn()} data={FeedbackData} isLoading={false} />
      <CustomTablePagination
        totalItems={FeedbackData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
