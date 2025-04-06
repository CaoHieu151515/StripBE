import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { CensorDriverDTO } from '@/data/censor-driver/dto/censor-driver.dto';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { CensorDriverRequestData } from '../mocks/CensorDriverRequestData';
import CensorDriverRequestFilter from './CensorDriverRequestFilter';
import { CensorDriverRequestColumn } from './column/CensorDriverRequestColumn';

export default function CensorDriverRequestList() {
  return (
    <div className='flex flex-col gap-5'>
      <CensorDriverRequestFilter />
      <TableBuilder<CensorDriverDTO>
        rowKey='id'
        columns={CensorDriverRequestColumn()}
        data={CensorDriverRequestData}
        isLoading={false}
      />
      <CustomTablePagination
        totalItems={CensorDriverRequestData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
