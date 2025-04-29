import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';
import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { ColumnsType } from 'antd/es/table';
import { useState } from 'react';
import CensorVehicleModal from '../CensorVehicleModal';

export const CensorVehicleColumn = (): ColumnsType<CensorVehicleDTO> => {
  const [isModalDetailOpen, setIsModalDetailOpen] = useState<number | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='STT' label={'STT'} sortKey='STT' />,
      key: 'STT',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.index}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='userLastName' label={'Tên Tài Xế'} sortKey='userLastName' />,
      key: 'Tên',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.lastName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='vehicleNumber' label={'Số Máy'} sortKey='vehicleNumber' />,
      key: 'vehicleNumber',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.vehicle?.vehicleNumber}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='vehicleType' label={'Loại Phương Tiện'} sortKey='vehicleType' />,
      key: 'vehicleType',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.vehicle?.vehicleType}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='vehicleBrand' label={'Hãng'} sortKey='vehicleBrand' />,
      key: 'vehicleBrand',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.vehicle?.vehicleBrand}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='vehicleStatus' label={'Trạng Thái'} sortKey='vehicleStatus' />,
      key: 'vehicleStatus',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.vehicle?.status}</div>;
      },
    },
    // {
    //   title: () => <TableHeaderCell key='brand' label={'Brand'} sortKey='brand' />,
    //   key: 'brand',
    //   render: ({ ...props }: CensorVehicleDTO) => {
    //     return <div>{props?.brand}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='numberInsead' label={'Number InSead'} />,
    //   key: 'numberInsead',
    //   render: ({ ...props }: CensorVehicleDTO) => {
    //     return <div>{props?.numberInsead}</div>;
    //   },
    // },

    // {
    //   title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
    //   key: 'status',
    //   render: ({ ...props }: CensorVehicleDTO) => {
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
      render: ({ ...props }: CensorVehicleDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton
              label={'Xem chi tiết'}
              onClick={() => setIsModalDetailOpen(props?.vehicle?.id ?? null)}
            />
          </div>
          {isModalDetailOpen === props?.vehicle?.id && (
            <CensorVehicleModal
              open={isModalDetailOpen === props?.vehicle?.id}
              setOpen={() => setIsModalDetailOpen(null)}
              data={props ?? []}
            />
          )}
        </>
      ),
    },
  ];
};
