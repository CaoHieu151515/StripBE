import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITrip } from 'app/shared/model/trip.model';
import { getEntities as getTrips } from 'app/entities/trip/trip.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPassenger } from 'app/shared/model/passenger.model';
import { PassengerType } from 'app/shared/model/enumerations/passenger-type.model';
import { PassengerStatus } from 'app/shared/model/enumerations/passenger-status.model';
import { getEntity, updateEntity, createEntity, reset } from './passenger.reducer';

export const PassengerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const trips = useAppSelector(state => state.trip.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const passengerEntity = useAppSelector(state => state.passenger.entity);
  const loading = useAppSelector(state => state.passenger.loading);
  const updating = useAppSelector(state => state.passenger.updating);
  const updateSuccess = useAppSelector(state => state.passenger.updateSuccess);
  const passengerTypeValues = Object.keys(PassengerType);
  const passengerStatusValues = Object.keys(PassengerStatus);

  const handleClose = () => {
    navigate('/passenger' + location.search);
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

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.amountApproveFee !== undefined && typeof values.amountApproveFee !== 'number') {
      values.amountApproveFee = Number(values.amountApproveFee);
    }
    values.pickUpTime = convertDateTimeToServer(values.pickUpTime);
    values.endTime = convertDateTimeToServer(values.endTime);
    values.checkInTime = convertDateTimeToServer(values.checkInTime);
    values.checkOutTIme = convertDateTimeToServer(values.checkOutTIme);
    values.appliedAt = convertDateTimeToServer(values.appliedAt);

    const entity = {
      ...passengerEntity,
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
          status: 'WATING',
          ...passengerEntity,
          pickUpTime: convertDateTimeFromServer(passengerEntity.pickUpTime),
          endTime: convertDateTimeFromServer(passengerEntity.endTime),
          checkInTime: convertDateTimeFromServer(passengerEntity.checkInTime),
          checkOutTIme: convertDateTimeFromServer(passengerEntity.checkOutTIme),
          appliedAt: convertDateTimeFromServer(passengerEntity.appliedAt),
          trip: passengerEntity?.trip?.id,
          user: passengerEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.passenger.home.createOrEditLabel" data-cy="PassengerCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.passenger.home.createOrEditLabel">Create or edit a Passenger</Translate>
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
                  id="passenger-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.passenger.passengerID')}
                id="passenger-passengerID"
                name="passengerID"
                data-cy="passengerID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.startLoca')}
                id="passenger-startLoca"
                name="startLoca"
                data-cy="startLoca"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.endLoca')}
                id="passenger-endLoca"
                name="endLoca"
                data-cy="endLoca"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.amountApproveFee')}
                id="passenger-amountApproveFee"
                name="amountApproveFee"
                data-cy="amountApproveFee"
                type="text"
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.passenger.luggageImg')}
                id="passenger-luggageImg"
                name="luggageImg"
                data-cy="luggageImg"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.luggageDescription')}
                id="passenger-luggageDescription"
                name="luggageDescription"
                data-cy="luggageDescription"
                type="text"
              />
              <ValidatedField label={translate('sTripBeApp.passenger.type')} id="passenger-type" name="type" data-cy="type" type="select">
                {passengerTypeValues.map(passengerType => (
                  <option value={passengerType} key={passengerType}>
                    {translate('sTripBeApp.PassengerType.' + passengerType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.passenger.status')}
                id="passenger-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {passengerStatusValues.map(passengerStatus => (
                  <option value={passengerStatus} key={passengerStatus}>
                    {translate('sTripBeApp.PassengerStatus.' + passengerStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.passenger.pickUpTime')}
                id="passenger-pickUpTime"
                name="pickUpTime"
                data-cy="pickUpTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.endTime')}
                id="passenger-endTime"
                name="endTime"
                data-cy="endTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.checkIn')}
                id="passenger-checkIn"
                name="checkIn"
                data-cy="checkIn"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.checkInTime')}
                id="passenger-checkInTime"
                name="checkInTime"
                data-cy="checkInTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.checkOut')}
                id="passenger-checkOut"
                name="checkOut"
                data-cy="checkOut"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.checkOutTIme')}
                id="passenger-checkOutTIme"
                name="checkOutTIme"
                data-cy="checkOutTIme"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.passenger.appliedAt')}
                id="passenger-appliedAt"
                name="appliedAt"
                data-cy="appliedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="passenger-trip" name="trip" data-cy="trip" label={translate('sTripBeApp.passenger.trip')} type="select">
                <option value="" key="0" />
                {trips
                  ? trips.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="passenger-user" name="user" data-cy="user" label={translate('sTripBeApp.passenger.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/passenger" replace color="info">
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

export default PassengerUpdate;
