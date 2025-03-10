import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IUserWallet } from 'app/shared/model/user-wallet.model';
import { getEntity, updateEntity, createEntity, reset } from './user-wallet.reducer';

export const UserWalletUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userWalletEntity = useAppSelector(state => state.userWallet.entity);
  const loading = useAppSelector(state => state.userWallet.loading);
  const updating = useAppSelector(state => state.userWallet.updating);
  const updateSuccess = useAppSelector(state => state.userWallet.updateSuccess);

  const handleClose = () => {
    navigate('/user-wallet');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    values.mobifyDate = convertDateTimeToServer(values.mobifyDate);

    const entity = {
      ...userWalletEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          ...userWalletEntity,
          mobifyDate: convertDateTimeFromServer(userWalletEntity.mobifyDate),
          user: userWalletEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.userWallet.home.createOrEditLabel" data-cy="UserWalletCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.userWallet.home.createOrEditLabel">Create or edit a UserWallet</Translate>
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
                  id="user-wallet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.userWallet.userWallet')}
                id="user-wallet-userWallet"
                name="userWallet"
                data-cy="userWallet"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userWallet.before')}
                id="user-wallet-before"
                name="before"
                data-cy="before"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userWallet.amount')}
                id="user-wallet-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userWallet.current')}
                id="user-wallet-current"
                name="current"
                data-cy="current"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userWallet.mobifyDate')}
                id="user-wallet-mobifyDate"
                name="mobifyDate"
                data-cy="mobifyDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-wallet-user"
                name="user"
                data-cy="user"
                label={translate('sTripBeApp.userWallet.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-wallet" replace color="info">
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

export default UserWalletUpdate;
