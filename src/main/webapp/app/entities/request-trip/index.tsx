import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RequestTrip from './request-trip';
import RequestTripDetail from './request-trip-detail';
import RequestTripUpdate from './request-trip-update';
import RequestTripDeleteDialog from './request-trip-delete-dialog';

const RequestTripRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RequestTrip />} />
    <Route path="new" element={<RequestTripUpdate />} />
    <Route path=":id">
      <Route index element={<RequestTripDetail />} />
      <Route path="edit" element={<RequestTripUpdate />} />
      <Route path="delete" element={<RequestTripDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RequestTripRoutes;
