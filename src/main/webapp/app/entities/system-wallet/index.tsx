import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SystemWallet from './system-wallet';
import SystemWalletDetail from './system-wallet-detail';
import SystemWalletUpdate from './system-wallet-update';
import SystemWalletDeleteDialog from './system-wallet-delete-dialog';

const SystemWalletRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SystemWallet />} />
    <Route path="new" element={<SystemWalletUpdate />} />
    <Route path=":id">
      <Route index element={<SystemWalletDetail />} />
      <Route path="edit" element={<SystemWalletUpdate />} />
      <Route path="delete" element={<SystemWalletDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SystemWalletRoutes;
