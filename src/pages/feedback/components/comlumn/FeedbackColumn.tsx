import StarIcon from '@/components/icons/StarIcon';
import CustomTableActionsButton from '@/components/table/CustomTableActionsButton';
import { TableHeaderCell } from '@/components/table/TableHeaderCell';
import { FeedbackDTO } from '@/data/feedback/dto/feedback.dto';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import { FEEDBACK_STATUS } from '@/utils/enum/feedback/feedback-status.enum';
import { Tag } from 'antd';
import { ColumnsType } from 'antd/es/table';
import dayjs from 'dayjs';
import { useState } from 'react';
import FeedbackDetailModal from '../FeedbackDetailModal';

export const FeedbackColumn = (): ColumnsType<FeedbackDTO> => {
  const [isShownDetailsModal, setIsShownDetailsModal] = useState<string | null>(null);

  return [
    {
      title: () => <TableHeaderCell key='stt' label={'STT'} />,
      key: 'stt',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.index}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='tripID' label={'Mã phản hồi'} sortKey='tripID' />,
      key: 'tripID',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.tripId}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='name' label={'Tên người phản hồi'} sortKey='name' />,
      key: 'name',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.user.firstName + ' ' + props?.user.lastName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='feedbackDate' label={'Ngày giờ phản hồi'} sortKey='feedbackDate' />,
      key: 'feedbackDate',
      width: 180,
      render: ({ ...props }: FeedbackDTO) => {
        return (
          <div>
            {dayjs(props?.feedbackTime).format(DATE_FORMAT_DOT) +
              ' - ' +
              dayjs(props?.feedbackTime).format(TIME_24H_FORMAT)}
          </div>
        );
      },
    },
    {
      title: () => <TableHeaderCell key='driverName' label={'Phản hồi cho tài xế'} />,
      key: 'driverName',
      render: ({ ...props }: FeedbackDTO) => {
        return <div>{props?.driver.firstName + ' ' + props?.driver.lastName}</div>;
      },
    },
    {
      title: () => <TableHeaderCell key='rating' label={'Đánh giá'} />,
      key: 'rating',
      render: ({ ...props }: FeedbackDTO) => {
        return (
          <div className='flex gap-1'>
            {props?.feedbackRating}
            <StarIcon className='size-5 text-yellow-300' />
          </div>
        );
      },
    },
    {
      title: () => <TableHeaderCell key='status' label={'Trạng thái'} sortKey='status' />,
      key: 'status',
      render: ({ ...props }: FeedbackDTO) => {
        return (
          <div className='capitalize'>
            {props?.feedbackStatus === FEEDBACK_STATUS.WAITING ? (
              <Tag color='processing'>Chưa xử lý</Tag>
            ) : (
              <Tag color='success'>Đã xử lý</Tag>
            )}
          </div>
        );
      },
    },
    {
      title: () => <TableHeaderCell key='action' label={''} />,
      key: 'action',
      render: ({ ...props }: FeedbackDTO) => (
        <>
          <div className='flex w-full justify-start'>
            <CustomTableActionsButton
              label={'Xem chi tiết'}
              onClick={() => setIsShownDetailsModal(props?.feedbackID)}
            />
          </div>
          {isShownDetailsModal === props?.feedbackID && (
            <FeedbackDetailModal
              open={isShownDetailsModal === props?.feedbackID}
              setOpen={() => setIsShownDetailsModal(null)}
              data={props}
            />
          )}
        </>
      ),
    },
  ];
};
