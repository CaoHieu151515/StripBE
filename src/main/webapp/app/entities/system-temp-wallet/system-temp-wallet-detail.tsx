import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './system-temp-wallet.reducer';

export const SystemTempWalletDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const systemTempWalletEntity = useAppSelector(state => state.systemTempWallet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="systemTempWalletDetailsHeading">
          <Translate contentKey="sTripBeApp.systemTempWallet.detail.title">SystemTempWallet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{systemTempWalletEntity.id}</dd>
          <dt>
            <span id="systemWalletID">
              <Translate contentKey="sTripBeApp.systemTempWallet.systemWalletID">System Wallet ID</Translate>
            </span>
          </dt>
          <dd>{systemTempWalletEntity.systemWalletID}</dd>
          <dt>
            <span id="before">
              <Translate contentKey="sTripBeApp.systemTempWallet.before">Before</Translate>
            </span>
          </dt>
          <dd>{systemTempWalletEntity.before}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="sTripBeApp.systemTempWallet.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{systemTempWalletEntity.amount}</dd>
          <dt>
            <span id="current">
              <Translate contentKey="sTripBeApp.systemTempWallet.current">Current</Translate>
            </span>
          </dt>
          <dd>{systemTempWalletEntity.current}</dd>
          <dt>
            <span id="mobifyDate">
              <Translate contentKey="sTripBeApp.systemTempWallet.mobifyDate">Mobify Date</Translate>
            </span>
          </dt>
          <dd>
            {systemTempWalletEntity.mobifyDate ? (
              <TextFormat value={systemTempWalletEntity.mobifyDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/system-temp-wallet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/system-temp-wallet/${systemTempWalletEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SystemTempWalletDetail;
