import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { PackageColumn } from '../column/PackageColumn';
import PackageFilter from './PackageFilter';
import usePackageData from '@/data/services/api/package/usePackageData';
import { PackageGetAllDTO } from '@/@types/dto/packageDTO';

// const PACKAGE_DATA = [
//   {
//     id: '1',
//     packageName: 'Bronze Package',
//     packageTime: 1,
//     packageBonusTime: 0,
//     packagePrice: 2000000,
//   },
//   {
//     id: '2',
//     packageName: 'Silver Package',
//     packageTime: 3,
//     packageBonusTime: 1,
//     packagePrice: 6000000,
//   },
//   {
//     id: '3',
//     packageName: 'Gold Package',
//     packageTime: 6,
//     packageBonusTime: 3,
//     packagePrice: 12000000,
//   },
// ];

export default function PackageCardList() {
  const { PackageData, isLoading } = usePackageData();

  return (
    <div className='flex flex-col gap-5'>
      <PackageFilter />
      <TableBuilder<PackageGetAllDTO>
        rowKey='id'
        columns={PackageColumn()}
        data={PackageData ?? []}
        isLoading={false}
      />
      <CustomTablePagination
        totalItems={PackageData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
