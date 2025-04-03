import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './wallet-transaction.reducer';

export const WalletTransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const walletTransactionEntity = useAppSelector(state => state.walletTransaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="walletTransactionDetailsHeading">
          <Translate contentKey="sTripBeApp.walletTransaction.detail.title">WalletTransaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.id}</dd>
          <dt>
            <span id="transID">
              <Translate contentKey="sTripBeApp.walletTransaction.transID">Trans ID</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.transID}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="sTripBeApp.walletTransaction.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.amount}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sTripBeApp.walletTransaction.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {walletTransactionEntity.date ? <TextFormat value={walletTransactionEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="walletType">
              <Translate contentKey="sTripBeApp.walletTransaction.walletType">Wallet Type</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.walletType}</dd>
          <dt>
            <span id="transStatus">
              <Translate contentKey="sTripBeApp.walletTransaction.transStatus">Trans Status</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.transStatus}</dd>
          <dt>
            <span id="transactionThirdPartyID">
              <Translate contentKey="sTripBeApp.walletTransaction.transactionThirdPartyID">Transaction Third Party ID</Translate>
            </span>
          </dt>
          <dd>{walletTransactionEntity.transactionThirdPartyID}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.walletTransaction.systemWallet">System Wallet</Translate>
          </dt>
          <dd>{walletTransactionEntity.systemWallet ? walletTransactionEntity.systemWallet.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.walletTransaction.payment">Payment</Translate>
          </dt>
          <dd>{walletTransactionEntity.payment ? walletTransactionEntity.payment.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.walletTransaction.userWallet">User Wallet</Translate>
          </dt>
          <dd>{walletTransactionEntity.userWallet ? walletTransactionEntity.userWallet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/wallet-transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wallet-transaction/${walletTransactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WalletTransactionDetail;
