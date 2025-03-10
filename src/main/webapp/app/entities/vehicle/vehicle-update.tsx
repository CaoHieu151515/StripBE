import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDriver } from 'app/shared/model/driver.model';
import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { VehicleType } from 'app/shared/model/enumerations/vehicle-type.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle.reducer';

export const VehicleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const drivers = useAppSelector(state => state.driver.entities);
  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  const loading = useAppSelector(state => state.vehicle.loading);
  const updating = useAppSelector(state => state.vehicle.updating);
  const updateSuccess = useAppSelector(state => state.vehicle.updateSuccess);
  const vehicleTypeValues = Object.keys(VehicleType);

  const handleClose = () => {
    navigate('/vehicle' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDrivers({}));
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
    if (values.numberOfSeats !== undefined && typeof values.numberOfSeats !== 'number') {
      values.numberOfSeats = Number(values.numberOfSeats);
    }

    const entity = {
      ...vehicleEntity,
      ...values,
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
      ? {}
      : {
          vehicleType: 'CAR',
          ...vehicleEntity,
          driver: vehicleEntity?.driver?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.vehicle.home.createOrEditLabel" data-cy="VehicleCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.vehicle.home.createOrEditLabel">Create or edit a Vehicle</Translate>
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
                  id="vehicle-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.vehicle.vehicleID')}
                id="vehicle-vehicleID"
                name="vehicleID"
                data-cy="vehicleID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.vehicle.vehicleType')}
                id="vehicle-vehicleType"
                name="vehicleType"
                data-cy="vehicleType"
                type="select"
              >
                {vehicleTypeValues.map(vehicleType => (
                  <option value={vehicleType} key={vehicleType}>
                    {translate('sTripBeApp.VehicleType.' + vehicleType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('sTripBeApp.vehicle.vehicleImage')}
                id="vehicle-vehicleImage"
                name="vehicleImage"
                data-cy="vehicleImage"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.vehicle.carregistration')}
                id="vehicle-carregistration"
                name="carregistration"
                data-cy="carregistration"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.vehicle.vehicleInspectionCertificate')}
                id="vehicle-vehicleInspectionCertificate"
                name="vehicleInspectionCertificate"
                data-cy="vehicleInspectionCertificate"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.vehicle.carInsurance')}
                id="vehicle-carInsurance"
                name="carInsurance"
                data-cy="carInsurance"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('sTripBeApp.vehicle.vehicleNumber')}
                id="vehicle-vehicleNumber"
                name="vehicleNumber"
                data-cy="vehicleNumber"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.vehicle.numberOfSeats')}
                id="vehicle-numberOfSeats"
                name="numberOfSeats"
                data-cy="numberOfSeats"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.vehicle.vehicleColor')}
                id="vehicle-vehicleColor"
                name="vehicleColor"
                data-cy="vehicleColor"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.vehicle.vehicleBrand')}
                id="vehicle-vehicleBrand"
                name="vehicleBrand"
                data-cy="vehicleBrand"
                type="text"
              />
              <ValidatedField
                id="vehicle-driver"
                name="driver"
                data-cy="driver"
                label={translate('sTripBeApp.vehicle.driver')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle" replace color="info">
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

export default VehicleUpdate;
