import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feedback.reducer';

export const FeedbackDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feedbackEntity = useAppSelector(state => state.feedback.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feedbackDetailsHeading">
          <Translate contentKey="sTripBeApp.feedback.detail.title">Feedback</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.id}</dd>
          <dt>
            <span id="feedbackID">
              <Translate contentKey="sTripBeApp.feedback.feedbackID">Feedback ID</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackID}</dd>
          <dt>
            <span id="feedbackType">
              <Translate contentKey="sTripBeApp.feedback.feedbackType">Feedback Type</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackType}</dd>
          <dt>
            <span id="feedbackDescription">
              <Translate contentKey="sTripBeApp.feedback.feedbackDescription">Feedback Description</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackDescription}</dd>
          <dt>
            <span id="feedbackRating">
              <Translate contentKey="sTripBeApp.feedback.feedbackRating">Feedback Rating</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackRating}</dd>
          <dt>
            <span id="feedbackStatus">
              <Translate contentKey="sTripBeApp.feedback.feedbackStatus">Feedback Status</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.feedbackStatus}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.feedback.trip">Trip</Translate>
          </dt>
          <dd>{feedbackEntity.trip ? feedbackEntity.trip.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.feedback.driver">Driver</Translate>
          </dt>
          <dd>{feedbackEntity.driver ? feedbackEntity.driver.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.feedback.user">User</Translate>
          </dt>
          <dd>{feedbackEntity.user ? feedbackEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/feedback" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feedback/${feedbackEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeedbackDetail;
