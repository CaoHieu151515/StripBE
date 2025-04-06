import LoadingPageBanner from '@/components/common/LoadingPageBanner';
import { RootState } from '@/data';
import { AuthState } from '@/data/auth/auth.slice';
import { setUserInfo } from '@/data/global/global.slice';
import { useGetUserInfoQuery } from '@/data/user/user.api';
import { DEFAULT_ROUTE } from '@/helpers/router/route.constant';
import { useAppDispatch, useAppSelector } from '@/hooks/reduxHook';
import { safeString } from '@/utils/parser.helper';
import { Empty } from 'antd';
import { useEffect } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import FullScreenLayout from './FullScreenLayout';

export default function ProtectedRoute() {
  const dispatch = useAppDispatch();
  const location = useLocation();
  const { status }: AuthState = useAppSelector((state: RootState) => state.auth);

  const {
    data: userInfoData,
    error: errorGetUserInfo,
    isLoading: isLoadingGetUserInfo,
    isFetching: isFetchingGetUserInfo,
  } = useGetUserInfoQuery(undefined, { skip: status !== 'in-app' });

  useEffect(() => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }, [location.pathname]);

  useEffect(() => {
    if (status !== 'in-app' || !userInfoData) return;
    dispatch(setUserInfo(userInfoData));
  }, [userInfoData, status, dispatch]);

  if (status === 'in-app') {
    if (isFetchingGetUserInfo || isLoadingGetUserInfo) return <LoadingPageBanner />;
    if (errorGetUserInfo) return <Empty />;

    return <Outlet />;
  }

  if (status === 'out-session')
    return (
      <FullScreenLayout>
        <Empty />
      </FullScreenLayout>
    );

  return <Navigate to={safeString(DEFAULT_ROUTE)} />;
}
