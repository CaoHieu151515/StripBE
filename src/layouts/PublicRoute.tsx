import { RootState } from '@/data';
import { AuthState } from '@/data/auth/auth.slice';
import { MY_ROUTE } from '@/helpers/router/route.constant';
import { useAppSelector } from '@/hooks/reduxHook';
import { safeString } from '@/utils/parser.helper';
import { Navigate, Outlet } from 'react-router-dom';
import FullScreenLayout from './FullScreenLayout';

export default function PublicRoute() {
  const { status }: AuthState = useAppSelector((state: RootState) => state.auth);

  return status === 'in-app' ? (
    <Navigate to={safeString(MY_ROUTE.DASHBOARD.self)} />
  ) : (
    <FullScreenLayout>
      <Outlet />
    </FullScreenLayout>
  );
}
