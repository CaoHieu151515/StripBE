import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { PackageColumn } from '../column/PackageColumn';
import PackageFilter from './PackageFilter';
import usePackageData from '@/data/services/api/package/usePackageData';
import { PackageGetAllDTO } from '@/@types/dto/packageDTO';
import CustomTablePagination from '@/components/table/CustomTablePagination';

export default function PackageCardList() {
  const { PackageData, isFetching } = usePackageData();

  return (
    <div className='flex flex-col gap-5'>
      <PackageFilter />
      <TableBuilder<PackageGetAllDTO>
        rowKey='id'
        columns={PackageColumn()}
        data={
          PackageData?.content.map((item, idx) => ({
            ...item,
            index: PackageData?.page * PackageData?.size + idx + 1,
          })) ?? []
        }
        isLoading={isFetching}
      />
      <CustomTablePagination
        showSizeChanger={false}
        totalItems={PackageData?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
