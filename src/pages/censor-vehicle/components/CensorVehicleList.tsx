import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import CensorVehicleFilter from './CensorVehicleFilter';
import { CensorVehicleColumn } from './column/CensorVehicleColumn';
import useCensorVehicleData from '@/data/services/api/censorVehicle/useCensorVehicleData';
import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';

export default function CensorVehicleList() {
  const { CensorVehicleData, isFetching } = useCensorVehicleData();
  return (
    <div className='flex flex-col gap-5'>
      <CensorVehicleFilter />
      <TableBuilder<CensorVehicleDTO>
        rowKey='driverId'
        columns={CensorVehicleColumn()}
        data={
          CensorVehicleData?.content?.map((item, idx) => ({
            ...item,
            index: CensorVehicleData?.page * CensorVehicleData?.size + idx + 1,
          })) ?? []
        }
        isLoading={isFetching}
      />
      <CustomTablePagination
        totalItems={CensorVehicleData?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
