import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { PackageDriverStatus } from 'app/shared/model/enumerations/package-driver-status.model';
import { createEntity, getEntity, reset, updateEntity } from './package-driver.reducer';

export const PackageDriverUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const packageDriverEntity = useAppSelector(state => state.packageDriver.entity);
  const loading = useAppSelector(state => state.packageDriver.loading);
  const updating = useAppSelector(state => state.packageDriver.updating);
  const updateSuccess = useAppSelector(state => state.packageDriver.updateSuccess);
  const packageDriverStatusValues = Object.keys(PackageDriverStatus);

  const handleClose = () => {
    navigate(`/package-driver${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.time !== undefined && typeof values.time !== 'number') {
      values.time = Number(values.time);
    }
    if (values.bonus !== undefined && typeof values.bonus !== 'number') {
      values.bonus = Number(values.bonus);
    }

    const entity = {
      ...packageDriverEntity,
      ...values,
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
          status: 'ACTIVE',
          ...packageDriverEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.packageDriver.home.createOrEditLabel" data-cy="PackageDriverCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.packageDriver.home.createOrEditLabel">Create or edit a PackageDriver</Translate>
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
                  id="package-driver-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.packageID')}
                id="package-driver-packageID"
                name="packageID"
                data-cy="packageID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.price')}
                id="package-driver-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.name')}
                id="package-driver-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.description')}
                id="package-driver-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.time')}
                id="package-driver-time"
                name="time"
                data-cy="time"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.bonus')}
                id="package-driver-bonus"
                name="bonus"
                data-cy="bonus"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.packageDriver.status')}
                id="package-driver-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {packageDriverStatusValues.map(packageDriverStatus => (
                  <option value={packageDriverStatus} key={packageDriverStatus}>
                    {translate(`sTripBeApp.PackageDriverStatus.${packageDriverStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/package-driver" replace color="info">
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

export default PackageDriverUpdate;
