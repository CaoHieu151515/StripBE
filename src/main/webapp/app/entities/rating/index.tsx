import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Rating from './rating';
import RatingDetail from './rating-detail';
import RatingUpdate from './rating-update';
import RatingDeleteDialog from './rating-delete-dialog';

const RatingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Rating />} />
    <Route path="new" element={<RatingUpdate />} />
    <Route path=":id">
      <Route index element={<RatingDetail />} />
      <Route path="edit" element={<RatingUpdate />} />
      <Route path="delete" element={<RatingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RatingRoutes;
