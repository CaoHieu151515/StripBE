import logo from '@/assets/images/logo.png';
import BellIcon from '@/components/icons/BellIcon';
import BracketRightIcon from '@/components/icons/BracketRightIcon';
import UserCircleIcon from '@/components/icons/UserCircleIcon';
import UserOutlineIcon from '@/components/icons/UserOutlineIcon';
import UserPenIcon from '@/components/icons/UserPenIcon';
import { RootState } from '@/data';
import { logoutThunk } from '@/data/auth/auth.thunk';
import { GlobalState } from '@/data/global/global.slice';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import { useAppDispatch, useAppSelector } from '@/hooks/reduxHook';
import { Popover } from 'antd';
import { useNavigate } from 'react-router-dom';

export default function NavBar() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { userInfo }: GlobalState = useAppSelector((state: RootState) => state.global);

  const contentPopover = (
    <div className='flex w-[150px] flex-col gap-2'>
      <button
        className='flex w-full items-center justify-between rounded-md px-1.5 py-2 hover:bg-gray-100'
        onClick={() => navigate(MY_ROUTE.PROFILE)}>
        <span className='text-xs font-semibold text-gray-900'>Profile</span>
        <UserOutlineIcon className='size-3 cursor-pointer text-gray-900 lg:size-4' />
      </button>
      <div className='h-px w-full bg-gray-200' />
      <button
        className='flex w-full items-center justify-between rounded-md px-1.5 py-2 hover:bg-gray-100'
        onClick={() => navigate(MY_ROUTE.CHANGE_PASSWORD)}>
        <span className='text-xs font-semibold text-gray-900'>Change Password</span>
        <UserPenIcon className='size-3 cursor-pointer text-gray-900 lg:size-4' />
      </button>
      <div className='h-px w-full bg-gray-200' />
      <button
        className='flex w-full items-center justify-between rounded-md px-1.5 py-2 hover:bg-gray-100'
        onClick={() => dispatch(logoutThunk())}>
        <span className='text-xs font-semibold text-red-500'>Log Out</span>
        <BracketRightIcon className='size-3 cursor-pointer text-red-500 lg:size-4' />
      </button>
    </div>
  );

  const contentPopoverBell = (
    <div className='flex w-[150px] flex-col gap-2'>
      <span className='text-xs font-semibold text-gray-900'>
        <span className='text-red-500'>1</span>
        notification
      </span>
    </div>
  );

  return (
    <nav className='default-shadow fixed left-0 top-0 z-[1000] flex h-navbar-sm w-full items-center bg-white lg:h-navbar'>
      <div className='mx-4 flex w-full items-center justify-between'>
        <div className='flex items-center gap-2'>
          <img src={logo} alt='logo' className='hidden h-10 w-auto object-contain lg:block' />
          <h3 className='text-center text-sm font-black tracking-wider text-primary-500 md:text-lg xl:text-xl'>
            S-TRIP
          </h3>
        </div>
        <div className='flex cursor-pointer items-center gap-5'>
          <Popover placement='bottom' content={contentPopover} className='flex cursor-pointer items-center gap-2'>
            {userInfo?.userImageUrl ? (
              <img
                src={userInfo?.userImageUrl}
                alt='User Image'
                className='h-7 w-7 rounded-full border border-gray-200 object-cover'
              />
            ) : (
              <UserCircleIcon />
            )}
            <p className='font-bold text-gray-900'>{userInfo?.firstName[0] + '. ' + userInfo?.lastName}</p>
          </Popover>
          <Popover placement='bottom' content={contentPopoverBell} className='flex cursor-pointer items-center gap-2'>
            <div className='relative'>
              <BellIcon className='size-5 cursor-pointer text-gray-900 lg:size-6' />
            </div>
          </Popover>
        </div>
      </div>
    </nav>
  );
}
