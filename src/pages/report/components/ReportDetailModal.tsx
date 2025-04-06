import InfoItem from '@/components/common/InfoItem';
import CustomModal from '@/components/modal/CustomModal';
import { ReportDTO } from '@/data/report/dto/report.dto';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import dayjs from 'dayjs';

type ReportDetailModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
  data: ReportDTO;
};

export default function ReportDetailModal({ open, setOpen, data }: ReportDetailModalProps) {
  return (
    <CustomModal title='Report Detail' open={open} setOpen={setOpen} className='!w-[520px]'>
      <div className='grid grid-cols-2 gap-4'>
        <InfoItem label='Name' value={data?.name} />
        <InfoItem label='Trip ID' value={data?.tripId} />
        <InfoItem label='Trip Name' value={data?.tripName || 'Trip Name'} />
        <InfoItem label='Feedback Date' value={dayjs(data?.reportDate).format(DATE_FORMAT_DOT)} />
        <InfoItem label='Feedback Time' value={dayjs(data?.reportDate).format(TIME_24H_FORMAT)} />
        <InfoItem label='Feedback for Driver' value={data?.driverName} />
        <InfoItem label='Content' value={data?.reportContent} />
      </div>
    </CustomModal>
  );
}
