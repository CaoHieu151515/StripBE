import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import UserAccountList from './components/UserAccountList';

export default function UserAccountPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='User Account'>
        <UserAccountList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
