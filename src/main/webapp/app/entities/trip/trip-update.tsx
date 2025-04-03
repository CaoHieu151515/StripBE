import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getVehicles } from 'app/entities/vehicle/vehicle.reducer';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { TripStatus } from 'app/shared/model/enumerations/trip-status.model';
import { createEntity, getEntity, reset, updateEntity } from './trip.reducer';

export const TripUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicles = useAppSelector(state => state.vehicle.entities);
  const drivers = useAppSelector(state => state.driver.entities);
  const tripEntity = useAppSelector(state => state.trip.entity);
  const loading = useAppSelector(state => state.trip.loading);
  const updating = useAppSelector(state => state.trip.updating);
  const updateSuccess = useAppSelector(state => state.trip.updateSuccess);
  const tripStatusValues = Object.keys(TripStatus);

  const handleClose = () => {
    navigate(`/trip${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVehicles({}));
    dispatch(getDrivers({}));
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
    if (values.pricePerSeat !== undefined && typeof values.pricePerSeat !== 'number') {
      values.pricePerSeat = Number(values.pricePerSeat);
    }
    if (values.maxSeat !== undefined && typeof values.maxSeat !== 'number') {
      values.maxSeat = Number(values.maxSeat);
    }
    if (values.totalTime !== undefined && typeof values.totalTime !== 'number') {
      values.totalTime = Number(values.totalTime);
    }
    if (values.totalDistance !== undefined && typeof values.totalDistance !== 'number') {
      values.totalDistance = Number(values.totalDistance);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.currentSeat !== undefined && typeof values.currentSeat !== 'number') {
      values.currentSeat = Number(values.currentSeat);
    }

    const entity = {
      ...tripEntity,
      ...values,
      vehicle: vehicles.find(it => it.id.toString() === values.vehicle?.toString()),
      driver: drivers.find(it => it.id.toString() === values.driver?.toString()),
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
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          tripStatus: 'UPCOMING',
          ...tripEntity,
          startDate: convertDateTimeFromServer(tripEntity.startDate),
          endDate: convertDateTimeFromServer(tripEntity.endDate),
          vehicle: tripEntity?.vehicle?.id,
          driver: tripEntity?.driver?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.trip.home.createOrEditLabel" data-cy="TripCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.trip.home.createOrEditLabel">Create or edit a Trip</Translate>
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
                  id="trip-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('sTripBeApp.trip.tripID')} id="trip-tripID" name="tripID" data-cy="tripID" type="text" />
              <ValidatedBlobField
                label={translate('sTripBeApp.trip.tripImg')}
                id="trip-tripImg"
                name="tripImg"
                data-cy="tripImg"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.pricePerSeat')}
                id="trip-pricePerSeat"
                name="pricePerSeat"
                data-cy="pricePerSeat"
                type="text"
              />
              <ValidatedField label={translate('sTripBeApp.trip.maxSeat')} id="trip-maxSeat" name="maxSeat" data-cy="maxSeat" type="text" />
              <ValidatedField
                label={translate('sTripBeApp.trip.totalTime')}
                id="trip-totalTime"
                name="totalTime"
                data-cy="totalTime"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.totalDistance')}
                id="trip-totalDistance"
                name="totalDistance"
                data-cy="totalDistance"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.startDate')}
                id="trip-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.endDate')}
                id="trip-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.currentSeat')}
                id="trip-currentSeat"
                name="currentSeat"
                data-cy="currentSeat"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.startLocation')}
                id="trip-startLocation"
                name="startLocation"
                data-cy="startLocation"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.endLocation')}
                id="trip-endLocation"
                name="endLocation"
                data-cy="endLocation"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.description')}
                id="trip-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.condition')}
                id="trip-condition"
                name="condition"
                data-cy="condition"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.cancelReason')}
                id="trip-cancelReason"
                name="cancelReason"
                data-cy="cancelReason"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.trip.tripStatus')}
                id="trip-tripStatus"
                name="tripStatus"
                data-cy="tripStatus"
                type="select"
              >
                {tripStatusValues.map(tripStatus => (
                  <option value={tripStatus} key={tripStatus}>
                    {translate(`sTripBeApp.TripStatus.${tripStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="trip-vehicle" name="vehicle" data-cy="vehicle" label={translate('sTripBeApp.trip.vehicle')} type="select">
                <option value="" key="0" />
                {vehicles
                  ? vehicles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="trip-driver" name="driver" data-cy="driver" label={translate('sTripBeApp.trip.driver')} type="select">
                <option value="" key="0" />
                {drivers
                  ? drivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/trip" replace color="info">
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

export default TripUpdate;
