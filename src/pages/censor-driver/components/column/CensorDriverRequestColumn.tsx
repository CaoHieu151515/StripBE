import { CensorDriverRequestDTO } from '@/@types/dto/censorDriverRequestDTO';
import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { ColumnsType } from 'antd/es/table';
import { useState } from 'react';
import CensorDriverDetailModal from '../CensorDriverDetailModal';

export const CensorDriverRequestColumn = (): ColumnsType<CensorDriverRequestDTO> => {
  const [isModalDetailOpen, setIsModalDetailOpen] = useState<string | null>(null);

  return [
    // {
    //   title: () => <TableHeaderCell key='driverId' label={'driverId'} sortKey='usedriverIdrId' />,
    //   key: 'driverId',
    //   render: ({ ...props }: CensorDriverRequestDTO) => {
    //     return <div>{props?.driverId}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='userId' label={'userId'} sortKey='userId' />,
    //   key: 'userId',
    //   render: ({ ...props }: CensorDriverRequestDTO) => {
    //     return <div>{props?.userId}</div>;
    //   },
    // },
    {
      title: () => <TableHeaderCell key='firstName' label={'firstName'} sortKey='firstName' />,
      key: 'firstName',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.firstName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='lastName' label={'lastName'} sortKey='lastName' />,
      key: 'lastName',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.lastName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='phone' label={'Phone Number'} sortKey='phone' />,
      key: 'phone',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.phone}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='email' label={'Email'} sortKey='email' />,
      key: 'email',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.email}</div>;
      },
    },
    // {
    //   title: () => <TableHeaderCell key='gender' label={'Gender'} />,
    //   key: 'gender',
    //   render: ({ ...props }: CensorDriverRequestDTO) => {
    //     return <div className='capitalize'>{props?.gender}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
    //   key: 'status',
    //   render: ({ ...props }: CensorDriverRequestDTO) => {
    //     return (
    //       <div className='capitalize'>
    //         {props?.status === CENSOR_DRIVER_STATUS.APPROVED ? (
    //           <Tag color='success' className='w-20 text-center'>
    //             {props?.status}
    //           </Tag>
    //         ) : props?.status === CENSOR_DRIVER_STATUS.PENDING ? (
    //           <Tag color='processing' className='w-20 text-center'>
    //             {props?.status}
    //           </Tag>
    //         ) : (
    //           <Tag color='error' className='w-20 text-center'>
    //             {props?.status}
    //           </Tag>
    //         )}
    //       </div>
    //     );
    //   },
    // },

    {
      title: () => <TableHeaderCell key='action' label={'Action'} />,
      key: 'action',
      render: ({ ...props }: CensorDriverRequestDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton
              label={'Xem chi tiết'}
              onClick={() => setIsModalDetailOpen(props.userId ?? null)}
            />
          </div>
          {isModalDetailOpen === props.userId && (
            <CensorDriverDetailModal
              open={isModalDetailOpen === props.userId}
              setOpen={() => setIsModalDetailOpen(null)}
              data={props}
            />
          )}
        </>
      ),
    },
  ];
};
