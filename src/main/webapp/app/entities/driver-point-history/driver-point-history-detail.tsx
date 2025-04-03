import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './driver-point-history.reducer';

export const DriverPointHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const driverPointHistoryEntity = useAppSelector(state => state.driverPointHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="driverPointHistoryDetailsHeading">
          <Translate contentKey="sTripBeApp.driverPointHistory.detail.title">DriverPointHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{driverPointHistoryEntity.id}</dd>
          <dt>
            <span id="pointId">
              <Translate contentKey="sTripBeApp.driverPointHistory.pointId">Point Id</Translate>
            </span>
          </dt>
          <dd>{driverPointHistoryEntity.pointId}</dd>
          <dt>
            <span id="point">
              <Translate contentKey="sTripBeApp.driverPointHistory.point">Point</Translate>
            </span>
          </dt>
          <dd>{driverPointHistoryEntity.point}</dd>
          <dt>
            <span id="reason">
              <Translate contentKey="sTripBeApp.driverPointHistory.reason">Reason</Translate>
            </span>
          </dt>
          <dd>{driverPointHistoryEntity.reason}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sTripBeApp.driverPointHistory.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {driverPointHistoryEntity.date ? (
              <TextFormat value={driverPointHistoryEntity.date} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="sTripBeApp.driverPointHistory.status">Status</Translate>
            </span>
          </dt>
          <dd>{driverPointHistoryEntity.status}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.driverPointHistory.driver">Driver</Translate>
          </dt>
          <dd>{driverPointHistoryEntity.driver ? driverPointHistoryEntity.driver.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.driverPointHistory.userDetail">User Detail</Translate>
          </dt>
          <dd>{driverPointHistoryEntity.userDetail ? driverPointHistoryEntity.userDetail.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/driver-point-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/driver-point-history/${driverPointHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DriverPointHistoryDetail;
