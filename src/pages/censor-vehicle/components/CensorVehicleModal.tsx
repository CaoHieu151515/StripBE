import { CensorVehicleDTO } from '@/@types/dto/censorVehicleDTO';
import InfoItem from '@/components/common/InfoItem';
import CustomModal from '@/components/modal/CustomModal';
import { Image } from 'antd';

type CensorVehicleModalProps = {
  open: boolean;
  setOpen: (open: boolean) => void;
  data: CensorVehicleDTO;
};

export default function CensorVehicleModal({ open, setOpen, data }: CensorVehicleModalProps) {
  return (
    <CustomModal title='Censor Vehicle Detail' open={open} setOpen={setOpen} className='!w-[1020px]'>
      <div>
        <div className='flex w-full gap-4'>
          <Image
            src={
              data?.vehicle?.vehicleImageUrl ??
              'https://vinfast-auto-vn.net/wp-content/uploads/2022/08/VinFast-VF-8-mau-Xanh-Luc.png'
            }
            alt='vehicle'
            width={600}
            height={350}
            className='w-1/3 object-contain'
          />
          <div className='max-w-2/3 w-full'>
            <h3 className='text-lg font-bold'>Vehicle Information</h3>
            <div className='mt-4 grid grid-cols-2 gap-4'>
              <InfoItem label='Vehicle Type' value={data?.vehicle?.vehicleType} />
              {/* <InfoItem label='Vehicle Name' value={'VinFast VF8'} /> */}
              <InfoItem label='Vehicle Brand' value={data?.vehicle?.vehicleBrand} />
              {/* <InfoItem label='Vehicle Model' value={'VF8'} /> */}
              <InfoItem label='Vehicle Color' value={data?.vehicle?.vehicleColor} />
              <InfoItem label='Machine Number' value={data?.vehicle?.vehicleNumber} />
              <InfoItem label='Number of Seats' value={data?.vehicle?.numberOfSeats} />
              <InfoItem label='Status' value={data?.vehicle?.status} />
            </div>
          </div>
        </div>
        <div className='mt-4 flex justify-between gap-5'>
          <InfoItem
            label='Vehicle Registration Certificate'
            value={
              <Image
                src={
                  data?.vehicle?.vehicleInspectionCertificateUrl ??
                  'https://tnclerks.zendesk.com/hc/article_attachments/4409967522708/Combined_month_and_year_decal.PNG'
                }
                alt='vehicle'
                width={200}
                height={200}
                className='object-contain'
              />
            }
          />
          <InfoItem
            label='Car Insurance'
            value={
              <Image
                src={
                  data?.vehicle?.carInsuranceUrl ??
                  'https://www.policybazaar.com/pblife/assets/images/pb_life_1650972275.jpg'
                }
                alt='vehicle'
                width={200}
                height={200}
                className='object-contain'
              />
            }
          />
          <InfoItem
            label='Registration Certificate'
            value={
              <Image
                src={
                  data?.vehicle?.carregistrationUrl ??
                  'https://dmv.ny.gov/sites/default/files/styles/wysiwyg/public/images/2022-01/reg_sample-340x300.png?itok=HZLA63ka'
                }
                alt='vehicle'
                width={200}
                height={200}
                className='object-contain'
              />
            }
          />
        </div>
      </div>
    </CustomModal>
  );
}
