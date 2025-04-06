import InfoItem from '@/components/common/InfoItem';
import CustomModal from '@/components/modal/CustomModal';
import { FeedbackDTO } from '@/data/feedback/dto/feedback.dto';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import dayjs from 'dayjs';

type FeedbackDetailModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
  data: FeedbackDTO;
};

export default function FeedbackDetailModal({ open, setOpen, data }: FeedbackDetailModalProps) {
  return (
    <CustomModal title='Feedback Detail' open={open} setOpen={setOpen} className='!w-[520px]'>
      <div className='grid grid-cols-2 gap-4'>
        <InfoItem label='Name' value={data?.name} />
        <InfoItem label='Trip ID' value={data?.tripId} />
        <InfoItem label='Trip Name' value={data?.tripName || 'Trip Name'} />
        <InfoItem label='Feedback Date' value={dayjs(data?.feedbackDate).format(DATE_FORMAT_DOT)} />
        <InfoItem label='Feedback Time' value={dayjs(data?.feedbackDate).format(TIME_24H_FORMAT)} />
        <InfoItem label='Feedback for Driver' value={data?.driverName} />
        <InfoItem label='Content' value={data?.feedbackContent} />
      </div>
    </CustomModal>
  );
}
