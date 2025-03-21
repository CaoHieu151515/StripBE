import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Feedback from './feedback';
import FeedbackDetail from './feedback-detail';
import FeedbackUpdate from './feedback-update';
import FeedbackDeleteDialog from './feedback-delete-dialog';

const FeedbackRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Feedback />} />
    <Route path="new" element={<FeedbackUpdate />} />
    <Route path=":id">
      <Route index element={<FeedbackDetail />} />
      <Route path="edit" element={<FeedbackUpdate />} />
      <Route path="delete" element={<FeedbackDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FeedbackRoutes;
