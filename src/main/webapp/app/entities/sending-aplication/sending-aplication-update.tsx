import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ISendingAplication } from 'app/shared/model/sending-aplication.model';
import { AplicationType } from 'app/shared/model/enumerations/aplication-type.model';
import { getEntity, updateEntity, createEntity, reset } from './sending-aplication.reducer';

export const SendingAplicationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const sendingAplicationEntity = useAppSelector(state => state.sendingAplication.entity);
  const loading = useAppSelector(state => state.sendingAplication.loading);
  const updating = useAppSelector(state => state.sendingAplication.updating);
  const updateSuccess = useAppSelector(state => state.sendingAplication.updateSuccess);
  const aplicationTypeValues = Object.keys(AplicationType);

  const handleClose = () => {
    navigate('/sending-aplication');
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

    const entity = {
      ...sendingAplicationEntity,
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
      ? {}
      : {
          sendApplicationType: 'FEEDBACKSYSTEM',
          ...sendingAplicationEntity,
          user: sendingAplicationEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.sendingAplication.home.createOrEditLabel" data-cy="SendingAplicationCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.sendingAplication.home.createOrEditLabel">Create or edit a SendingAplication</Translate>
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
                  id="sending-aplication-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.sendingAplication.apliID')}
                id="sending-aplication-apliID"
                name="apliID"
                data-cy="apliID"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.sendingAplication.sendApplicationType')}
                id="sending-aplication-sendApplicationType"
                name="sendApplicationType"
                data-cy="sendApplicationType"
                type="select"
              >
                {aplicationTypeValues.map(aplicationType => (
                  <option value={aplicationType} key={aplicationType}>
                    {translate('sTripBeApp.AplicationType.' + aplicationType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sTripBeApp.sendingAplication.content')}
                id="sending-aplication-content"
                name="content"
                data-cy="content"
                type="text"
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.sendingAplication.img')}
                id="sending-aplication-img"
                name="img"
                data-cy="img"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                id="sending-aplication-user"
                name="user"
                data-cy="user"
                label={translate('sTripBeApp.sendingAplication.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sending-aplication" replace color="info">
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

export default SendingAplicationUpdate;
