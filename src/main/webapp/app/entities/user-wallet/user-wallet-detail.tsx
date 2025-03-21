import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-wallet.reducer';

export const UserWalletDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userWalletEntity = useAppSelector(state => state.userWallet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userWalletDetailsHeading">
          <Translate contentKey="sTripBeApp.userWallet.detail.title">UserWallet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userWalletEntity.id}</dd>
          <dt>
            <span id="userWallet">
              <Translate contentKey="sTripBeApp.userWallet.userWallet">User Wallet</Translate>
            </span>
          </dt>
          <dd>{userWalletEntity.userWallet}</dd>
          <dt>
            <span id="before">
              <Translate contentKey="sTripBeApp.userWallet.before">Before</Translate>
            </span>
          </dt>
          <dd>{userWalletEntity.before}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="sTripBeApp.userWallet.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{userWalletEntity.amount}</dd>
          <dt>
            <span id="current">
              <Translate contentKey="sTripBeApp.userWallet.current">Current</Translate>
            </span>
          </dt>
          <dd>{userWalletEntity.current}</dd>
          <dt>
            <span id="mobifyDate">
              <Translate contentKey="sTripBeApp.userWallet.mobifyDate">Mobify Date</Translate>
            </span>
          </dt>
          <dd>
            {userWalletEntity.mobifyDate ? <TextFormat value={userWalletEntity.mobifyDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="sTripBeApp.userWallet.user">User</Translate>
          </dt>
          <dd>{userWalletEntity.user ? userWalletEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-wallet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-wallet/${userWalletEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserWalletDetail;
