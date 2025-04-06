import { MY_ROUTE } from '@/helpers/router/route.constant';
import MainLayout from '@/layouts/MainLayout';
import ProtectedRoute from '@/layouts/ProtectedRoute';
import PublicRoute from '@/layouts/PublicRoute';
import CensorDriverRequestPage from '@/pages/censor-driver/CensorDriverRequestPage';
import CensorVehiclePage from '@/pages/censor-vehicle/CensorVehiclePage';
import ChangePasswordPage from '@/pages/change-password/ChangePasswordPage';
import DashboardPage from '@/pages/dashboard/DashboardPage';
import FeedbackPage from '@/pages/feedback/FeedbackPage';
import ForgotPasswordPage from '@/pages/forgot-password/ForgotPasswordPage';
import LoginAdminPage from '@/pages/login/LoginAdminPage';
import PackagePage from '@/pages/package/PackagePage';
import RatingPage from '@/pages/rating/RatingPage';
import ReportPage from '@/pages/report/ReportPage';
import TransactionHistoryPage from '@/pages/transaction-history/TransactionHistoryPage';
import TripDetailPage from '@/pages/trip/TripDetailPage';
import TripPage from '@/pages/trip/TripPage';
import { UserAccountDetailPage } from '@/pages/user-account/UserAccountDetailPage';
import UserAccountPage from '@/pages/user-account/UserAccountPage';
import ProfilePage from '@/pages/user/ProfilePage';
import WalletPage from '@/pages/wallet/WalletPage';

import { createBrowserRouter, Navigate } from 'react-router-dom';

const router = createBrowserRouter(
  [
    {
      element: <ProtectedRoute />,
      children: [
        { path: MY_ROUTE.HOME, element: <Navigate to={MY_ROUTE.DASHBOARD.self} replace /> },
        {
          path: MY_ROUTE.HOME,
          element: <MainLayout />,
          children: [
            {
              path: MY_ROUTE.DASHBOARD.self,
              element: <DashboardPage />,
            },
            {
              path: MY_ROUTE.USER.self,
              children: [
                {
                  path: MY_ROUTE.USER.VIEW_USER_ACCOUNT.self,
                  element: <UserAccountPage />,
                },
                {
                  path: MY_ROUTE.USER.VIEW_USER_ACCOUNT.detail(':id'),
                  element: <UserAccountDetailPage />,
                },
                {
                  path: MY_ROUTE.USER.CENSOR_DRIVER_REQUEST.self,
                  element: <CensorDriverRequestPage />,
                },
              ],
            },
            {
              path: MY_ROUTE.TRIP.self,
              element: <TripPage />,
            },
            {
              path: MY_ROUTE.TRIP.detail(':id'),
              element: <TripDetailPage />,
            },
            {
              path: MY_ROUTE.FEEDBACK.self,
              element: <FeedbackPage />,
            },
            {
              path: MY_ROUTE.PACKAGE.self,
              element: <PackagePage />,
            },
            {
              path: MY_ROUTE.WALLET.self,
              element: <WalletPage />,
            },
            {
              path: MY_ROUTE.REPORT.self,
              element: <ReportPage />,
            },
            {
              path: MY_ROUTE.TRANSACTION_HISTORY.self,
              element: <TransactionHistoryPage />,
            },
            {
              path: MY_ROUTE.CENSOR_VEHICLE.self,
              element: <CensorVehiclePage />,
            },
            {
              path: MY_ROUTE.RATE_DRIVER.self,
              element: <RatingPage />,
            },
            {
              path: MY_ROUTE.CHANGE_PASSWORD,
              element: <ChangePasswordPage />,
            },
            {
              path: MY_ROUTE.PROFILE,
              element: <ProfilePage />,
            },
          ],
        },
      ],
    },
    {
      element: <PublicRoute />,
      children: [
        {
          path: MY_ROUTE.HOME,
          children: [
            {
              path: MY_ROUTE.LOGIN,
              element: <LoginAdminPage />,
            },
            {
              path: MY_ROUTE.FORGOT_PASSWORD.self,
              element: <ForgotPasswordPage />,
            },
          ],
        },
      ],
    },
  ],
  {
    future: {
      v7_relativeSplatPath: true,
      v7_fetcherPersist: true,
      v7_normalizeFormMethod: true,
      v7_partialHydration: true,
      v7_skipActionErrorRevalidation: true,
    },
  },
);

export default router;
