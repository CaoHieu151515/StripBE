import { TableBuilder } from '@/components/table/TableBuilder';
import { TransactionHistoryDTO } from '@/data/transaction/dto/transaction-history.dto';
import { TransactionHistoryColumn } from './column/TransactionHistoryColumn';
import { TransactionHistoryData } from './mocks/TransactionHistoryData';
import TransactionFilter from './TransactionFilter';

export default function TransactionHistoryList() {
  return (
    <div className='flex max-w-full flex-col gap-5'>
      <TransactionFilter />
      <TableBuilder<TransactionHistoryDTO>
        rowKey='id'
        columns={TransactionHistoryColumn()}
        data={TransactionHistoryData}
        isLoading={false}
      />
    </div>
  );
}
