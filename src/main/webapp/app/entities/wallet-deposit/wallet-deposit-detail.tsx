import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './wallet-deposit.reducer';

export const WalletDepositDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const walletDepositEntity = useAppSelector(state => state.walletDeposit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="walletDepositDetailsHeading">
          <Translate contentKey="sTripBeApp.walletDeposit.detail.title">WalletDeposit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="sTripBeApp.walletDeposit.id">Id</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.id}</dd>
          <dt>
            <span id="bankNumber">
              <Translate contentKey="sTripBeApp.walletDeposit.bankNumber">Bank Number</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.bankNumber}</dd>
          <dt>
            <span id="nameOfBank">
              <Translate contentKey="sTripBeApp.walletDeposit.nameOfBank">Name Of Bank</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.nameOfBank}</dd>
          <dt>
            <span id="bank">
              <Translate contentKey="sTripBeApp.walletDeposit.bank">Bank</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.bank}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="sTripBeApp.walletDeposit.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.amount}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sTripBeApp.walletDeposit.date">Date</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.date ? <TextFormat value={walletDepositEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="sTripBeApp.walletDeposit.status">Status</Translate>
            </span>
          </dt>
          <dd>{walletDepositEntity.status}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.walletDeposit.userWallet">User Wallet</Translate>
          </dt>
          <dd>{walletDepositEntity.userWallet ? walletDepositEntity.userWallet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/wallet-deposit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wallet-deposit/${walletDepositEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WalletDepositDetail;
