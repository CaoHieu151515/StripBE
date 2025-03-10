import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TripStopLocation from './trip-stop-location';
import TripStopLocationDetail from './trip-stop-location-detail';
import TripStopLocationUpdate from './trip-stop-location-update';
import TripStopLocationDeleteDialog from './trip-stop-location-delete-dialog';

const TripStopLocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TripStopLocation />} />
    <Route path="new" element={<TripStopLocationUpdate />} />
    <Route path=":id">
      <Route index element={<TripStopLocationDetail />} />
      <Route path="edit" element={<TripStopLocationUpdate />} />
      <Route path="delete" element={<TripStopLocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TripStopLocationRoutes;
