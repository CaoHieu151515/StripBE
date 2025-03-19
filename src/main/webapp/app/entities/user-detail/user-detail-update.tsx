import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './user-detail.reducer';

export const UserDetailUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const userDetailEntity = useAppSelector(state => state.userDetail.entity);
  const loading = useAppSelector(state => state.userDetail.loading);
  const updating = useAppSelector(state => state.userDetail.updating);
  const updateSuccess = useAppSelector(state => state.userDetail.updateSuccess);

  const handleClose = () => {
    navigate('/user-detail');
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

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dob = convertDateTimeToServer(values.dob);

    const entity = {
      ...userDetailEntity,
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
          dob: displayDefaultDateTime(),
        }
      : {
          ...userDetailEntity,
          dob: convertDateTimeFromServer(userDetailEntity.dob),
          user: userDetailEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sTripBeApp.userDetail.home.createOrEditLabel" data-cy="UserDetailCreateUpdateHeading">
            <Translate contentKey="sTripBeApp.userDetail.home.createOrEditLabel">Create or edit a UserDetail</Translate>
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
                  id="user-detail-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sTripBeApp.userDetail.appUserDetail')}
                id="user-detail-appUserDetail"
                name="appUserDetail"
                data-cy="appUserDetail"
                type="text"
              />
              <ValidatedBlobField
                label={translate('sTripBeApp.userDetail.userimage')}
                id="user-detail-userimage"
                name="userimage"
                data-cy="userimage"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                label={translate('sTripBeApp.userDetail.phone')}
                id="user-detail-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userDetail.gender')}
                id="user-detail-gender"
                name="gender"
                data-cy="gender"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userDetail.address')}
                id="user-detail-address"
                name="address"
                data-cy="address"
                type="text"
              />
              <ValidatedField
                label={translate('sTripBeApp.userDetail.dob')}
                id="user-detail-dob"
                name="dob"
                data-cy="dob"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-detail-user"
                name="user"
                data-cy="user"
                label={translate('sTripBeApp.userDetail.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-detail" replace color="info">
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

export default UserDetailUpdate;
