import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './driver.reducer';

export const DriverDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const driverEntity = useAppSelector(state => state.driver.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="driverDetailsHeading">
          <Translate contentKey="sTripBeApp.driver.detail.title">Driver</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{driverEntity.id}</dd>
          <dt>
            <span id="driverID">
              <Translate contentKey="sTripBeApp.driver.driverID">Driver ID</Translate>
            </span>
          </dt>
          <dd>{driverEntity.driverID}</dd>
          <dt>
            <span id="usedtoDriver">
              <Translate contentKey="sTripBeApp.driver.usedtoDriver">Usedto Driver</Translate>
            </span>
          </dt>
          <dd>{driverEntity.usedtoDriver ? 'true' : 'false'}</dd>
          <dt>
            <span id="expirationDate">
              <Translate contentKey="sTripBeApp.driver.expirationDate">Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {driverEntity.expirationDate ? <TextFormat value={driverEntity.expirationDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="driverStatus">
              <Translate contentKey="sTripBeApp.driver.driverStatus">Driver Status</Translate>
            </span>
          </dt>
          <dd>{driverEntity.driverStatus}</dd>
          <dt>
            <span id="driverPoint">
              <Translate contentKey="sTripBeApp.driver.driverPoint">Driver Point</Translate>
            </span>
          </dt>
          <dd>{driverEntity.driverPoint}</dd>
          <dt>
            <span id="bannedDay">
              <Translate contentKey="sTripBeApp.driver.bannedDay">Banned Day</Translate>
            </span>
          </dt>
          <dd>{driverEntity.bannedDay ? <TextFormat value={driverEntity.bannedDay} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="driverLicense">
              <Translate contentKey="sTripBeApp.driver.driverLicense">Driver License</Translate>
            </span>
          </dt>
          <dd>
            {driverEntity.driverLicense ? (
              <div>
                {driverEntity.driverLicenseContentType ? (
                  <a onClick={openFile(driverEntity.driverLicenseContentType, driverEntity.driverLicense)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {driverEntity.driverLicenseContentType}, {byteSize(driverEntity.driverLicense)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="identityCardFaceUp">
              <Translate contentKey="sTripBeApp.driver.identityCardFaceUp">Identity Card Face Up</Translate>
            </span>
          </dt>
          <dd>
            {driverEntity.identityCardFaceUp ? (
              <div>
                {driverEntity.identityCardFaceUpContentType ? (
                  <a onClick={openFile(driverEntity.identityCardFaceUpContentType, driverEntity.identityCardFaceUp)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {driverEntity.identityCardFaceUpContentType}, {byteSize(driverEntity.identityCardFaceUp)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="identityCardFacedown">
              <Translate contentKey="sTripBeApp.driver.identityCardFacedown">Identity Card Facedown</Translate>
            </span>
          </dt>
          <dd>
            {driverEntity.identityCardFacedown ? (
              <div>
                {driverEntity.identityCardFacedownContentType ? (
                  <a onClick={openFile(driverEntity.identityCardFacedownContentType, driverEntity.identityCardFacedown)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {driverEntity.identityCardFacedownContentType}, {byteSize(driverEntity.identityCardFacedown)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="sTripBeApp.driver.user">User</Translate>
          </dt>
          <dd>{driverEntity.user ? driverEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/driver" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/driver/${driverEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DriverDetail;
