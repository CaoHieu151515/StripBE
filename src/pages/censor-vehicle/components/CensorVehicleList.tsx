import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { CensorVehicleDTO } from '@/data/censor-vehicle/dto/censor-vehicle.dto';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { CensorVehicleData } from '../mocks/CensorVehicleData';
import CensorVehicleFilter from './CensorVehicleFilter';
import { CensorVehicleColumn } from './column/CensorVehicleColumn';

export default function CensorVehicleList() {
  return (
    <div className='flex flex-col gap-5'>
      <CensorVehicleFilter />
      <TableBuilder<CensorVehicleDTO>
        rowKey='id'
        columns={CensorVehicleColumn()}
        data={CensorVehicleData}
        isLoading={false}
      />
      <CustomTablePagination
        totalItems={CensorVehicleData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
