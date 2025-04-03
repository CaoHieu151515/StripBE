import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSystemWallets } from 'app/entities/system-wallet/system-wallet.reducer';
import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { getEntities as getUserWallets } from 'app/entities/user-wallet/user-wallet.reducer';
import { WalletTransactionType } from 'app/shared/model/enumerations/wallet-transaction-type.model';
import { TransactionStatus } from 'app/shared/model/enumerations/transaction-status.model';
import { createEntity, getEntity, reset, updateEntity } from './wallet-transaction.reducer';

export const WalletTransactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const systemWallets = useAppSelector(state => state.systemWallet.entities);
  const payments = useAppSelector(state => state.payment.entities);
  const userWallets = useAppSelector(state => state.userWallet.entities);
  const walletTransactionEntity = useAppSelector(state => state.walletTransaction.entity);
  const loading = useAppSelector(state => state.walletTransaction.loading);
  const updating = useAppSelector(state => state.walletTransaction.updating);
  const updateSuccess = useAppSelector(state => state.walletTransaction.updateSuccess);
  const walletTransactionTypeValues = Object.keys(WalletTransactionType);
  const transactionStatusValues = Object.keys(TransactionStatus);

  const handleClose = () => {
    navigate('/wallet-transaction');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSystemWallets({}));
    dispatch(getPayments({}));
    dispatch(getUserWallets({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...walletTransactionEntity,
      ...values,
      systemWallet: systemWallets.find(it => it.id.toString() === values.systemWallet?.toString()),
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
      userWallet: userWallets.find(it => it.id.toString() === values.userWallet?.toString()),
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
          date: displayDefaultDateTime(),
        }
      : {
          walletType: 'DEPOSIT',
          transStatus: 'PENDING',
          ...walletTransactionEntity,
          date: convertDateTimeFromServer(walletTransactionEntity.date),
          systemWallet: walletTransactionEntity?.systemWallet?.id,
          payment: walletTransactionEntity?.payment?.id,
          userWallet: walletTransactionEntity?.userWallet?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.walletTransaction.home.createOrEditLabel" data-cy="WalletTransactionCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.walletTransaction.home.createOrEditLabel">Create or edit a WalletTransaction</Translate>
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
                  id="wallet-transaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.walletTransaction.transID')}
                id="wallet-transaction-transID"
                name="transID"
                data-cy="transID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletTransaction.amount')}
                id="wallet-transaction-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletTransaction.date')}
                id="wallet-transaction-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletTransaction.walletType')}
                id="wallet-transaction-walletType"
                name="walletType"
                data-cy="walletType"
                type="select"
              >
                {walletTransactionTypeValues.map(walletTransactionType => (
                  <option value={walletTransactionType} key={walletTransactionType}>
                    {translate(`sTripBeApp.WalletTransactionType.${walletTransactionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.walletTransaction.transStatus')}
                id="wallet-transaction-transStatus"
                name="transStatus"
                data-cy="transStatus"
                type="select"
              >
                {transactionStatusValues.map(transactionStatus => (
                  <option value={transactionStatus} key={transactionStatus}>
                    {translate(`sTripBeApp.TransactionStatus.${transactionStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.walletTransaction.transactionThirdPartyID')}
                id="wallet-transaction-transactionThirdPartyID"
                name="transactionThirdPartyID"
                data-cy="transactionThirdPartyID"
                type="text"
              />
              <ValidatedField
                id="wallet-transaction-systemWallet"
                name="systemWallet"
                data-cy="systemWallet"
                label={translate('sTripBeApp.walletTransaction.systemWallet')}
                type="select"
              >
                <option value="" key="0" />
                {systemWallets
                  ? systemWallets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="wallet-transaction-payment"
                name="payment"
                data-cy="payment"
                label={translate('sTripBeApp.walletTransaction.payment')}
                type="select"
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="wallet-transaction-userWallet"
                name="userWallet"
                data-cy="userWallet"
                label={translate('sTripBeApp.walletTransaction.userWallet')}
                type="select"
              >
                <option value="" key="0" />
                {userWallets
                  ? userWallets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/wallet-transaction" replace color="info">
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

export default WalletTransactionUpdate;
