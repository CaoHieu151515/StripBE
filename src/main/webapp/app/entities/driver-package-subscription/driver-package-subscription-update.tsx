import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDrivers } from 'app/entities/driver/driver.reducer';
import { getEntities as getPackageDrivers } from 'app/entities/package-driver/package-driver.reducer';
import { createEntity, getEntity, reset, updateEntity } from './driver-package-subscription.reducer';

export const DriverPackageSubscriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const drivers = useAppSelector(state => state.driver.entities);
  const packageDrivers = useAppSelector(state => state.packageDriver.entities);
  const driverPackageSubscriptionEntity = useAppSelector(state => state.driverPackageSubscription.entity);
  const loading = useAppSelector(state => state.driverPackageSubscription.loading);
  const updating = useAppSelector(state => state.driverPackageSubscription.updating);
  const updateSuccess = useAppSelector(state => state.driverPackageSubscription.updateSuccess);

  const handleClose = () => {
    navigate('/driver-package-subscription');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDrivers({}));
    dispatch(getPackageDrivers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.purchaseDate = convertDateTimeToServer(values.purchaseDate);
    values.expirationDate = convertDateTimeToServer(values.expirationDate);
    if (values.packagePrice !== undefined && typeof values.packagePrice !== 'number') {
      values.packagePrice = Number(values.packagePrice);
    }

    const entity = {
      ...driverPackageSubscriptionEntity,
      ...values,
      driver: drivers.find(it => it.id.toString() === values.driver?.toString()),
      packageDriver: packageDrivers.find(it => it.id.toString() === values.packageDriver?.toString()),
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
          purchaseDate: displayDefaultDateTime(),
          expirationDate: displayDefaultDateTime(),
        }
      : {
          ...driverPackageSubscriptionEntity,
          purchaseDate: convertDateTimeFromServer(driverPackageSubscriptionEntity.purchaseDate),
          expirationDate: convertDateTimeFromServer(driverPackageSubscriptionEntity.expirationDate),
          driver: driverPackageSubscriptionEntity?.driver?.id,
          packageDriver: driverPackageSubscriptionEntity?.packageDriver?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.driverPackageSubscription.home.createOrEditLabel" data-cy="DriverPackageSubscriptionCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.driverPackageSubscription.home.createOrEditLabel">
              Create or edit a DriverPackageSubscription
            </Translate>
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
                  id="driver-package-subscription-id"
                  label={translate('sTripBeApp.driverPackageSubscription.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.driverPackageSubscription.purchaseDate')}
                id="driver-package-subscription-purchaseDate"
                name="purchaseDate"
                data-cy="purchaseDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPackageSubscription.expirationDate')}
                id="driver-package-subscription-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPackageSubscription.packagePrice')}
                id="driver-package-subscription-packagePrice"
                name="packagePrice"
                data-cy="packagePrice"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.driverPackageSubscription.active')}
                id="driver-package-subscription-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="driver-package-subscription-driver"
                name="driver"
                data-cy="driver"
                label={translate('sTripBeApp.driverPackageSubscription.driver')}
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
              <ValidatedField
                id="driver-package-subscription-packageDriver"
                name="packageDriver"
                data-cy="packageDriver"
                label={translate('sTripBeApp.driverPackageSubscription.packageDriver')}
                type="select"
              >
                <option value="" key="0" />
                {packageDrivers
                  ? packageDrivers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/driver-package-subscription" replace color="info">
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

export default DriverPackageSubscriptionUpdate;
