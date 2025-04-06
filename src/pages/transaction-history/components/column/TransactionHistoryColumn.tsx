import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { TransactionHistoryDTO } from '@/data/transaction/dto/transaction-history.dto';
import { DATE_SHORT_TIME_FORMAT_DOT } from '@/utils/constants/date.constant';
import { TRANSACTION_STATUS } from '@/utils/enum/transaction/transaction.enum';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';

export const TransactionHistoryColumn = (): ColumnsType<TransactionHistoryDTO> => {
  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      width: 64,
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'Full Name'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div>{props?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='packageTime' label={'Package Time'} sortKey='packageTime' />,
      key: 'phone',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div>{`${props?.packageTime} ${props?.packageTime > 1 ? 'months' : 'month'}`}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentTime' label={'Payment Time'} sortKey='paymentTime' />,
      key: 'paymentTime',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div>{dayjs(props?.paymentTime).format(DATE_SHORT_TIME_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentDate' label={'Payment Date'} sortKey='paymentDate' />,
      key: 'paymentDate',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div>{dayjs(props?.paymentDate).format(DATE_SHORT_TIME_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentMethod' label={'Payment Method'} sortKey='paymentMethod' />,
      key: 'paymentMethod',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div className='capitalize'>{props?.paymentMethod}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='transactionCode' label={'Transaction Code'} sortKey='transactionCode' />,
      key: 'transactionCode',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return <div className='capitalize'>{props?.transactionCode}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentStatus' label={'Payment Status'} sortKey='paymentStatus' />,
      key: 'paymentStatus',
      render: ({ ...props }: TransactionHistoryDTO) => {
        return (
          <div className='capitalize'>
            {props?.paymentStatus === TRANSACTION_STATUS.SUCCESS ? (
              <Tag color='success' className='w-20 text-center'>
                {props?.paymentStatus}
              </Tag>
            ) : props?.paymentStatus === TRANSACTION_STATUS.PENDING ? (
              <Tag color='processing' className='w-20 text-center'>
                {props?.paymentStatus}
              </Tag>
            ) : (
              <Tag color='error' className='w-20 text-center'>
                {props?.paymentStatus}
              </Tag>
            )}
          </div>
        );
      },
    },
    // {
    //   title: () => <TableHeaderCell key='action' label={'Action'} />,
    //   key: 'action',
    //   render: ({ ...props }: TransactionHistoryDTO) => (
    //     <div className='flex justify-start w-full'>
    //       <CustomTableActionsButton
    //         label={'See Details'}
    //         onClick={() => navigate(MY_ROUTE.USER.CENSOR_DRIVER_REQUEST.detail(props?.id.toString()))}
    //       />
    //     </div>
    //   ),
    // },
  ];
};
