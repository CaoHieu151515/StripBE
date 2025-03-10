import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IDriver } from 'app/shared/model/driver.model';
import { DriverStatus } from 'app/shared/model/enumerations/driver-status.model';
import { getEntity, updateEntity, createEntity, reset } from './driver.reducer';

export const DriverUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const driverEntity = useAppSelector(state => state.driver.entity);
  const loading = useAppSelector(state => state.driver.loading);
  const updating = useAppSelector(state => state.driver.updating);
  const updateSuccess = useAppSelector(state => state.driver.updateSuccess);
  const driverStatusValues = Object.keys(DriverStatus);

  const handleClose = () => {
    navigate('/driver' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.expirationDate = convertDateTimeToServer(values.expirationDate);
    if (values.driverPoint !== undefined && typeof values.driverPoint !== 'number') {
      values.driverPoint = Number(values.driverPoint);
    }
    values.bannedDay = convertDateTimeToServer(values.bannedDay);

    const entity = {
      ...driverEntity,
      ...values,
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
          expirationDate: displayDefaultDateTime(),
          bannedDay: displayDefaultDateTime(),
        }
      : {
          driverStatus: 'ACTIVE',
          ...driverEntity,
          expirationDate: convertDateTimeFromServer(driverEntity.expirationDate),
          bannedDay: convertDateTimeFromServer(driverEntity.bannedDay),
          user: driverEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.driver.home.createOrEditLabel" data-cy="DriverCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.driver.home.createOrEditLabel">Create or edit a Driver</Translate>
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
                  id="driver-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.driver.driverID')}
                id="driver-driverID"
                name="driverID"
                data-cy="driverID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.driver.usedtoDriver')}
                id="driver-usedtoDriver"
                name="usedtoDriver"
                data-cy="usedtoDriver"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('sTripBeApp.driver.expirationDate')}
                id="driver-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.driver.driverStatus')}
                id="driver-driverStatus"
                name="driverStatus"
                data-cy="driverStatus"
                type="select"
              >
                {driverStatusValues.map(driverStatus => (
                  <option value={driverStatus} key={driverStatus}>
                    {translate('sTripBeApp.DriverStatus.' + driverStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.driver.driverPoint')}
                id="driver-driverPoint"
                name="driverPoint"
                data-cy="driverPoint"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.driver.bannedDay')}
                id="driver-bannedDay"
                name="bannedDay"
                data-cy="bannedDay"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.driver.driverLicense')}
                id="driver-driverLicense"
                name="driverLicense"
                data-cy="driverLicense"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.driver.identityCardFaceUp')}
                id="driver-identityCardFaceUp"
                name="identityCardFaceUp"
                data-cy="identityCardFaceUp"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.driver.identityCardFacedown')}
                id="driver-identityCardFacedown"
                name="identityCardFacedown"
                data-cy="identityCardFacedown"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField id="driver-user" name="user" data-cy="user" label={translate('sTripBeApp.driver.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/driver" replace color="info">
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

export default DriverUpdate;
