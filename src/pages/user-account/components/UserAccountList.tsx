import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { UserColumn } from './column/UserAccountColumn';
import UserAccountFilter from './UserAccountFilter';
import { useEffect } from 'react';
import useUsersData from '@/data/services/api/user/useUserData';
import { UserGetAllDTO } from '@/@types/dto/userDTO';

export default function UserAccountList() {
  const { UserData, isLoading } = useUsersData();

  useEffect(() => {
    console.table(UserData);
  }, [UserData]);

  return (
    <div className='flex flex-col gap-5'>
      <UserAccountFilter />
      <TableBuilder<UserGetAllDTO> rowKey='userId' columns={UserColumn()} data={UserData ?? []} isLoading={isLoading} />
      <CustomTablePagination
        totalItems={UserData?.length || 1}
        queryKey={PARAM_FIELD.CURRENT_PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
