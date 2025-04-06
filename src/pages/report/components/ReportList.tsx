import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { ReportDTO } from '@/data/report/dto/report.dto';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { ReportData } from '../mocks/ReportData';
import { ReportColumn } from './comlumn/ReportColumn';
import ReportFilter from './ReportFilter';

export default function ReportList() {
  return (
    <div className='flex flex-col gap-5'>
      <ReportFilter />
      <TableBuilder<ReportDTO> rowKey='id' columns={ReportColumn()} data={ReportData} isLoading={false} />
      <CustomTablePagination
        totalItems={ReportData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
