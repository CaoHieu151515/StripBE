import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserDetail from './user-detail';
import UserDetailDetail from './user-detail-detail';
import UserDetailUpdate from './user-detail-update';
import UserDetailDeleteDialog from './user-detail-delete-dialog';

const UserDetailRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserDetail />} />
    <Route path="new" element={<UserDetailUpdate />} />
    <Route path=":id">
      <Route index element={<UserDetailDetail />} />
      <Route path="edit" element={<UserDetailUpdate />} />
      <Route path="delete" element={<UserDetailDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserDetailRoutes;
