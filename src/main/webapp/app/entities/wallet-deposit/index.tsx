import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WalletDeposit from './wallet-deposit';
import WalletDepositDetail from './wallet-deposit-detail';
import WalletDepositUpdate from './wallet-deposit-update';
import WalletDepositDeleteDialog from './wallet-deposit-delete-dialog';

const WalletDepositRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WalletDeposit />} />
    <Route path="new" element={<WalletDepositUpdate />} />
    <Route path=":id">
      <Route index element={<WalletDepositDetail />} />
      <Route path="edit" element={<WalletDepositUpdate />} />
      <Route path="delete" element={<WalletDepositDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WalletDepositRoutes;
