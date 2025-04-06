import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { TripDTO } from '@/data/trip/dto/trip.dto';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import { TRIP_STATUS } from '@/utils/enum/trip/trip-status.enum';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';

export const TripColumn = (): ColumnsType<TripDTO> => {
  const navigate = useNavigate();

  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      width: 60,
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='driverName' label={'Driver Name'} sortKey='driverName' />,
      key: 'driverName',
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.driver?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='departureDate' label={'Departure Date'} sortKey='departureDate' />,
      key: 'departureDate',
      width: 150,
      render: ({ ...props }: TripDTO) => {
        return <div>{dayjs(props?.departureDate).format(DATE_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='arrivalDate' label={'Arrival Date'} sortKey='arrivalDate' />,
      key: 'arrivalDate',
      width: 150,
      render: ({ ...props }: TripDTO) => {
        return <div>{dayjs(props?.arrivalDate).format(DATE_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='departureLocation' label={'Departure Location'} />,
      key: 'departureLocation',
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.departureLocation}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='arrivalLocation' label={'Arrival Location'} />,
      key: 'arrivalLocation',
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.arrivalLocation}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='departureTime' label={'Departure Time'} sortKey='departureTime' />,
      key: 'departureTime',
      render: ({ ...props }: TripDTO) => {
        return <div>{dayjs(props?.departureTime).format(DATE_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='arrivalTime' label={'Arrival Time'} sortKey='arrivalTime' />,
      key: 'arrivalTime',
      width: 130,
      render: ({ ...props }: TripDTO) => {
        return <div>{dayjs(props?.arrivalTime).format(TIME_24H_FORMAT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
      key: 'status',
      render: ({ ...props }: TripDTO) => {
        return (
          <div className='capitalize'>
            {props?.status === TRIP_STATUS.ACCEPTED ? (
              <Tag color='success' className='w-20 text-center'>
                {props?.status}
              </Tag>
            ) : props?.status === TRIP_STATUS.PENDING ? (
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
      render: ({ ...props }: TripDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton
              label={'See Details'}
              onClick={() => navigate(MY_ROUTE.TRIP.detail(props?.id.toString()))}
            />
          </div>
        </>
      ),
    },
  ];
};
