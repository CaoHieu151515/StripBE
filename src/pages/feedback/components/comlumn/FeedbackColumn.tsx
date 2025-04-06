import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { FeedbackDTO } from '@/data/feedback/dto/feedback.dto';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { useState } from 'react';
import FeedbackDetailModal from '../FeedbackDetailModal';

export const FeedbackColumn = (): ColumnsType<FeedbackDTO> => {
  const [isShownDetailsModal, setIsShownDetailsModal] = useState<number | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='id' label={'ID'} sortKey='id' />,
      key: 'id',
      width: 60,
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.id}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'Name'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.name}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='feedbackDate' label={'Feedback Date'} sortKey='feedbackDate' />,
      key: 'feedbackDate',
      width: 150,
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{dayjs(props?.feedbackDate).format(DATE_FORMAT_DOT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='feedbackTime' label={'Feedback Time'} sortKey='feedbackTime' />,
      key: 'feedbackTime',
      width: 150,
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{dayjs(props?.feedbackDate).format(TIME_24H_FORMAT)}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='driverName' label={'Feedback for Driver'} />,
      key: 'driverName',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.driverName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='tripId' label={'Trip ID'} />,
      key: 'tripId',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.tripId}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'Status'} sortKey='status' />,
      key: 'status',
      render: ({ ...props }: FeedbackDTO) => {
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
      title: () => <TableHeaderCell key='action' label={'Action'} />,
      key: 'action',
      render: ({ ...props }: FeedbackDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton label={'See Details'} onClick={() => setIsShownDetailsModal(props?.id)} />
          </div>
          {isShownDetailsModal === props?.id && (
            <FeedbackDetailModal
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
