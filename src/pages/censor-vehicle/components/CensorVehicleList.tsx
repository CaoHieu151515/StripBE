import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import CensorVehicleFilter from './CensorVehicleFilter';
import { CensorVehicleColumn } from './column/CensorVehicleColumn';
import useCensorVehicleData from '@/data/services/api/censorVehicle/useCensorVehicleData';
import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';

export default function CensorVehicleList() {
  const { CensorVehicleData, isLoading } = useCensorVehicleData();
  return (
    <div className='flex flex-col gap-5'>
      <CensorVehicleFilter />
      <TableBuilder<CensorVehicleDTO>
        rowKey='driverId'
        columns={CensorVehicleColumn()}
        data={CensorVehicleData ?? []}
        isLoading={isLoading}
      />
      <CustomTablePagination
        totalItems={CensorVehicleData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
