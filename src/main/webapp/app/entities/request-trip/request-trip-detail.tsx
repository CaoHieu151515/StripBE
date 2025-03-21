import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate, byteSize, openFile } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './request-trip.reducer';

export const RequestTripDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const requestTripEntity = useAppSelector(state => state.requestTrip.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="requestTripDetailsHeading">
          <Translate contentKey="sTripBeApp.requestTrip.detail.title">RequestTrip</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.id}</dd>
          <dt>
            <span id="requestTripID">
              <Translate contentKey="sTripBeApp.requestTrip.requestTripID">Request Trip ID</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.requestTripID}</dd>
          <dt>
            <span id="startLoca">
              <Translate contentKey="sTripBeApp.requestTrip.startLoca">Start Loca</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.startLoca}</dd>
          <dt>
            <span id="endLoca">
              <Translate contentKey="sTripBeApp.requestTrip.endLoca">End Loca</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.endLoca}</dd>
          <dt>
            <span id="amountApproveFee">
              <Translate contentKey="sTripBeApp.requestTrip.amountApproveFee">Amount Approve Fee</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.amountApproveFee}</dd>
          <dt>
            <span id="luggageImg">
              <Translate contentKey="sTripBeApp.requestTrip.luggageImg">Luggage Img</Translate>
            </span>
          </dt>
          <dd>
            {requestTripEntity.luggageImg ? (
              <div>
                {requestTripEntity.luggageImgContentType ? (
                  <a onClick={openFile(requestTripEntity.luggageImgContentType, requestTripEntity.luggageImg)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {requestTripEntity.luggageImgContentType}, {byteSize(requestTripEntity.luggageImg)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="luggageDescription">
              <Translate contentKey="sTripBeApp.requestTrip.luggageDescription">Luggage Description</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.luggageDescription}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="sTripBeApp.requestTrip.type">Type</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.type}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="sTripBeApp.requestTrip.status">Status</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.status}</dd>
          <dt>
            <span id="pickUpTime">
              <Translate contentKey="sTripBeApp.requestTrip.pickUpTime">Pick Up Time</Translate>
            </span>
          </dt>
          <dd>
            {requestTripEntity.pickUpTime ? <TextFormat value={requestTripEntity.pickUpTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="sTripBeApp.requestTrip.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {requestTripEntity.endTime ? <TextFormat value={requestTripEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="checkIn">
              <Translate contentKey="sTripBeApp.requestTrip.checkIn">Check In</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.checkIn ? 'true' : 'false'}</dd>
          <dt>
            <span id="checkInTime">
              <Translate contentKey="sTripBeApp.requestTrip.checkInTime">Check In Time</Translate>
            </span>
          </dt>
          <dd>
            {requestTripEntity.checkInTime ? (
              <TextFormat value={requestTripEntity.checkInTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="checkOut">
              <Translate contentKey="sTripBeApp.requestTrip.checkOut">Check Out</Translate>
            </span>
          </dt>
          <dd>{requestTripEntity.checkOut ? 'true' : 'false'}</dd>
          <dt>
            <span id="checkOutTIme">
              <Translate contentKey="sTripBeApp.requestTrip.checkOutTIme">Check Out T Ime</Translate>
            </span>
          </dt>
          <dd>
            {requestTripEntity.checkOutTIme ? (
              <TextFormat value={requestTripEntity.checkOutTIme} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="appliedAt">
              <Translate contentKey="sTripBeApp.requestTrip.appliedAt">Applied At</Translate>
            </span>
          </dt>
          <dd>
            {requestTripEntity.appliedAt ? <TextFormat value={requestTripEntity.appliedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="sTripBeApp.requestTrip.trip">Trip</Translate>
          </dt>
          <dd>{requestTripEntity.trip ? requestTripEntity.trip.id : ''}</dd>
          <dt>
            <Translate contentKey="sTripBeApp.requestTrip.user">User</Translate>
          </dt>
          <dd>{requestTripEntity.user ? requestTripEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/request-trip" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/request-trip/${requestTripEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RequestTripDetail;
