import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Passenger from './passenger';
import PassengerDetail from './passenger-detail';
import PassengerUpdate from './passenger-update';
import PassengerDeleteDialog from './passenger-delete-dialog';

const PassengerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Passenger />} />
    <Route path="new" element={<PassengerUpdate />} />
    <Route path=":id">
      <Route index element={<PassengerDetail />} />
      <Route path="edit" element={<PassengerUpdate />} />
      <Route path="delete" element={<PassengerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PassengerRoutes;
