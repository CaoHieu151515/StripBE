import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PackageDriver from './package-driver';
import PackageDriverDetail from './package-driver-detail';
import PackageDriverUpdate from './package-driver-update';
import PackageDriverDeleteDialog from './package-driver-delete-dialog';

const PackageDriverRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PackageDriver />} />
    <Route path="new" element={<PackageDriverUpdate />} />
    <Route path=":id">
      <Route index element={<PackageDriverDetail />} />
      <Route path="edit" element={<PackageDriverUpdate />} />
      <Route path="delete" element={<PackageDriverDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PackageDriverRoutes;
