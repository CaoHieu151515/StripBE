import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SystemTempWallet from './system-temp-wallet';
import SystemTempWalletDetail from './system-temp-wallet-detail';
import SystemTempWalletUpdate from './system-temp-wallet-update';
import SystemTempWalletDeleteDialog from './system-temp-wallet-delete-dialog';

const SystemTempWalletRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SystemTempWallet />} />
    <Route path="new" element={<SystemTempWalletUpdate />} />
    <Route path=":id">
      <Route index element={<SystemTempWalletDetail />} />
      <Route path="edit" element={<SystemTempWalletUpdate />} />
      <Route path="delete" element={<SystemTempWalletDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SystemTempWalletRoutes;
