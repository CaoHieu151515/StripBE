import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './trip.reducer';

export const TripDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tripEntity = useAppSelector(state => state.trip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tripDetailsHeading">
          <Translate contentKey="sTripBeApp.trip.detail.title">Trip</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tripEntity.id}</dd>
          <dt>
            <span id="tripID">
              <Translate contentKey="sTripBeApp.trip.tripID">Trip ID</Translate>
            </span>
          </dt>
          <dd>{tripEntity.tripID}</dd>
          <dt>
            <span id="tripImg">
              <Translate contentKey="sTripBeApp.trip.tripImg">Trip Img</Translate>
            </span>
          </dt>
          <dd>
            {tripEntity.tripImg ? (
              <div>
                {tripEntity.tripImgContentType ? (
                  <a onClick={openFile(tripEntity.tripImgContentType, tripEntity.tripImg)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {tripEntity.tripImgContentType}, {byteSize(tripEntity.tripImg)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="pricePerSeat">
              <Translate contentKey="sTripBeApp.trip.pricePerSeat">Price Per Seat</Translate>
            </span>
          </dt>
          <dd>{tripEntity.pricePerSeat}</dd>
          <dt>
            <span id="maxSeat">
              <Translate contentKey="sTripBeApp.trip.maxSeat">Max Seat</Translate>
            </span>
          </dt>
          <dd>{tripEntity.maxSeat}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="sTripBeApp.trip.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{tripEntity.startDate ? <TextFormat value={tripEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="sTripBeApp.trip.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{tripEntity.endDate ? <TextFormat value={tripEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="currentSeat">
              <Translate contentKey="sTripBeApp.trip.currentSeat">Current Seat</Translate>
            </span>
          </dt>
          <dd>{tripEntity.currentSeat}</dd>
          <dt>
            <span id="startLocation">
              <Translate contentKey="sTripBeApp.trip.startLocation">Start Location</Translate>
            </span>
          </dt>
          <dd>{tripEntity.startLocation}</dd>
          <dt>
            <span id="endLocation">
              <Translate contentKey="sTripBeApp.trip.endLocation">End Location</Translate>
            </span>
          </dt>
          <dd>{tripEntity.endLocation}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="sTripBeApp.trip.description">Description</Translate>
            </span>
          </dt>
          <dd>{tripEntity.description}</dd>
          <dt>
            <span id="condition">
              <Translate contentKey="sTripBeApp.trip.condition">Condition</Translate>
            </span>
          </dt>
          <dd>{tripEntity.condition}</dd>
          <dt>
            <span id="cancelReason">
              <Translate contentKey="sTripBeApp.trip.cancelReason">Cancel Reason</Translate>
            </span>
          </dt>
          <dd>{tripEntity.cancelReason}</dd>
          <dt>
            <span id="tripStatus">
              <Translate contentKey="sTripBeApp.trip.tripStatus">Trip Status</Translate>
            </span>
          </dt>
          <dd>{tripEntity.tripStatus}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.trip.vehicle">Vehicle</Translate>
          </dt>
          <dd>{tripEntity.vehicle ? tripEntity.vehicle.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.trip.driver">Driver</Translate>
          </dt>
          <dd>{tripEntity.driver ? tripEntity.driver.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/trip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/trip/${tripEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TripDetail;
