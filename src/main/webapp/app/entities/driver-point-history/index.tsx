import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DriverPointHistory from './driver-point-history';
import DriverPointHistoryDetail from './driver-point-history-detail';
import DriverPointHistoryUpdate from './driver-point-history-update';
import DriverPointHistoryDeleteDialog from './driver-point-history-delete-dialog';

const DriverPointHistoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DriverPointHistory />} />
    <Route path="new" element={<DriverPointHistoryUpdate />} />
    <Route path=":id">
      <Route index element={<DriverPointHistoryDetail />} />
      <Route path="edit" element={<DriverPointHistoryUpdate />} />
      <Route path="delete" element={<DriverPointHistoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DriverPointHistoryRoutes;
