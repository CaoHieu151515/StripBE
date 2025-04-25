import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import CensorDriverRequestList from './components/CensorDriverRequestList';

export default function CensorDriverRequestPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Kiểm Duyệt Tài Xế'>
        <CensorDriverRequestList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
