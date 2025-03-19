import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { FeedbackType } from 'app/shared/model/enumerations/feedback-type.model';
import { FeedbackStatus } from 'app/shared/model/enumerations/feedback-status.model';
import { createEntity, getEntity, reset, updateEntity } from './feedback.reducer';

export const FeedbackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const drivers = useAppSelector(state => state.driver.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const feedbackEntity = useAppSelector(state => state.feedback.entity);
  const loading = useAppSelector(state => state.feedback.loading);
  const updating = useAppSelector(state => state.feedback.updating);
  const updateSuccess = useAppSelector(state => state.feedback.updateSuccess);
  const feedbackTypeValues = Object.keys(FeedbackType);
  const feedbackStatusValues = Object.keys(FeedbackStatus);

  const handleClose = () => {
    navigate(`/feedback${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrips({}));
    dispatch(getDrivers({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.feedbackRating !== undefined && typeof values.feedbackRating !== 'number') {
      values.feedbackRating = Number(values.feedbackRating);
    }

    const entity = {
      ...feedbackEntity,
      ...values,
      trip: trips.find(it => it.id.toString() === values.trip?.toString()),
      driver: drivers.find(it => it.id.toString() === values.driver?.toString()),
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          feedbackType: 'DRIVER_TO_USER',
          feedbackStatus: 'WAITING',
          ...feedbackEntity,
          trip: feedbackEntity?.trip?.id,
          driver: feedbackEntity?.driver?.id,
          user: feedbackEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.feedback.home.createOrEditLabel" data-cy="FeedbackCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.feedback.home.createOrEditLabel">Create or edit a Feedback</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="feedback-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.feedback.feedbackID')}
                id="feedback-feedbackID"
                name="feedbackID"
                data-cy="feedbackID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.feedback.feedbackType')}
                id="feedback-feedbackType"
                name="feedbackType"
                data-cy="feedbackType"
                type="select"
              >
                {feedbackTypeValues.map(feedbackType => (
                  <option value={feedbackType} key={feedbackType}>
                    {translate(`sTripBeApp.FeedbackType.${feedbackType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.feedback.feedbackDescription')}
                id="feedback-feedbackDescription"
                name="feedbackDescription"
                data-cy="feedbackDescription"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.feedback.feedbackRating')}
                id="feedback-feedbackRating"
                name="feedbackRating"
                data-cy="feedbackRating"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.feedback.feedbackStatus')}
                id="feedback-feedbackStatus"
                name="feedbackStatus"
                data-cy="feedbackStatus"
                type="select"
              >
                {feedbackStatusValues.map(feedbackStatus => (
                  <option value={feedbackStatus} key={feedbackStatus}>
                    {translate(`sTripBeApp.FeedbackStatus.${feedbackStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="feedback-trip" name="trip" data-cy="trip" label={translate('sTripBeApp.feedback.trip')} type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="feedback-driver"
                name="driver"
                data-cy="driver"
                label={translate('sTripBeApp.feedback.driver')}
                type="select"
              >
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="feedback-user" name="user" data-cy="user" label={translate('sTripBeApp.feedback.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedback" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default FeedbackUpdate;
