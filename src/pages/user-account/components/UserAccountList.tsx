import CustomTablePagination from '@/components/table/CustomTablePagination';
import { TableBuilder } from '@/components/table/TableBuilder';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import { UserColumn } from './column/UserAccountColumn';
import UserAccountFilter from './UserAccountFilter';
import useUsersData from '@/data/services/api/user/useUserData';
import { UserGetAllDTO } from '@/@types/dto/userDTO';

export default function UserAccountList() {
  const { UserData, isFetching } = useUsersData();
  return (
    <div className='flex flex-col gap-5'>
      <UserAccountFilter />
      <TableBuilder<UserGetAllDTO>
        rowKey='userId'
        columns={UserColumn()}
        data={
          UserData?.content.map((item, idx) => ({ ...item, index: UserData?.page * UserData?.size + idx + 1 })) ?? []
        }
        isLoading={isFetching}
      />
      <CustomTablePagination
        totalItems={UserData?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        isScrollAfterPageChange
      />
    </div>
  );
}
