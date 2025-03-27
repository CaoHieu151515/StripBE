import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserWallets } from 'app/entities/user-wallet/user-wallet.reducer';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';
import { createEntity, getEntity, reset, updateEntity } from './wallet-deposit.reducer';

export const WalletDepositUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userWallets = useAppSelector(state => state.userWallet.entities);
  const walletDepositEntity = useAppSelector(state => state.walletDeposit.entity);
  const loading = useAppSelector(state => state.walletDeposit.loading);
  const updating = useAppSelector(state => state.walletDeposit.updating);
  const updateSuccess = useAppSelector(state => state.walletDeposit.updateSuccess);
  const paymentStatusValues = Object.keys(PaymentStatus);

  const handleClose = () => {
    navigate('/wallet-deposit');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserWallets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...walletDepositEntity,
      ...values,
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
          status: 'PENDING',
          ...walletDepositEntity,
          date: convertDateTimeFromServer(walletDepositEntity.date),
          userWallet: walletDepositEntity?.userWallet?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.walletDeposit.home.createOrEditLabel" data-cy="WalletDepositCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.walletDeposit.home.createOrEditLabel">Create or edit a WalletDeposit</Translate>
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
                  id="wallet-deposit-id"
                  label={translate('sTripBeApp.walletDeposit.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.walletDeposit.bankNumber')}
                id="wallet-deposit-bankNumber"
                name="bankNumber"
                data-cy="bankNumber"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletDeposit.nameOfBank')}
                id="wallet-deposit-nameOfBank"
                name="nameOfBank"
                data-cy="nameOfBank"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletDeposit.bank')}
                id="wallet-deposit-bank"
                name="bank"
                data-cy="bank"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletDeposit.amount')}
                id="wallet-deposit-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletDeposit.date')}
                id="wallet-deposit-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sTripBeApp.walletDeposit.status')}
                id="wallet-deposit-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {paymentStatusValues.map(paymentStatus => (
                  <option value={paymentStatus} key={paymentStatus}>
                    {translate(`sTripBeApp.PaymentStatus.${paymentStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="wallet-deposit-userWallet"
                name="userWallet"
                data-cy="userWallet"
                label={translate('sTripBeApp.walletDeposit.userWallet')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/wallet-deposit" replace color="info">
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

export default WalletDepositUpdate;
