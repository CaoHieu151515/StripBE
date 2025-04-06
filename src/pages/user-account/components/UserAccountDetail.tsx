import InfoItem from '@/components/common/InfoItem';
import { Divider, Image } from 'antd';
import { useMemo } from 'react';
import { useParams } from 'react-router-dom';
import { UserAccountData } from '../mocks/UserData';

export const UserAccountDetail = () => {
  const { id } = useParams();
  const dataAccount = useMemo(() => UserAccountData.find((item) => item.id === Number(id)), [id]);

  return (
    <div className='space-y-4'>
      <div className='mb-4 grid gap-4 sm:grid-cols-2 sm:gap-8 lg:gap-16'>
        <div className='space-y-4'>
          <div className='flex space-x-4'>
            <figure className='relative h-24 w-24 rounded-xl border-[5px] border-white'>
              <img
                src={`https://ui-avatars.com/api/?name=${dataAccount?.name}&background=6366f1&color=fff&size=24`}
                alt={'avatar'}
                className='h-full w-full rounded-xl border-[5px] border-white object-contain'
              />
            </figure>
            <div>
              <span className='mb-2 inline-block rounded bg-primary-100 px-2.5 py-0.5 text-xs font-medium text-primary-800'>
                Gold Package
              </span>
              <h2 className='flex items-center text-xl font-bold leading-none text-gray-900 sm:text-2xl'>
                {dataAccount?.name}
              </h2>
            </div>
          </div>
          <InfoItem label='Gender' value={dataAccount?.gender.toLocaleUpperCase()} />
          <InfoItem label='Phone Number' value={dataAccount?.phone} />
          <InfoItem label='Email' value={dataAccount?.email} />
        </div>
        <div className='space-y-4'>
          <InfoItem label='Date of Birth' value={'19/02/1989'} />
          <InfoItem label='Home Address' value={'FPT Software, Ho Chi Minh City'} />
          <InfoItem label='Role' value={dataAccount?.role.toLocaleUpperCase()} />
          <InfoItem label='Total trip attended' value={'10'} />
          <InfoItem label='Number of trips created' value={'20'} />
        </div>
      </div>
      <Divider />
      <div className='space-y-4'>
        <h3 className='text-xl font-bold text-primary-500'>List Vehicle</h3>
        <div>
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
                <h3 className='text-lg font-bold'>Vehicle Information</h3>
                <div className='mt-4 grid grid-cols-2 gap-4'>
                  <InfoItem label='Vehicle Type' value={'Car'} />
                  <InfoItem label='Vehicle Name' value={'VinFast VF8'} />
                  <InfoItem label='Vehicle Brand' value={'VinFast'} />
                  <InfoItem label='Vehicle Model' value={'VF8'} />
                  <InfoItem label='Vehicle Color' value={'Blue'} />
                  <InfoItem label='Machine Number' value={'1234567890'} />
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
          <Divider />
          <div>
            <div className='flex w-full gap-4'>
              <div className='w-full'>
                <h3 className='text-lg font-bold'>Vehicle Information</h3>
                <div className='mt-4 grid grid-cols-2 gap-4'>
                  <InfoItem label='Vehicle Type' value={'Car'} />
                  <InfoItem label='Vehicle Name' value={'Volvo V70'} />
                  <InfoItem label='Vehicle Brand' value={'Volvo'} />
                  <InfoItem label='Vehicle Model' value={'V70'} />
                  <InfoItem label='Vehicle Color' value={'Gray'} />
                  <InfoItem label='Machine Number' value={'1234567890'} />
                </div>
              </div>
              <Image
                src={'https://images.dealer.com/ddc/vehicles/2021/Volvo/XC90/SUV/perspective/front-left/2021_76.png'}
                alt='vehicle'
                width={600}
                height={350}
                className='object-contain'
              />
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
    </div>
  );
};
