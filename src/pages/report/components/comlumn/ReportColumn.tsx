import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { ReportDTO } from '@/data/report/dto/report.dto';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { useState } from 'react';
import ReportDetailModal from '../ReportDetailModal';

export const ReportColumn = (): ColumnsType<ReportDTO> => {
  const [isShownDetailsModal, setIsShownDetailsModal] = useState<number | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      width: 60,
      render: ({ ...props }: ReportDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'Name'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: ReportDTO) => {
        return <div>{props?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='reportDate' label={'Report Date'} sortKey='reportDate' />,
      key: 'reportDate',
      width: 150,
      render: ({ ...props }: ReportDTO) => {
        return <div>{dayjs(props?.reportDate).format(DATE_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='reportTime' label={'Report Time'} sortKey='reportTime' />,
      key: 'reportTime',
      width: 150,
      render: ({ ...props }: ReportDTO) => {
        return <div>{dayjs(props?.reportDate).format(TIME_24H_FORMAT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='driverName' label={'Report for Driver'} />,
      key: 'driverName',
      render: ({ ...props }: ReportDTO) => {
        return <div>{props?.driverName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='tripId' label={'Trip ID'} />,
      key: 'tripId',
      render: ({ ...props }: ReportDTO) => {
        return <div>{props?.tripId}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
      key: 'status',
      render: ({ ...props }: ReportDTO) => {
        return (
          <div className='capitalize'>
            {props?.status === 'accept' ? (
              <Tag color='success' className='w-20 text-center'>
                {props?.status}
              </Tag>
            ) : props?.status === 'waiting' ? (
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
      title: () => <TableHeaderCell key='action' label={''} />,
      key: 'action',
      render: ({ ...props }: ReportDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton label={'Xem chi tiết'} onClick={() => setIsShownDetailsModal(props?.id)} />
          </div>
          {isShownDetailsModal === props?.id && (
            <ReportDetailModal
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
