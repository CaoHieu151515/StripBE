import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISystemWallet } from 'app/shared/model/system-wallet.model';
import { getEntity, updateEntity, createEntity, reset } from './system-wallet.reducer';

export const SystemWalletUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const systemWalletEntity = useAppSelector(state => state.systemWallet.entity);
  const loading = useAppSelector(state => state.systemWallet.loading);
  const updating = useAppSelector(state => state.systemWallet.updating);
  const updateSuccess = useAppSelector(state => state.systemWallet.updateSuccess);

  const handleClose = () => {
    navigate('/system-wallet');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.before !== undefined && typeof values.before !== 'number') {
      values.before = Number(values.before);
    }
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    if (values.current !== undefined && typeof values.current !== 'number') {
      values.current = Number(values.current);
    }
    if (values.blockAmount !== undefined && typeof values.blockAmount !== 'number') {
      values.blockAmount = Number(values.blockAmount);
    }
    values.mobifyDate = convertDateTimeToServer(values.mobifyDate);

    const entity = {
      ...systemWalletEntity,
      ...values,
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
          mobifyDate: displayDefaultDateTime(),
        }
      : {
          ...systemWalletEntity,
          mobifyDate: convertDateTimeFromServer(systemWalletEntity.mobifyDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.systemWallet.home.createOrEditLabel" data-cy="SystemWalletCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.systemWallet.home.createOrEditLabel">Create or edit a SystemWallet</Translate>
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
                  id="system-wallet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.systemWallet.systemWalletID')}
                id="system-wallet-systemWalletID"
                name="systemWalletID"
                data-cy="systemWalletID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.systemWallet.before')}
                id="system-wallet-before"
                name="before"
                data-cy="before"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.systemWallet.amount')}
                id="system-wallet-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.systemWallet.current')}
                id="system-wallet-current"
                name="current"
                data-cy="current"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.systemWallet.blockAmount')}
                id="system-wallet-blockAmount"
                name="blockAmount"
                data-cy="blockAmount"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.systemWallet.mobifyDate')}
                id="system-wallet-mobifyDate"
                name="mobifyDate"
                data-cy="mobifyDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/system-wallet" replace color="info">
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

export default SystemWalletUpdate;
