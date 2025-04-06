import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { RatingDTO } from '@/data/rating/dto/rating.dto';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { RatingData } from '../mocks/RatingData';
import RatingFilter from './RatingFilter';
import { RatingColumn } from './comlumn/RatingColumn';

export default function RatingList() {
  return (
    <div className='flex flex-col gap-5'>
      <RatingFilter />
      <TableBuilder<RatingDTO> rowKey='id' columns={RatingColumn()} data={RatingData} isLoading={false} />
      <CustomTablePagination
        totalItems={RatingData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
