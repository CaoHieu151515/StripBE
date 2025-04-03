import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Driver from './driver';
import Vehicle from './vehicle';
import Trip from './trip';
import TripStopLocation from './trip-stop-location';
import Passenger from './passenger';
import Feedback from './feedback';
import Rating from './rating';
import PackageDriver from './package-driver';
import Payment from './payment';
import Notification from './notification';
import Report from './report';
import SystemWallet from './system-wallet';
import WalletTransaction from './wallet-transaction';
import SendingAplication from './sending-aplication';
import UserWallet from './user-wallet';
import UserDetail from './user-detail';
import RequestTrip from './request-trip';
import DriverPackageSubscription from './driver-package-subscription';
import WalletDeposit from './wallet-deposit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="driver/*" element={<Driver />} />
        <Route path="vehicle/*" element={<Vehicle />} />
        <Route path="trip/*" element={<Trip />} />
        <Route path="trip-stop-location/*" element={<TripStopLocation />} />
        <Route path="passenger/*" element={<Passenger />} />
        <Route path="feedback/*" element={<Feedback />} />
        <Route path="rating/*" element={<Rating />} />
        <Route path="package-driver/*" element={<PackageDriver />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="report/*" element={<Report />} />
        <Route path="system-wallet/*" element={<SystemWallet />} />
        <Route path="wallet-transaction/*" element={<WalletTransaction />} />
        <Route path="sending-aplication/*" element={<SendingAplication />} />
        <Route path="user-wallet/*" element={<UserWallet />} />
        <Route path="user-detail/*" element={<UserDetail />} />
        <Route path="request-trip/*" element={<RequestTrip />} />
        <Route path="driver-package-subscription/*" element={<DriverPackageSubscription />} />
        <Route path="wallet-deposit/*" element={<WalletDeposit />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
