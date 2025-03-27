import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { PassengerType } from 'app/shared/model/enumerations/passenger-type.model';
import { PassengerStatus } from 'app/shared/model/enumerations/passenger-status.model';
import { createEntity, getEntity, reset, updateEntity } from './request-trip.reducer';

export const RequestTripUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const requestTripEntity = useAppSelector(state => state.requestTrip.entity);
  const loading = useAppSelector(state => state.requestTrip.loading);
  const updating = useAppSelector(state => state.requestTrip.updating);
  const updateSuccess = useAppSelector(state => state.requestTrip.updateSuccess);
  const passengerTypeValues = Object.keys(PassengerType);
  const passengerStatusValues = Object.keys(PassengerStatus);

  const handleClose = () => {
    navigate(`/request-trip${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTrips({}));
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
    if (values.amountApproveFee !== undefined && typeof values.amountApproveFee !== 'number') {
      values.amountApproveFee = Number(values.amountApproveFee);
    }
    if (values.numberofSeats !== undefined && typeof values.numberofSeats !== 'number') {
      values.numberofSeats = Number(values.numberofSeats);
    }
    values.pickUpTime = convertDateTimeToServer(values.pickUpTime);
    values.endTime = convertDateTimeToServer(values.endTime);
    values.checkInTime = convertDateTimeToServer(values.checkInTime);
    values.checkOutTIme = convertDateTimeToServer(values.checkOutTIme);
    values.appliedAt = convertDateTimeToServer(values.appliedAt);

    const entity = {
      ...requestTripEntity,
      ...values,
      trip: trips.find(it => it.id.toString() === values.trip?.toString()),
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
          pickUpTime: displayDefaultDateTime(),
          endTime: displayDefaultDateTime(),
          checkInTime: displayDefaultDateTime(),
          checkOutTIme: displayDefaultDateTime(),
          appliedAt: displayDefaultDateTime(),
        }
      : {
          type: 'LUGGAGE',
          status: 'WAITING',
          ...requestTripEntity,
          pickUpTime: convertDateTimeFromServer(requestTripEntity.pickUpTime),
          endTime: convertDateTimeFromServer(requestTripEntity.endTime),
          checkInTime: convertDateTimeFromServer(requestTripEntity.checkInTime),
          checkOutTIme: convertDateTimeFromServer(requestTripEntity.checkOutTIme),
          appliedAt: convertDateTimeFromServer(requestTripEntity.appliedAt),
          trip: requestTripEntity?.trip?.id,
          user: requestTripEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.requestTrip.home.createOrEditLabel" data-cy="RequestTripCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.requestTrip.home.createOrEditLabel">Create or edit a RequestTrip</Translate>
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
                  id="request-trip-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.requestTripID')}
                id="request-trip-requestTripID"
                name="requestTripID"
                data-cy="requestTripID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.startLoca')}
                id="request-trip-startLoca"
                name="startLoca"
                data-cy="startLoca"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.endLoca')}
                id="request-trip-endLoca"
                name="endLoca"
                data-cy="endLoca"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.amountApproveFee')}
                id="request-trip-amountApproveFee"
                name="amountApproveFee"
                data-cy="amountApproveFee"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.numberofSeats')}
                id="request-trip-numberofSeats"
                name="numberofSeats"
                data-cy="numberofSeats"
                type="text"
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.requestTrip.luggageImg')}
                id="request-trip-luggageImg"
                name="luggageImg"
                data-cy="luggageImg"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.luggageDescription')}
                id="request-trip-luggageDescription"
                name="luggageDescription"
                data-cy="luggageDescription"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.type')}
                id="request-trip-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {passengerTypeValues.map(passengerType => (
                  <option value={passengerType} key={passengerType}>
                    {translate(`sTripBeApp.PassengerType.${passengerType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.status')}
                id="request-trip-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {passengerStatusValues.map(passengerStatus => (
                  <option value={passengerStatus} key={passengerStatus}>
                    {translate(`sTripBeApp.PassengerStatus.${passengerStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.pickUpTime')}
                id="request-trip-pickUpTime"
                name="pickUpTime"
                data-cy="pickUpTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.endTime')}
                id="request-trip-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.checkIn')}
                id="request-trip-checkIn"
                name="checkIn"
                data-cy="checkIn"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.checkInTime')}
                id="request-trip-checkInTime"
                name="checkInTime"
                data-cy="checkInTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.checkOut')}
                id="request-trip-checkOut"
                name="checkOut"
                data-cy="checkOut"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.checkOutTIme')}
                id="request-trip-checkOutTIme"
                name="checkOutTIme"
                data-cy="checkOutTIme"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.requestTrip.appliedAt')}
                id="request-trip-appliedAt"
                name="appliedAt"
                data-cy="appliedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="request-trip-trip"
                name="trip"
                data-cy="trip"
                label={translate('sTripBeApp.requestTrip.trip')}
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
              <ValidatedField
                id="request-trip-user"
                name="user"
                data-cy="user"
                label={translate('sTripBeApp.requestTrip.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/request-trip" replace color="info">
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

export default RequestTripUpdate;
