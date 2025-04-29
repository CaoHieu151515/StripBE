import { CensorDriverRequestDTO } from '@/@types/dto/censorDriverRequestDTO';
import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { ColumnsType } from 'antd/es/table';
import { useState } from 'react';
import CensorDriverDetailModal from '../CensorDriverDetailModal';

export const CensorDriverRequestColumn = (): ColumnsType<CensorDriverRequestDTO> => {
  const [isModalDetailOpen, setIsModalDetailOpen] = useState<string | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='STT' label={'STT'} sortKey='STT' />,
      key: 'STT',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.index}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='firstName' label={'Tên'} sortKey='firstName' />,
      key: 'firstName',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.firstName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='lastName' label={'Họ'} sortKey='lastName' />,
      key: 'lastName',
      render: ({ ...props }: CensorDriverRequestDTO) => {
        return <div>{props?.lastName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='phone' label={'Số Điện Thoại'} sortKey='phone' />,
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
      title: () => <TableHeaderCell key='action' label={'Hành Động'} />,
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
