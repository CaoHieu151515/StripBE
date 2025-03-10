import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip-stop-location.reducer';

export const TripStopLocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tripStopLocationEntity = useAppSelector(state => state.tripStopLocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripStopLocationDetailsHeading">
          <Translate contentKey="sTripBeApp.tripStopLocation.detail.title">TripStopLocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tripStopLocationEntity.id}</dd>
          <dt>
            <span id="stopLocaID">
              <Translate contentKey="sTripBeApp.tripStopLocation.stopLocaID">Stop Loca ID</Translate>
            </span>
          </dt>
          <dd>{tripStopLocationEntity.stopLocaID}</dd>
          <dt>
            <span id="stopLoca">
              <Translate contentKey="sTripBeApp.tripStopLocation.stopLoca">Stop Loca</Translate>
            </span>
          </dt>
          <dd>{tripStopLocationEntity.stopLoca}</dd>
          <dt>
            <span id="stopLocaTime">
              <Translate contentKey="sTripBeApp.tripStopLocation.stopLocaTime">Stop Loca Time</Translate>
            </span>
          </dt>
          <dd>
            {tripStopLocationEntity.stopLocaTime ? (
              <TextFormat value={tripStopLocationEntity.stopLocaTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="stopLocaStatus">
              <Translate contentKey="sTripBeApp.tripStopLocation.stopLocaStatus">Stop Loca Status</Translate>
            </span>
          </dt>
          <dd>{tripStopLocationEntity.stopLocaStatus}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.tripStopLocation.trip">Trip</Translate>
          </dt>
          <dd>{tripStopLocationEntity.trip ? tripStopLocationEntity.trip.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip-stop-location" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip-stop-location/${tripStopLocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripStopLocationDetail;
