import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './system-wallet.reducer';

export const SystemWalletDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const systemWalletEntity = useAppSelector(state => state.systemWallet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="systemWalletDetailsHeading">
          <Translate contentKey="sTripBeApp.systemWallet.detail.title">SystemWallet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{systemWalletEntity.id}</dd>
          <dt>
            <span id="systemWalletID">
              <Translate contentKey="sTripBeApp.systemWallet.systemWalletID">System Wallet ID</Translate>
            </span>
          </dt>
          <dd>{systemWalletEntity.systemWalletID}</dd>
          <dt>
            <span id="before">
              <Translate contentKey="sTripBeApp.systemWallet.before">Before</Translate>
            </span>
          </dt>
          <dd>{systemWalletEntity.before}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="sTripBeApp.systemWallet.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{systemWalletEntity.amount}</dd>
          <dt>
            <span id="current">
              <Translate contentKey="sTripBeApp.systemWallet.current">Current</Translate>
            </span>
          </dt>
          <dd>{systemWalletEntity.current}</dd>
          <dt>
            <span id="blockAmount">
              <Translate contentKey="sTripBeApp.systemWallet.blockAmount">Block Amount</Translate>
            </span>
          </dt>
          <dd>{systemWalletEntity.blockAmount}</dd>
          <dt>
            <span id="mobifyDate">
              <Translate contentKey="sTripBeApp.systemWallet.mobifyDate">Mobify Date</Translate>
            </span>
          </dt>
          <dd>
            {systemWalletEntity.mobifyDate ? (
              <TextFormat value={systemWalletEntity.mobifyDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/system-wallet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/system-wallet/${systemWalletEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SystemWalletDetail;
