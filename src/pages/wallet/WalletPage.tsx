import { TableUtilProvider } from '@/components/table/TableContextUtil';
import DefaultContainer from '@/layouts/DefaultContainer';
import TransactionList from './components/TransactionList';
import WalletOverview from './components/WalletOverview';

export default function WalletPage() {
  return (
    <TableUtilProvider>
      <DefaultContainer title='Ví'>
        <div className='flex max-w-full flex-col gap-6'>
          <WalletOverview />
          <TransactionList />
        </div>
      </DefaultContainer>
    </TableUtilProvider>
  );
}
