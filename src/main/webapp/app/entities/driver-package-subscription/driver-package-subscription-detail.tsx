import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './driver-package-subscription.reducer';

export const DriverPackageSubscriptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const driverPackageSubscriptionEntity = useAppSelector(state => state.driverPackageSubscription.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="driverPackageSubscriptionDetailsHeading">
          <Translate contentKey="sTripBeApp.driverPackageSubscription.detail.title">DriverPackageSubscription</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="sTripBeApp.driverPackageSubscription.id">Id</Translate>
            </span>
          </dt>
          <dd>{driverPackageSubscriptionEntity.id}</dd>
          <dt>
            <span id="purchaseDate">
              <Translate contentKey="sTripBeApp.driverPackageSubscription.purchaseDate">Purchase Date</Translate>
            </span>
          </dt>
          <dd>
            {driverPackageSubscriptionEntity.purchaseDate ? (
              <TextFormat value={driverPackageSubscriptionEntity.purchaseDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="sTripBeApp.driverPackageSubscription.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {driverPackageSubscriptionEntity.expirationDate ? (
              <TextFormat value={driverPackageSubscriptionEntity.expirationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="packagePrice">
              <Translate contentKey="sTripBeApp.driverPackageSubscription.packagePrice">Package Price</Translate>
            </span>
          </dt>
          <dd>{driverPackageSubscriptionEntity.packagePrice}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="sTripBeApp.driverPackageSubscription.active">Active</Translate>
            </span>
          </dt>
          <dd>{driverPackageSubscriptionEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.driverPackageSubscription.driver">Driver</Translate>
          </dt>
          <dd>{driverPackageSubscriptionEntity.driver ? driverPackageSubscriptionEntity.driver.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.driverPackageSubscription.packageDriver">Package Driver</Translate>
          </dt>
          <dd>{driverPackageSubscriptionEntity.packageDriver ? driverPackageSubscriptionEntity.packageDriver.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/driver-package-subscription" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/driver-package-subscription/${driverPackageSubscriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DriverPackageSubscriptionDetail;
