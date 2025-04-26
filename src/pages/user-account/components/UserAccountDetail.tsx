import InfoItem from '@/components/common/InfoItem';
import { Divider, Image } from 'antd';
import { useMemo } from 'react';
import { useParams } from 'react-router-dom';
import { UserAccountData } from '../mocks/UserData';
import { useUserDetailData } from '@/data/services/api/user/useUserDetailData';
import { DATE_FORMAT } from '@/utils/constants/date.constant';
import dayjs from 'dayjs';

export const UserAccountDetail = () => {
  const { id } = useParams();
  const { userDetailData } = useUserDetailData(id);
  const dataAccount = useMemo(() => UserAccountData.find((item) => item.id === Number(id)), [id]);

  return (
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
      <div className='mt-4 grid grid-cols-3 gap-4'>
        <InfoItem
          label='Tên'
          value={`${userDetailData?.firstName ?? ''} ${userDetailData?.lastName ?? ''}`.trim() || '—'}
        />
        <InfoItem label='Số điện thoại' value={userDetailData?.phone || '—'} />
        <InfoItem label='Địa chỉ' value={userDetailData?.address || '—'} />
        <InfoItem label='Giới tính' value={userDetailData?.gender || '—'} />
        <InfoItem label='Ngày sinh' value={userDetailData?.dob ? dayjs(userDetailData.dob).format(DATE_FORMAT) : '—'} />
        <InfoItem label='Email' value={userDetailData?.email || '—'} />

        <InfoItem
          label='Giấy phép lái xe'
          value={
            <Image
              src={
                userDetailData?.driverLicenseUrl ??
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
      <Divider />

      <div className='space-y-4'>
        <h3 className='text-xl font-bold text-primary-500'>Danh sách phương tiện</h3>
        {userDetailData?.vehicles?.length ? (
          userDetailData.vehicles.map((v, idx) => (
            <>
              <Divider />
              <h4 className='mb-2 text-lg font-medium text-blue-500'>Phương tiện {idx + 1}</h4>
              <figure className='relative h-48 w-48 rounded-xl border-[5px] border-white'>
                <img
                  src={
                    v?.vehicleImageUrl ??
                    'https://vinfast-auto-vn.net/wp-content/uploads/2022/08/VinFast-VF-8-mau-Xanh-Luc.png'
                  }
                  alt={'avatar'}
                  className='h-full w-full rounded-xl border-[5px] border-white object-contain'
                />
              </figure>
              <div key={v.vehicleId || idx} className='mt-4 grid grid-cols-3 gap-4'>
                <InfoItem label='Loại xe' value={v.vehicleType || '—'} />

                <InfoItem label='Biển số xe' value={v.vehicleNumber || '—'} />

                <InfoItem label='Số ghế' value={v.numberOfSeats != null ? `${v.numberOfSeats}` : '—'} />

                <InfoItem label='Màu sắc' value={v.vehicleColor || '—'} />

                <InfoItem label='Hãng xe' value={v.vehicleBrand || '—'} />

                <InfoItem label='Trạng thái' value={v.status || '—'} />

                <InfoItem
                  label='Giấy Tờ Xe'
                  value={
                    <Image
                      src={
                        v?.carRegistrationUrl ??
                        'https://dmv.ny.gov/sites/default/files/styles/wysiwyg/public/images/2022-01/reg_sample-340x300.png?itok=HZLA63ka'
                      }
                      alt='vehicle'
                      width={200}
                      height={200}
                      className='object-contain'
                    />
                  }
                />

                <InfoItem
                  label='Bảo Hiểm Xe'
                  value={
                    <Image
                      src={
                        v?.carInsuranceUrl ?? 'https://www.policybazaar.com/pblife/assets/images/pb_life_1650972275.jpg'
                      }
                      alt='vehicle'
                      width={200}
                      height={200}
                      className='object-contain'
                    />
                  }
                />

                <InfoItem
                  label='Đăng Ký Xe'
                  value={
                    <Image
                      src={
                        v.vehicleInspectionCertificateUrl ??
                        'https://tnclerks.zendesk.com/hc/article_attachments/4409967522708/Combined_month_and_year_decal.PNG'
                      }
                      alt='vehicle'
                      width={200}
                      height={200}
                      className='object-contain'
                    />
                  }
                />
              </div>
            </>
          ))
        ) : (
          <p className='ml-4 text-gray-500'>Chưa có phương tiện nào</p>
        )}
      </div>
    </div>
  );
};
