import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DriverPackageSubscription from './driver-package-subscription';
import DriverPackageSubscriptionDetail from './driver-package-subscription-detail';
import DriverPackageSubscriptionUpdate from './driver-package-subscription-update';
import DriverPackageSubscriptionDeleteDialog from './driver-package-subscription-delete-dialog';

const DriverPackageSubscriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DriverPackageSubscription />} />
    <Route path="new" element={<DriverPackageSubscriptionUpdate />} />
    <Route path=":id">
      <Route index element={<DriverPackageSubscriptionDetail />} />
      <Route path="edit" element={<DriverPackageSubscriptionUpdate />} />
      <Route path="delete" element={<DriverPackageSubscriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DriverPackageSubscriptionRoutes;
