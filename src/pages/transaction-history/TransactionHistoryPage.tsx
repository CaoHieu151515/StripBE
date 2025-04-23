import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import TransactionHistoryList from './components/TransactionHistoryList';

export default function TransactionHistoryPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Lợi Nhuận Hệ Thống'>
        <TransactionHistoryList />
      </DefaultContainer>
    </TableUtilProvider>
  );
}
