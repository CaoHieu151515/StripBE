import InfoItem from '@/components/common/InfoItem';
import CustomModal from '@/components/modal/CustomModal';
import { RatingDTO } from '@/data/rating/dto/rating.dto';

type RatingDetailModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
  data: RatingDTO;
};

export default function RatingDetailModal({ open, setOpen, data }: RatingDetailModalProps) {
  return (
    <CustomModal title='Rating Detail' open={open} setOpen={setOpen} className='!w-[520px]'>
      <div className='space-y-5'>
        <div className='grid grid-cols-2 gap-4'>
          <InfoItem label='Driver Name' value={data?.driverName} />
          <InfoItem label='Rating' value={data?.rating} />
        </div>
        <InfoItem label='Content' value={data?.content} />
      </div>
    </CustomModal>
  );
}
