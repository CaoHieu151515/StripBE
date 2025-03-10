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
import { ITripStopLocation } from 'app/shared/model/trip-stop-location.model';
import { getEntity, updateEntity, createEntity, reset } from './trip-stop-location.reducer';

export const TripStopLocationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const tripStopLocationEntity = useAppSelector(state => state.tripStopLocation.entity);
  const loading = useAppSelector(state => state.tripStopLocation.loading);
  const updating = useAppSelector(state => state.tripStopLocation.updating);
  const updateSuccess = useAppSelector(state => state.tripStopLocation.updateSuccess);

  const handleClose = () => {
    navigate('/trip-stop-location' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrips({}));
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
    values.stopLocaTime = convertDateTimeToServer(values.stopLocaTime);

    const entity = {
      ...tripStopLocationEntity,
      ...values,
      trip: trips.find(it => it.id.toString() === values.trip?.toString()),
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
          stopLocaTime: displayDefaultDateTime(),
        }
      : {
          ...tripStopLocationEntity,
          stopLocaTime: convertDateTimeFromServer(tripStopLocationEntity.stopLocaTime),
          trip: tripStopLocationEntity?.trip?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.tripStopLocation.home.createOrEditLabel" data-cy="TripStopLocationCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.tripStopLocation.home.createOrEditLabel">Create or edit a TripStopLocation</Translate>
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
                  id="trip-stop-location-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.tripStopLocation.stopLocaID')}
                id="trip-stop-location-stopLocaID"
                name="stopLocaID"
                data-cy="stopLocaID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.tripStopLocation.stopLoca')}
                id="trip-stop-location-stopLoca"
                name="stopLoca"
                data-cy="stopLoca"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.tripStopLocation.stopLocaTime')}
                id="trip-stop-location-stopLocaTime"
                name="stopLocaTime"
                data-cy="stopLocaTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.tripStopLocation.stopLocaStatus')}
                id="trip-stop-location-stopLocaStatus"
                name="stopLocaStatus"
                data-cy="stopLocaStatus"
                type="text"
              />
              <ValidatedField
                id="trip-stop-location-trip"
                name="trip"
                data-cy="trip"
                label={translate('sTripBeApp.tripStopLocation.trip')}
                type="select"
              >
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trip-stop-location" replace color="info">
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

export default TripStopLocationUpdate;
