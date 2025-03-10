import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrip } from 'app/shared/model/trip.model';
import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { IDriver } from 'app/shared/model/driver.model';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IRating } from 'app/shared/model/rating.model';
import { RatingType } from 'app/shared/model/enumerations/rating-type.model';
import { getEntity, updateEntity, createEntity, reset } from './rating.reducer';

export const RatingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const drivers = useAppSelector(state => state.driver.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const ratingEntity = useAppSelector(state => state.rating.entity);
  const loading = useAppSelector(state => state.rating.loading);
  const updating = useAppSelector(state => state.rating.updating);
  const updateSuccess = useAppSelector(state => state.rating.updateSuccess);
  const ratingTypeValues = Object.keys(RatingType);

  const handleClose = () => {
    navigate('/rating');
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

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.ratingTime = convertDateTimeToServer(values.ratingTime);
    if (values.ratingDriver !== undefined && typeof values.ratingDriver !== 'number') {
      values.ratingDriver = Number(values.ratingDriver);
    }

    const entity = {
      ...ratingEntity,
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
      ? {
          ratingTime: displayDefaultDateTime(),
        }
      : {
          ratingType: 'DRIVER_TO_USER',
          ...ratingEntity,
          ratingTime: convertDateTimeFromServer(ratingEntity.ratingTime),
          trip: ratingEntity?.trip?.id,
          driver: ratingEntity?.driver?.id,
          user: ratingEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.rating.home.createOrEditLabel" data-cy="RatingCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.rating.home.createOrEditLabel">Create or edit a Rating</Translate>
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
                  id="rating-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.rating.ratingID')}
                id="rating-ratingID"
                name="ratingID"
                data-cy="ratingID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.rating.ratingTime')}
                id="rating-ratingTime"
                name="ratingTime"
                data-cy="ratingTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.rating.ratingDriver')}
                id="rating-ratingDriver"
                name="ratingDriver"
                data-cy="ratingDriver"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.rating.ratingType')}
                id="rating-ratingType"
                name="ratingType"
                data-cy="ratingType"
                type="select"
              >
                {ratingTypeValues.map(ratingType => (
                  <option value={ratingType} key={ratingType}>
                    {translate('sTripBeApp.RatingType.' + ratingType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="rating-trip" name="trip" data-cy="trip" label={translate('sTripBeApp.rating.trip')} type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="rating-driver" name="driver" data-cy="driver" label={translate('sTripBeApp.rating.driver')} type="select">
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="rating-user" name="user" data-cy="user" label={translate('sTripBeApp.rating.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/rating" replace color="info">
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

export default RatingUpdate;
