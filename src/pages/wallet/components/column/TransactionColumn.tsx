import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { TransactionDTO } from '@/data/transaction/dto/transaction.dto';
import { DATE_SHORT_TIME_FORMAT_DOT } from '@/utils/constants/date.constant';
import { formatPhoneNumber } from '@/utils/string.helper';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';

export const TransactionColumn = (): ColumnsType<TransactionDTO> => {
  const navigate = useNavigate();

  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      width: 64,
      render: ({ ...props }: TransactionDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'Full Name'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: TransactionDTO) => {
        return <div>{props?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='phone' label={'Phone Number'} sortKey='phone' />,
      key: 'phone',
      render: ({ ...props }: TransactionDTO) => {
        return <div>{formatPhoneNumber(props?.phone)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='email' label={'Email'} sortKey='email' />,
      key: 'email',
      render: ({ ...props }: TransactionDTO) => {
        return <div>{props?.email}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentTime' label={'Payment Time'} sortKey='paymentTime' />,
      key: 'paymentTime',
      render: ({ ...props }: TransactionDTO) => {
        return <div>{dayjs(props?.paymentTime).format(DATE_SHORT_TIME_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentDate' label={'Payment Date'} sortKey='paymentDate' />,
      key: 'paymentDate',
      render: ({ ...props }: TransactionDTO) => {
        return <div>{dayjs(props?.paymentDate).format(DATE_SHORT_TIME_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentMethod' label={'Payment Method'} sortKey='paymentMethod' />,
      key: 'paymentMethod',
      render: ({ ...props }: TransactionDTO) => {
        return <div className='capitalize'>{props?.paymentMethod}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='paymentStatus' label={'Payment Status'} sortKey='paymentStatus' />,
      key: 'paymentStatus',
      render: ({ ...props }: TransactionDTO) => {
        return (
          <div className='capitalize'>
            {props?.paymentStatus === 'Accepted' ? (
              <Tag color='success' className='w-20 text-center'>
                {props?.paymentStatus}
              </Tag>
            ) : props?.paymentStatus === 'Pending' ? (
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
    //   render: ({ ...props }: TransactionDTO) => (
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
