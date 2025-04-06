import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { CensorVehicleDTO } from '@/data/censor-vehicle/dto/censor-vehicle.dto';
import { CENSOR_DRIVER_STATUS } from '@/utils/enum/censor-driver/censor-driver.enum';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import { useState } from 'react';
import CensorVehicleModal from '../CensorVehicleModal';

export const CensorVehicleColumn = (): ColumnsType<CensorVehicleDTO> => {
  const [isModalDetailOpen, setIsModalDetailOpen] = useState<number | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='vehicleName' label={'Vehicle Name'} sortKey='vehicleName' />,
      key: 'vehicleName',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.vehicleName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='vehicleType' label={'Vehicle Type'} sortKey='vehicleType' />,
      key: 'vehicleType',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.vehicleType}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='brand' label={'Brand'} sortKey='brand' />,
      key: 'brand',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.brand}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='numberInsead' label={'Number InSead'} />,
      key: 'numberInsead',
      render: ({ ...props }: CensorVehicleDTO) => {
        return <div>{props?.numberInsead}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
      key: 'status',
      render: ({ ...props }: CensorVehicleDTO) => {
        return (
          <div className='capitalize'>
            {props?.status === CENSOR_DRIVER_STATUS.APPROVED ? (
              <Tag color='success' className='w-20 text-center'>
                {props?.status}
              </Tag>
            ) : props?.status === CENSOR_DRIVER_STATUS.PENDING ? (
              <Tag color='processing' className='w-20 text-center'>
                {props?.status}
              </Tag>
            ) : (
              <Tag color='error' className='w-20 text-center'>
                {props?.status}
              </Tag>
            )}
          </div>
        );
      },
    },
    {
      title: () => <TableHeaderCell key='action' label={'Action'} />,
      key: 'action',
      render: ({ ...props }: CensorVehicleDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton label={'See Details'} onClick={() => setIsModalDetailOpen(props.id)} />
          </div>
          {isModalDetailOpen === props.id && (
            <CensorVehicleModal
              open={isModalDetailOpen === props.id}
              setOpen={() => setIsModalDetailOpen(null)}
              data={props}
            />
          )}
        </>
      ),
    },
  ];
};
