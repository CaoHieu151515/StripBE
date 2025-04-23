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
      title: () => <TableHeaderCell key='stt' label={'STT'} />,
      key: 'stt',
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.index}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='startLocation' label={'Điểm khởi hành'} />,
      key: 'startLocation',
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.startLocation}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='endlocation' label={'Điểm đến'} />,
      key: 'endlocation',
      render: ({ ...props }: TripDTO) => {
        return <div>{props?.endlocation}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='startDay' label={'Ngày và thời gian khởi hành'} />,
      key: 'startDay',
      render: ({ ...props }: TripDTO) => {
        return (
          <div>
            {dayjs(props?.startDay).format(DATE_FORMAT_DOT) + ' - ' + dayjs(props?.startDay).format(TIME_24H_FORMAT)}
          </div>
        );
      },
    },
    // {
    //   title: () => <TableHeaderCell key='endDay' label={'Ngày tới'} />,
    //   key: 'endDay',
    //   width: 150,
    //   render: ({ ...props }: TripDTO) => {
    //     return <div>{dayjs(props?.endDay).format(DATE_FORMAT_DOT)}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='startTime' label={'Thời gian khởi hành'} />,
    //   key: 'startTime',
    //   render: ({ ...props }: TripDTO) => {
    //     return <div>{dayjs(props?.startDay).format(TIME_24H_FORMAT)}</div>;
    //   },
    // },
    // {
    //   title: () => <TableHeaderCell key='endTime' label={'Thời gian đến'} />,
    //   key: 'endTime',
    //   width: 130,
    //   render: ({ ...props }: TripDTO) => {
    //     return <div>{dayjs(props?.endDay).format(TIME_24H_FORMAT)}</div>;
    //   },
    // },
    {
      title: () => <TableHeaderCell key='price' label={'Giá (VNĐ)'} />,
      key: 'price',
      width: 100,
      render: ({ ...props }: TripDTO) => {
        return (
          <div className='text-right'>
            {props?.price.toLocaleString('vi-VN', {
              minimumFractionDigits: 0,
              maximumFractionDigits: 0,
            })}
          </div>
        );
      },
    },
    // {
    //   title: () => <TableHeaderCell key='totalTime' label={'Thời gian'} />,
    //   key: 'totalTime',
    //   width: 150,
    //   render: ({ ...props }: TripDTO) => {
    //     return (
    //       <div>
    //         {props?.totalTime > 120
    //           ? `${Math.floor(props?.totalTime / 60)} giờ ${props?.totalTime % 60} phút`
    //           : `${props?.totalTime} phút`}
    //       </div>
    //     );
    //   },
    // },
    {
      title: () => <TableHeaderCell key='status' label={'Status'} />,
      key: 'status',
      render: ({ ...props }: TripDTO) => {
        return (
          <div className='capitalize'>
            {props?.status === TRIP_STATUS.DONE ? (
              <Tag color='success' className='w-36 text-center'>
                Đã hoàn thành
              </Tag>
            ) : props?.status === TRIP_STATUS.CONFIRMING ? (
              <Tag color='warning' className='w-36 text-center'>
                Chờ xác nhận
              </Tag>
            ) : props?.status === TRIP_STATUS.ON_GOING ? (
              <Tag color='processing' className='w-36 text-center'>
                Đang đi
              </Tag>
            ) : props?.status === TRIP_STATUS.UPCOMING ? (
              <Tag color='geekblue' className='w-36 text-center'>
                Chuẩn bị khởi hành
              </Tag>
            ) : props?.status === TRIP_STATUS.REJECTED ? (
              <Tag color='error' className='w-36 text-center'>
                Đã từ chối
              </Tag>
            ) : props?.status === TRIP_STATUS.CANCEL ? (
              <Tag color='error' className='w-36 text-center'>
                Đã hủy
              </Tag>
            ) : props?.status === TRIP_STATUS.RESEND ? (
              <Tag color='blue-inverse' className='w-36 text-center'>
                Đã gửi lại
              </Tag>
            ) : (
              <Tag color='default' className='w-36 text-center'>
                Chờ xác nhận
              </Tag>
            )}
          </div>
        );
      },
    },
    {
      title: () => <TableHeaderCell key='action' label={''} />,
      key: 'action',
      render: ({ ...props }: TripDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton
              label={'Xem chi tiết'}
              onClick={() => navigate(MY_ROUTE.TRIP.detail(props?.stripID.toString()))}
            />
          </div>
        </>
      ),
    },
  ];
};
