import { TableBuilder } from '@/components/table/TableBuilder';
import { TransactionColumn } from './column/TransactionColumn';
// import { TransactionData } from './mocks/TransactionData';
import TransactionFilter from './TransactionFilter';
import useWalletData from '@/data/services/api/wallet/useWalletData';
import { WalletDTO } from '@/@types/dto/walletDTO';
import { PARAM_FIELD } from '@/utils/enum/param-field.enum';
import CustomTablePagination from '@/components/table/CustomTablePagination';

export default function TransactionList() {
  const { WalletData, isLoading } = useWalletData();
  return (
    <div className='flex max-w-full flex-col gap-5'>
      <h3 className='mb-2 text-2xl font-semibold text-gray-900'>Danh sách các giao dịch</h3>
      <TransactionFilter />
      <TableBuilder<WalletDTO>
        rowKey='transactionId'
        columns={TransactionColumn()}
        data={WalletData?.content ?? []}
        isLoading={isLoading}
      />
      <CustomTablePagination
        totalItems={WalletData?.totalElements || 1}
        queryKey={PARAM_FIELD.PAGE}
        // isScrollAfterPageChange
      />
    </div>
  );
}
