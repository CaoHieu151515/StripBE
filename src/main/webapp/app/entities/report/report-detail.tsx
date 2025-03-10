import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './report.reducer';

export const ReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reportEntity = useAppSelector(state => state.report.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reportDetailsHeading">
          <Translate contentKey="sTripBeApp.report.detail.title">Report</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reportEntity.id}</dd>
          <dt>
            <span id="reportID">
              <Translate contentKey="sTripBeApp.report.reportID">Report ID</Translate>
            </span>
          </dt>
          <dd>{reportEntity.reportID}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="sTripBeApp.report.date">Date</Translate>
            </span>
          </dt>
          <dd>{reportEntity.date ? <TextFormat value={reportEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="sTripBeApp.report.content">Content</Translate>
            </span>
          </dt>
          <dd>{reportEntity.content}</dd>
          <dt>
            <span id="reportStatus">
              <Translate contentKey="sTripBeApp.report.reportStatus">Report Status</Translate>
            </span>
          </dt>
          <dd>{reportEntity.reportStatus}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.report.user">User</Translate>
          </dt>
          <dd>{reportEntity.user ? reportEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/report/${reportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReportDetail;
