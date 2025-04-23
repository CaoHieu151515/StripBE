import { WalletDTO } from '@/@types/dto/walletDTO';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';

export const TransactionColumn = (): ColumnsType<WalletDTO> => {
  const navigate = useNavigate();

  return [
    // {
    //   title: () => <TableHeaderCell key='transactionId?: ' label={'transactionId?: '} sortKey='transactionId?: ' />,
    //   key: 'transactionId?: ',
    //   width: 64,
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div>{props?.transactionId }</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='type' label={'Type'} sortKey='type' />,
    //   key: 'type',
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div>{props?.type}</div>;
    //   },
    // },
    {
      title: () => <TableHeaderCell key='fromOwner' label={'fromOwner'} sortKey='fromOwner' />,
      key: 'fromOwner',
      render: ({ ...props }: WalletDTO) => {
        return <div>{props?.fromOwner}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='amount' label={'amount (vnd)'} sortKey='amount' />,
      key: 'amount',
      render: ({ ...props }: WalletDTO) => {
        return <div>{props?.amount}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='toOwner' label={'toOwner'} sortKey='toOwner' />,
      key: 'toOwner',
      render: ({ ...props }: WalletDTO) => {
        return <div>{props?.toOwner}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='createdDate' label={'createdDate'} sortKey='createdDate' />,
      key: 'createdDate',
      render: ({ ...props }: WalletDTO) => {
        return <div>{dayjs(props?.createdDate).format('DD/MM/YYYY HH:mm')}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='description' label={'description'} sortKey='description' />,
      key: 'description',
      render: ({ ...props }: WalletDTO) => {
        return <div>{props?.description}</div>;
      },
    },
    // {
    //   title: () => <TableHeaderCell key='phone' label={'Phone Number'} sortKey='phone' />,
    //   key: 'phone',
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div>{formatPhoneNumber(props?.phone)}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='email' label={'Email'} sortKey='email' />,
    //   key: 'email',
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div>{props?.email}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='paymentTime' label={'Payment Time'} sortKey='paymentTime' />,
    //   key: 'paymentTime',
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div>{dayjs(props?.paymentTime).format(DATE_SHORT_TIME_FORMAT_DOT)}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='paymentDate' label={'Payment Date'} sortKey='paymentDate' />,
    //   key: 'paymentDate',
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div>{dayjs(props?.paymentDate).format(DATE_SHORT_TIME_FORMAT_DOT)}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='paymentMethod' label={'Payment Method'} sortKey='paymentMethod' />,
    //   key: 'paymentMethod',
    //   render: ({ ...props }: WalletDTO) => {
    //     return <div className='capitalize'>{props?.paymentMethod}</div>;
    //   },
    // },

    //////////////
    // {
    //   title: () => <TableHeaderCell key='paymentStatus' label={'Payment Status'} sortKey='paymentStatus' />,
    //   key: 'paymentStatus',
    //   render: ({ ...props }: WalletDTO) => {
    //     return (
    //       <div className='capitalize'>
    //         {props?.paymentStatus === 'Accepted' ? (
    //           <Tag color='success' className='w-20 text-center'>
    //             {props?.paymentStatus}
    //           </Tag>
    //         ) : props?.paymentStatus === 'Pending' ? (
    //           <Tag color='processing' className='w-20 text-center'>
    //             {props?.paymentStatus}
    //           </Tag>
    //         ) : (
    //           <Tag color='error' className='w-20 text-center'>
    //             {props?.paymentStatus}
    //           </Tag>
    //         )}
    //       </div>
    //     );
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='action' label={''} />,
    //   key: 'action',
    //   render: ({ ...props }: WalletDTO) => (
    //     <div className='flex justify-start w-full'>
    //       <CustomTableActionsButton
    //         label={'Xem chi tiết'}
    //         onClick={() => navigate(MY_ROUTE.USER.CENSOR_DRIVER_REQUEST.detail(props?.id.toString()))}
    //       />
    //     </div>
    //   ),
    // },
  ];
};
