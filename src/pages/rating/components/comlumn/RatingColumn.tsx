import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { RatingDTO } from '@/data/rating/dto/rating.dto';
import { formatPhoneNumber } from '@/utils/string.helper';
import { ColumnsType } from 'antd/es/table';
import { useState } from 'react';
import RatingDetailModal from '../RatingDetailModal';

export const RatingColumn = (): ColumnsType<RatingDTO> => {
  const [isShownDetailsModal, setIsShownDetailsModal] = useState<number | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      width: 60,
      render: ({ ...props }: RatingDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='driverName' label={'Driver Name'} sortKey='driverName' />,
      key: 'driverName',
      render: ({ ...props }: RatingDTO) => {
        return <div>{props?.driverName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='phoneNumber' label={'Phone Number'} sortKey='phoneNumber' />,
      key: 'phoneNumber',
      render: ({ ...props }: RatingDTO) => {
        return <div>{formatPhoneNumber(props?.phoneNumber)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='email' label={'Email'} sortKey='email' />,
      key: 'email',
      render: ({ ...props }: RatingDTO) => {
        return <div>{props?.email}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='gender' label={'Gender'} />,
      key: 'gender',
      render: ({ ...props }: RatingDTO) => {
        return <div className='capitalize'>{props?.gender}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='rating' label={'Rating'} />,
      key: 'rating',
      render: ({ ...props }: RatingDTO) => {
        return <div>{props?.rating + ' /5'}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='action' label={'Action'} />,
      key: 'action',
      render: ({ ...props }: RatingDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton label={'See Details'} onClick={() => setIsShownDetailsModal(props?.id)} />
          </div>
          {isShownDetailsModal === props?.id && (
            <RatingDetailModal
              open={isShownDetailsModal === props?.id}
              setOpen={() => setIsShownDetailsModal(null)}
              data={props}
            />
          )}
        </>
      ),
    },
  ];
};
