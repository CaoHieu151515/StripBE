import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { TripDTO } from '@/data/trip/dto/trip.dto';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { TripData } from '../mocks/TripData';
import TripFilter from './TripFilter';
import { TripColumn } from './comlumn/TripColumn';

export default function TripList() {
  return (
    <div className='flex flex-col gap-5'>
      <TripFilter />
      <TableBuilder<TripDTO> rowKey='id' columns={TripColumn()} data={TripData} isLoading={false} />
      <CustomTablePagination
        totalItems={TripData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
