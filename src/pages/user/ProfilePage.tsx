import InfoItem from '@/components/common/InfoItem';
import { RootState } from '@/data';
import { GlobalState } from '@/data/global/global.slice';
import { useAppSelector } from '@/hooks/reduxHook';
import DefaultContainer from '@/layouts/DefaultContainer';
import { DATE_FORMAT } from '@/utils/constants/date.constant';
import dayjs from 'dayjs';

export default function ProfilePage() {
  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);
  console.log('userInfo', userInfo?.userImageUrl);
  return (
    <DefaultContainer title='Profile Page'>
      <div className='bg-white'>
        <div className='container mx-auto py-8'>
          <div className='grid grid-cols-4 gap-6 px-4 sm:grid-cols-12'>
            <div className='col-span-4'>
              <div className='rounded-lg bg-white p-2 shadow'>
                <div className='flex flex-col items-center'>
                  <img src={userInfo?.userImageUrl} className='mb-4 h-32 w-32 shrink-0 rounded-full bg-gray-300' />
                  <h1 className='text-xl font-bold'>{userInfo?.firstName + ' ' + userInfo?.lastName}</h1>
                  <p className='text-gray-700'>{userInfo?.email}</p>
                  <div className='mt-6 flex flex-wrap justify-center gap-4'>
                    <a href='#' className='rounded bg-blue-500 px-4 py-2 text-white hover:bg-blue-600'>
                      Contact
                    </a>
                    <a href='#' className='rounded bg-gray-300 px-4 py-2 text-gray-700 hover:bg-gray-400'>
                      Resume
                    </a>
                  </div>
                </div>
              </div>
            </div>
            <div className='col-span-4 sm:col-span-8'>
              <div className='grid grid-cols-2 gap-6 rounded-lg bg-white p-6 shadow'>
                <InfoItem label='Name' value={userInfo?.firstName + ' ' + userInfo?.lastName} />
                <InfoItem label='Phone' value={userInfo?.phone} />
                <InfoItem label='Address' value={userInfo?.address} />
                <InfoItem label='Date of Birth' value={dayjs(userInfo?.dob).format(DATE_FORMAT)} />
                <InfoItem label='Gender' value={userInfo?.gender} />
                <InfoItem
                  label='Role'
                  value={
                    userInfo?.roles
                      .map((role) => {
                        if (role === 'ROLE_ADMIN') {
                          return 'Admin';
                        }
                        if (role === 'ROLE_USER') {
                          return 'User';
                        }
                        if (role === 'ROLE_MODERATOR') {
                          return 'Moderator';
                        }
                        return role;
                      })
                      ?.join(' | ') ?? ''
                  }
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </DefaultContainer>
  );
}
