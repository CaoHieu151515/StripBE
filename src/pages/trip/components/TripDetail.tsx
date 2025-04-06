import InfoItem from '@/components/common/InfoItem';
import { DATE_FORMAT_DOT, TIME_24H_FORMAT } from '@/utils/constants/date.constant';
import { TRIP_STATUS } from '@/utils/enum/trip/trip-status.enum';
import { Divider, Image, Tag } from 'antd';
import dayjs from 'dayjs';
import { useMemo } from 'react';
import { useParams } from 'react-router-dom';
import { TripData } from '../mocks/TripData';

export default function TripDetail() {
  const { id } = useParams();
  const tripInfor = useMemo(() => TripData.find((trip) => trip.id === Number(id)), [id]);
  return (
    <div className='space-y-5'>
      <div>
        <h3 className='text-xl font-bold text-primary-500'>Driver Information</h3>
        <Divider className='mt-1' />
        <div className='flex items-center gap-4'>
          <figure className='relative h-40 w-40 rounded-xl border-[5px] border-white'>
            <img
              src={`https://ui-avatars.com/api/?name=${tripInfor?.driver?.name}&background=6366f1&color=fff&size=24`}
              alt={'avatar'}
              className='h-full w-full rounded-xl border-[5px] border-white object-contain'
            />
          </figure>
          <div className='mt-4 grid grid-cols-2 gap-4'>
            <InfoItem label='Name' value={tripInfor?.driver?.name} />
            <InfoItem label='Phone' value={tripInfor?.driver?.phone} />
            <InfoItem label='Email' value={tripInfor?.driver?.email} />
            <InfoItem label='Package Buy' value={'Gold Package'} />
            <InfoItem label='Payment Method' value={'Bank Transfer'} />
            <InfoItem label='Gender' value={tripInfor?.driver?.gender} />
          </div>
        </div>
        <div className='mt-10 flex justify-start gap-10'>
          <InfoItem
            label='Driver License'
            value={
              <img
                src={'https://www.shutterstock.com/image-vector/driver-license-plastic-card-photo-260nw-2216933107.jpg'}
                alt='driver license'
                className='h-[200px] w-[300px] object-contain'
              />
            }
          />
          <InfoItem
            label='ID Card'
            value={
              <img
                src={'https://tayho.hanoi.gov.vn/Medias/1/35/2024/6/30/a5456b6e-5530-4ee1-9b65-99450249205d.jpg'}
                alt='id card'
                className='h-[200px] w-[300px] object-contain'
              />
            }
          />
        </div>
      </div>
      <div>
        <h3 className='text-xl font-bold text-primary-500'>Trip Information</h3>
        <Divider className='mt-1' />
        <div className='mt-4 grid grid-cols-2 gap-4'>
          <InfoItem label='Departure Location' value={tripInfor?.departureLocation} />
          <InfoItem label='Arrival Location' value={tripInfor?.arrivalLocation} />
          <InfoItem label='Departure Date' value={dayjs(tripInfor?.departureDate).format(DATE_FORMAT_DOT)} />
          <InfoItem label='Arrival Date' value={dayjs(tripInfor?.arrivalDate).format(DATE_FORMAT_DOT)} />
          <InfoItem label='Departure Time' value={dayjs(tripInfor?.departureTime).format(TIME_24H_FORMAT)} />
          <InfoItem label='Arrival Time' value={dayjs(tripInfor?.arrivalTime).format(TIME_24H_FORMAT)} />
          <InfoItem
            label='Status'
            value={
              <div className='capitalize'>
                {tripInfor?.status === TRIP_STATUS.ACCEPTED ? (
                  <Tag color='success' className='w-20 text-center'>
                    {tripInfor?.status}
                  </Tag>
                ) : tripInfor?.status === TRIP_STATUS.PENDING ? (
                  <Tag color='processing' className='w-20 text-center'>
                    {tripInfor?.status}
                  </Tag>
                ) : (
                  <Tag color='error' className='w-20 text-center'>
                    {tripInfor?.status}
                  </Tag>
                )}
              </div>
            }
          />
          <InfoItem label='Prive' value={tripInfor?.price.toLocaleString() + ' VND'} />
          <InfoItem label='Description' value={tripInfor?.description} />
        </div>
      </div>
      <div>
        <h3 className='text-xl font-bold text-primary-500'>Vehicle Information</h3>
        <Divider className='mt-1' />
        <div>
          <div className='flex w-full gap-4'>
            <Image
              src={'https://vinfast-auto-vn.net/wp-content/uploads/2022/08/VinFast-VF-8-mau-Xanh-Luc.png'}
              alt='vehicle'
              width={600}
              height={350}
              className='w-1/3 object-contain'
            />
            <div className='max-w-2/3 w-full'>
              <div className='mt-4 grid grid-cols-2 gap-4'>
                <InfoItem label='Vehicle Type' value={tripInfor?.vehicle?.vehicleType} />
                <InfoItem label='Vehicle Name' value={tripInfor?.vehicle?.vehicleName} />
                <InfoItem label='Vehicle Brand' value={tripInfor?.vehicle?.brand} />
                <InfoItem label='Number Insead' value={tripInfor?.vehicle?.numberInsead} />
              </div>
            </div>
          </div>
          <div className='mt-4 flex justify-between gap-5'>
            <InfoItem
              label='Vehicle Registration Certificate'
              value={
                <Image
                  src={
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
                  src={'https://www.policybazaar.com/pblife/assets/images/pb_life_1650972275.jpg'}
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
      </div>
    </div>
  );
}
