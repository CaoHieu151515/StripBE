import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SendingAplication from './sending-aplication';
import SendingAplicationDetail from './sending-aplication-detail';
import SendingAplicationUpdate from './sending-aplication-update';
import SendingAplicationDeleteDialog from './sending-aplication-delete-dialog';

const SendingAplicationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SendingAplication />} />
    <Route path="new" element={<SendingAplicationUpdate />} />
    <Route path=":id">
      <Route index element={<SendingAplicationDetail />} />
      <Route path="edit" element={<SendingAplicationUpdate />} />
      <Route path="delete" element={<SendingAplicationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SendingAplicationRoutes;
