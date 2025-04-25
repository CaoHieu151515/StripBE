import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import CensorDriverRequestFilter from './CensorDriverRequestFilter';
import { CensorDriverRequestColumn } from './column/CensorDriverRequestColumn';

import { CensorDriverRequestDTO } from '@/@types/dto/censorDriverRequestDTO';
import useCensorDriverRequestData from '@/data/services/api/censorDriverRequest/useCensorDriverRequestData';

export default function CensorDriverRequestList() {
  const { CensorDriverRequestData, isFetching } = useCensorDriverRequestData();

  return (
    <div className='flex flex-col gap-5'>
      <CensorDriverRequestFilter />
      <TableBuilder<CensorDriverRequestDTO>
        rowKey='userId'
        columns={CensorDriverRequestColumn()}
        data={CensorDriverRequestData?.content ?? []}
        isLoading={isFetching}
      />
      <CustomTablePagination
        totalItems={CensorDriverRequestData?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
