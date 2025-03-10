import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-detail.reducer';

export const UserDetailDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userDetailEntity = useAppSelector(state => state.userDetail.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userDetailDetailsHeading">
          <Translate contentKey="sTripBeApp.userDetail.detail.title">UserDetail</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userDetailEntity.id}</dd>
          <dt>
            <span id="appUserDetail">
              <Translate contentKey="sTripBeApp.userDetail.appUserDetail">App User Detail</Translate>
            </span>
          </dt>
          <dd>{userDetailEntity.appUserDetail}</dd>
          <dt>
            <span id="userimage">
              <Translate contentKey="sTripBeApp.userDetail.userimage">Userimage</Translate>
            </span>
          </dt>
          <dd>
            {userDetailEntity.userimage ? (
              <div>
                {userDetailEntity.userimageContentType ? (
                  <a onClick={openFile(userDetailEntity.userimageContentType, userDetailEntity.userimage)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {userDetailEntity.userimageContentType}, {byteSize(userDetailEntity.userimage)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="phone">
              <Translate contentKey="sTripBeApp.userDetail.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{userDetailEntity.phone}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="sTripBeApp.userDetail.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{userDetailEntity.gender}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.userDetail.user">User</Translate>
          </dt>
          <dd>{userDetailEntity.user ? userDetailEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-detail" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-detail/${userDetailEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserDetailDetail;
